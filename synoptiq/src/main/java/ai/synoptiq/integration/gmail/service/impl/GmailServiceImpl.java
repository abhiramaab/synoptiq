package ai.synoptiq.integration.gmail.service.impl;

import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import ai.synoptiq.integration.gmail.service.GmailService;
import ai.synoptiq.integration.gmail.util.GoogleAuthorizationUtil;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GmailServiceImpl implements GmailService {

    private static final String APPLICATION_NAME = "Synoptiq AI";

    @Override
    public List<GmailMessageDTO> getEmails() throws Exception {

        Gmail gmail = new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleAuthorizationUtil.authorize())
                .setApplicationName(APPLICATION_NAME)
                .build();

        ListMessagesResponse response = gmail.users()
                .messages()
                .list("me")
                .setMaxResults(20L)
                .execute();

        List<GmailMessageDTO> emails = new ArrayList<>();

        if (response.getMessages() == null) {
            return emails;
        }

        for (Message message : response.getMessages()) {

            Message fullMessage = gmail.users()
                    .messages()
                    .get("me", message.getId())
                    .setFormat("full")
                    .execute();

            String from = "";
            String subject = "";

            for (MessagePartHeader header : fullMessage.getPayload().getHeaders()) {

                if ("From".equalsIgnoreCase(header.getName())) {
                    from = header.getValue();
                }

                if ("Subject".equalsIgnoreCase(header.getName())) {
                    subject = header.getValue();
                }
            }

            String body = extractBody(fullMessage.getPayload());

            emails.add(new GmailMessageDTO(
                    fullMessage.getId(),
                    from,
                    subject,
                    fullMessage.getSnippet(),
                    body,
                    null
            ));
        }

        return emails;
    }

    private String extractBody(MessagePart part) {

        if (part == null) {
            return "";
        }

        // Prefer HTML because many emails have richer HTML content.
        if ("text/html".equalsIgnoreCase(part.getMimeType())) {

            MessagePartBody body = part.getBody();

            if (body != null && body.getData() != null) {
                String html = decodeBase64(body.getData());

                if (!html.isBlank()) {
                    return html;
                }
            }
        }

        // Then use plain text.
        if ("text/plain".equalsIgnoreCase(part.getMimeType())) {

            MessagePartBody body = part.getBody();

            if (body != null && body.getData() != null) {
                String text = decodeBase64(body.getData());

                if (!text.isBlank()) {
                    return text;
                }
            }
        }

        if (part.getParts() != null) {

            String plainText = "";

            for (MessagePart child : part.getParts()) {

                String result = extractBody(child);

                if (result.isBlank()) {
                    continue;
                }

                // Return immediately if it's clearly a real email.
                if (result.length() > 50) {
                    return result;
                }

                // Keep short text as fallback.
                plainText = result;
            }

            return plainText;
        }

        return "";
    }

    private String decodeBase64(String data) {

        if (data == null || data.isBlank()) {
            return "";
        }

        byte[] decodedBytes = Base64.getUrlDecoder().decode(data);

        String text = new String(decodedBytes, StandardCharsets.UTF_8);

        return Jsoup.parse(text).text().trim();
    }
}