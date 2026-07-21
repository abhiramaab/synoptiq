package ai.synoptiq.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.HashMap;
import java.util.Map;

public class OAuth2AuthorizationRequestResolverConfig
        implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public OAuth2AuthorizationRequestResolverConfig(
            ClientRegistrationRepository repository) {

        defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(
                        repository,
                        "/oauth2/authorization"
                );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {

        OAuth2AuthorizationRequest requestObject =
                defaultResolver.resolve(request);

        return customize(requestObject);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(
            HttpServletRequest request,
            String clientRegistrationId) {

        OAuth2AuthorizationRequest requestObject =
                defaultResolver.resolve(request, clientRegistrationId);

        return customize(requestObject);
    }

    private OAuth2AuthorizationRequest customize(
            OAuth2AuthorizationRequest request) {

        if (request == null) {
            return null;
        }

        Map<String, Object> parameters =
                new HashMap<>(request.getAdditionalParameters());

        parameters.put("access_type", "offline");
        parameters.put("prompt", "consent");

        return OAuth2AuthorizationRequest
                .from(request)
                .additionalParameters(parameters)
                .build();
    }

}