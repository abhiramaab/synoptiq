package ai.synoptiq.integration.gmail.controller;

import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import ai.synoptiq.integration.gmail.service.GmailService;
import ai.synoptiq.user.entity.User;
import com.google.api.services.gmail.model.ListMessagesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gmail")
@RequiredArgsConstructor
@Tag(
        name = "Gmail Integration",
        description = "Google Gmail OAuth and Gmail API operations."
)
public class GmailController {

    private final GmailService gmailService;

    @GetMapping("/emails")
    @Operation(
            summary = "Fetch Gmail Emails",
            description = "Retrieves emails from the authenticated Gmail account using the Gmail API."
    )
    public List<GmailMessageDTO> getEmails(User user) throws Exception  {
        return gmailService.getEmails(user);
    }
}
