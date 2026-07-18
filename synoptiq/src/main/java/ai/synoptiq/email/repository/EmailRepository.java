package ai.synoptiq.email.repository;

import ai.synoptiq.email.entity.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long>
{
    Optional<Email> findByGmailId(String gmailId);
    Optional<Email> findById(Long id);
    Page<Email> findBySenderContainingIgnoreCaseOrSubjectContainingIgnoreCaseOrSnippetContainingIgnoreCase(
            String sender,
            String subject,
            String snippet,
            Pageable pageable
    );
    List<Email> findByReceivedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    long countBySummarizedTrue();

    long countBySummarizedFalse();

    long countByReceivedAtAfter(LocalDateTime dateTime);
}
