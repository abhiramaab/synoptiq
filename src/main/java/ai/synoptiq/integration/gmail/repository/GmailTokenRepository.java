package ai.synoptiq.integration.gmail.repository;

import ai.synoptiq.integration.gmail.entity.GmailToken;
import ai.synoptiq.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GmailTokenRepository extends JpaRepository<GmailToken, Long>
{

    Optional<GmailToken> findByUser(User user);
}
