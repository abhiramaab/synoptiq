package ai.synoptiq.oauth;

import ai.synoptiq.security.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final GoogleOAuthTokenService googleOAuthTokenService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        System.out.println();
        System.out.println("========================================");
        System.out.println("===== SUCCESS HANDLER CALLED =====");
        System.out.println("========================================");

        // Authentication details
        System.out.println("Authentication Class : " + authentication.getClass().getName());
        System.out.println("Principal Class      : " + authentication.getPrincipal().getClass().getName());
        System.out.println("Authorities          : " + authentication.getAuthorities());
        System.out.println("Authenticated        : " + authentication.isAuthenticated());

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        System.out.println("----------------------------------------");
        System.out.println("OAuth User Class     : " + oauthUser.getClass().getName());
        System.out.println("Attributes           : " + oauthUser.getAttributes());
        System.out.println("----------------------------------------");

        String email = oauthUser.getAttribute("email");

        System.out.println("Email = " + email);

        try {
            System.out.println("Calling GoogleOAuthTokenService...");
            googleOAuthTokenService.saveGoogleTokens(authentication);
            System.out.println("GoogleOAuthTokenService completed.");
        } catch (Exception e) {
            System.out.println("Failed to save Google tokens");
            e.printStackTrace();
        }

        String jwt = jwtService.generateToken(email);

        System.out.println("JWT Generated Successfully");
        System.out.println("Redirect URL = " + frontendUrl + "/oauth-success?token=<JWT>");
        System.out.println("========================================");
        System.out.println();

        response.sendRedirect(frontendUrl + "/oauth-success?token=" + jwt);
    }
}