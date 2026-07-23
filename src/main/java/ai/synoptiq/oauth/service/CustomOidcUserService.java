package ai.synoptiq.oauth;

import ai.synoptiq.common.constants.Provider;
import ai.synoptiq.common.constants.Role;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Google's OAuth scope includes "openid", so Spring Security runs the
 * OIDC login flow. In that flow, Spring Security calls an
 * OidcUserService (configured via .oidcUserService(...)), NOT the plain
 * OAuth2UserService registered via .userService(...).
 *
 * This is the service that actually gets invoked for Google login.
 */
@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        // Let Spring do the actual call to Google's userinfo/id-token parsing first
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email not returned by OAuth2 provider");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        User user = existingUser.orElseGet(User::new);

        user.setEmail(email);
        user.setUsername(oidcUser.getAttribute("name"));
        user.setGoogleId(oidcUser.getAttribute("sub"));
        user.setProfilePicture(oidcUser.getAttribute("picture"));
        user.setProvider(Provider.GOOGLE);
        user.setUpdatedAt(LocalDateTime.now());

        if (existingUser.isEmpty()) {
            user.setRole(Role.USER);
            user.setCreatedAt(LocalDateTime.now());
        }

        userRepository.saveAndFlush(user);

        return oidcUser;
    }
}