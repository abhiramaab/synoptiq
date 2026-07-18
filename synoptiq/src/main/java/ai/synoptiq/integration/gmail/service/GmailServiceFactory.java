package ai.synoptiq.integration.gmail.service;

import ai.synoptiq.integration.gmail.entity.GmailToken;
import ai.synoptiq.integration.gmail.repository.GmailTokenRepository;
import ai.synoptiq.user.entity.User;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.UserCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class GmailServiceFactory {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    private static final String APPLICATION_NAME = "Synoptiq AI";

    private final GmailTokenRepository gmailTokenRepository;

    public Gmail create(User user) throws Exception {

        GmailToken gmailToken = gmailTokenRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Google account not connected."));

        UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAccessToken(
                        new AccessToken(
                                gmailToken.getAccessToken(),
                                Date.from(gmailToken.getExpiryTime())
                        )
                )
                .setRefreshToken(gmailToken.getRefreshToken())
                .build();

        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new com.google.auth.http.HttpCredentialsAdapter(credentials)
        )
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}