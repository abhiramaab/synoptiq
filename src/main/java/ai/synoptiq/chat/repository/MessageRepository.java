package ai.synoptiq.chat.repository;

import ai.synoptiq.chat.entity.Conversation;
import ai.synoptiq.chat.entity.Message;
import ai.synoptiq.chat.enums.MessageRole;
import ai.synoptiq.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);

    Long countByRole(MessageRole role);

    Long countByRoleAndConversation_UserAndCreatedAtAfter(
            MessageRole role, User user, LocalDateTime after);
}