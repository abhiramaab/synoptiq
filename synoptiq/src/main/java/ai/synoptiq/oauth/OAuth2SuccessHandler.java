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

        System.out.println("===== SUCCESS HANDLER CALLED =====");

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");

        System.out.println("Email = " + email);

       // googleOAuthTokenService.saveGoogleTokens(authentication);

        System.out.println("Returned from saveGoogleTokens()");

        try {
            System.out.println("Generating JWT...");
            String jwt = jwtService.generateToken(email);
            System.out.println("JWT generated successfully");

            response.sendRedirect(frontendUrl + "/oauth-success?token=" + jwt);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

}