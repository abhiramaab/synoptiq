package ai.synoptiq.integration.gmail.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

public class GoogleAuthorizationUtil {

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    public static Credential authorize() throws Exception {

        InputStream in = GoogleAuthorizationUtil.class
                .getClassLoader()
                .getResourceAsStream("credentials.json");

        if (in == null) {
            throw new RuntimeException("credentials.json not found.");
        }

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        GsonFactory.getDefaultInstance(),
                        new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        clientSecrets,
                        Collections.singletonList("https://www.googleapis.com/auth/gmail.readonly"))
                        .setDataStoreFactory(
                                new FileDataStoreFactory(
                                        new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline")
                        .build();

        LocalServerReceiver receiver =
                new LocalServerReceiver.Builder()
                        .setPort(8889)
                        .build();

        return new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");
    }
}