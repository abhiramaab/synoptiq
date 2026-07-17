package ai.synoptiq.integration.gmail.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GmailConfig {

    @Bean
    public NetHttpTransport netHttpTransport() throws Exception {
        return GoogleNetHttpTransport.newTrustedTransport();
    }
}
