package ai.synoptiq.integration.gmail.service;

import ai.synoptiq.chat.enums.MessageRole;
import ai.synoptiq.chat.repository.MessageRepository;
import ai.synoptiq.integration.gmail.dto.GmailDashboardResponse;
import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import ai.synoptiq.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GmailDashboardServiceImpl implements GmailDashboardService {

    private final GmailService gmailService;
    private final MessageRepository messageRepository;

    @Override
    public GmailDashboardResponse getDashboard() {

        User user = getCurrentUser();

        List<GmailMessageDTO> emails;

        try {
            emails = gmailService.getEmails(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Long unread = 0L;

        Long summaries = messageRepository.countByRole(MessageRole.ASSISTANT);

        return GmailDashboardResponse.builder()
                .emailSynced((long) emails.size())
                .unread(unread)
                .summariesCreated(summaries)
                .watchlist(0L)
                .notifications(0L)
                .latestEmails(emails)
                .build();
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
