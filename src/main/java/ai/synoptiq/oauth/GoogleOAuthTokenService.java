package ai.synoptiq.oauth;

import ai.synoptiq.integration.gmail.entity.GmailToken;
import ai.synoptiq.integration.gmail.repository.GmailTokenRepository;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleOAuthTokenService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final GmailTokenRepository gmailTokenRepository;
    private final UserRepository userRepository;

    public void saveGoogleTokens(Authentication authentication) {

        System.out.println("===== saveGoogleTokens() CALLED =====");

        System.out.println("authentication.getName() = " + authentication.getName());

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        System.out.println("email = " + oauthUser.getAttribute("email"));
        System.out.println("sub = " + oauthUser.getAttribute("sub"));
        System.out.println("name = " + oauthUser.getAttribute("name"));

        OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient(
                        "google",
                        authentication.getName()
                );

        System.out.println("Authentication Name: " + authentication.getName());

        if (client == null) {
            throw new RuntimeException("OAuth2AuthorizedClient is NULL");
        }

        System.out.println("========== CLIENT FOUND ==========");
        System.out.println("Access Token: " + client.getAccessToken().getTokenValue());

        if (client.getRefreshToken() == null) {
            System.out.println("Refresh Token: NULL");
        } else {
            System.out.println("Refresh Token FOUND");
        }

        String email = oauthUser.getAttribute("email");

        System.out.println("Email: " + email);

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found: " + email);
        }

        User user = optionalUser.get();

        GmailToken gmailToken = gmailTokenRepository
                .findByUser(user)
                .orElse(new GmailToken());

        gmailToken.setUser(user);

        gmailToken.setAccessToken(
                client.getAccessToken().getTokenValue()
        );

        if (client.getRefreshToken() != null) {
            gmailToken.setRefreshToken(
                    client.getRefreshToken().getTokenValue()
            );
        }

        if (client.getAccessToken().getExpiresAt() != null) {
            Instant expiry = client.getAccessToken().getExpiresAt();
            gmailToken.setExpiryTime(expiry);
        }

        System.out.println("Saving GmailToken for " + email);

        gmailTokenRepository.save(gmailToken);

        System.out.println("GmailToken saved successfully.");
    }
}