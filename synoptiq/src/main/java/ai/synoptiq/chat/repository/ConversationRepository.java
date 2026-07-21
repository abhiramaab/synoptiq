package ai.synoptiq.chat.repository;

import ai.synoptiq.chat.entity.Conversation;
import ai.synoptiq.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findByUser(User user);
    Optional<Conversation> findByIdAndUser(Long id, User user);
}
