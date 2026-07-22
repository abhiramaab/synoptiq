package ai.synoptiq.email.repository;

import ai.synoptiq.email.entity.Notification;
import ai.synoptiq.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>
{

    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
