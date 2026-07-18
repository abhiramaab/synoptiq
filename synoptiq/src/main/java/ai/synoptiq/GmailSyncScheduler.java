package ai.synoptiq;

import ai.synoptiq.email.service.EmailService;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GmailSyncScheduler {

    private final EmailService emailService;
    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 300000)
    public void syncEmails() {
        for (User user : userRepository.findAll()) {

            try {
                emailService.syncEmails(user);
            } catch (Exception e) {
                log.error("Failed while syncing for {}", user.getEmail(), e);
            }
        }
    }
}
