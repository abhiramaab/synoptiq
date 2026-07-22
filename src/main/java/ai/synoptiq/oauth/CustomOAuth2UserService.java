package ai.synoptiq.oauth;

import ai.synoptiq.common.constants.Provider;
import ai.synoptiq.common.constants.Role;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

@PostConstruct
public void init() {
    System.out.println(">>> CustomOAuth2UserService bean created");
}

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        System.out.println("########################################");
        System.out.println("######## CUSTOM OAUTH SERVICE ##########");
        System.out.println("########################################");

        OAuth2User oauthUser = super.loadUser(request);

        Map<String, Object> attributes = oauthUser.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String googleId = (String) attributes.get("sub");
        String picture = (String) attributes.get("picture");

        User user = userRepository.findByEmail(email)
                .orElseGet(User::new);

        user.setEmail(email);
        user.setUsername(name);
        user.setGoogleId(googleId);
        user.setProfilePicture(picture);
        user.setRole(Role.USER);
        user.setProvider(Provider.GOOGLE);

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        user.setUpdatedAt(LocalDateTime.now());

        user = userRepository.saveAndFlush(user);

        System.out.println("================================");
        System.out.println("USER SAVED SUCCESSFULLY");
        System.out.println("ID    : " + user.getId());
        System.out.println("EMAIL : " + user.getEmail());
        System.out.println("================================");

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }
}
