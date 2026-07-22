package ai.synoptiq.integration.gmail.service.impl;

import ai.synoptiq.integration.gmail.client.GmailClientProvider;
import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import ai.synoptiq.integration.gmail.service.GmailService;
import ai.synoptiq.user.entity.User;
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

    private final GmailClientProvider gmailClientProvider;

    @Override
    public List<GmailMessageDTO> getEmails(User user) throws Exception {


        Gmail gmail = gmailClientProvider.getClient(user);

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
                    fullMessage.getThreadId(),
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

        if ("text/html".equalsIgnoreCase(part.getMimeType())) {

            MessagePartBody body = part.getBody();

            if (body != null && body.getData() != null) {

                String html = decodeBase64(body.getData());

                if (!html.isBlank()) {
                    return html;
                }
            }
        }

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

            for (MessagePart child : part.getParts()) {

                String result = extractBody(child);

                if (!result.isBlank()) {
                    return result;
                }
            }
        }

        return "";
    }

    private String decodeBase64(String data) {

        byte[] decodedBytes =
                Base64.getUrlDecoder().decode(data);

        return Jsoup.parse(
                new String(decodedBytes, StandardCharsets.UTF_8)
        ).text().trim();
    }
}