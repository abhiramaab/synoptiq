package ai.synoptiq.email.repository;

import ai.synoptiq.email.entity.Email;
import ai.synoptiq.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findByGmailIdAndUser(String gmailId, User user);

    Optional<Email> findByIdAndUser(Long id, User user);

    Page<Email> findByUser(
            User user,
            Pageable pageable
    );

    Page<Email> findByUserAndSenderContainingIgnoreCaseOrUserAndSubjectContainingIgnoreCaseOrUserAndSnippetContainingIgnoreCase(
            User user1,
            String sender,
            User user2,
            String subject,
            User user3,
            String snippet,
            Pageable pageable
    );

    List<Email> findByUserAndReceivedAtBetween(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    long countByUser(User user);

    long countByUserAndSummarizedTrue(User user);

    long countByUserAndSummarizedFalse(User user);

    long countByUserAndReceivedAtAfter(
            User user,
            LocalDateTime dateTime
    );
}