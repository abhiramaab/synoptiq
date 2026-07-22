package ai.synoptiq.email.service.impl;

import ai.synoptiq.email.entity.Email;
import ai.synoptiq.email.entity.WatchThread;
import ai.synoptiq.email.repository.EmailRepository;
import ai.synoptiq.email.repository.WatchRepository;
import ai.synoptiq.email.service.WatchService;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WatchServiceImpl implements WatchService {

    private final WatchRepository watchRepository;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    @Override
    public void watchEmail(Long emailId) {

        User user = getCurrentUser();

        Email email = emailRepository.findByIdAndUser(emailId, user)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (watchRepository.existsByThreadIdAndUser(email.getThreadId(), user)) {
            throw new RuntimeException("This thread is already being watched.");
        }

        WatchThread watchThread = WatchThread.builder()
                .threadId(email.getThreadId())
                .subject(email.getSubject())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        watchRepository.save(watchThread);
    }

    private User getCurrentUser() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof User user)) {
            throw new RuntimeException("User not authenticated");
        }

        return user;
    }
}