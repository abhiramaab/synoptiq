package ai.synoptiq.integration.gmail.client.impl;

import ai.synoptiq.integration.gmail.client.GmailClientProvider;
import ai.synoptiq.integration.gmail.entity.GmailToken;
import ai.synoptiq.integration.gmail.repository.GmailTokenRepository;
import ai.synoptiq.user.entity.User;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class GmailClientProviderImpl implements GmailClientProvider {

    private static final String APPLICATION_NAME = "Synoptiq AI";

    private final GmailTokenRepository gmailTokenRepository;

    private final NetHttpTransport netHttpTransport;

    private final JsonFactory jsonFactory;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Override
    public Gmail getClient(User user) throws Exception {

        GmailToken gmailToken = gmailTokenRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Google account not connected."));

        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(gmailToken.getRefreshToken())
                .setAccessToken(
                        new AccessToken(
                                gmailToken.getAccessToken(),
                                Date.from(gmailToken.getExpiryTime())
                        )
                )
                .build();

        HttpRequestInitializer requestInitializer =
                new HttpCredentialsAdapter(credentials);

        return new Gmail.Builder(
                netHttpTransport,
                jsonFactory,
                requestInitializer
        )
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}