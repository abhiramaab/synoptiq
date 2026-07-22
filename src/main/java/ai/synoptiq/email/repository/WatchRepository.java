package ai.synoptiq.email.repository;

import ai.synoptiq.email.entity.WatchThread;
import ai.synoptiq.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchRepository extends JpaRepository<WatchThread, Long> {

    Optional<WatchThread> findByThreadIdAndUser(String threadId, User user);

    boolean existsByThreadIdAndUser(String threadId, User user);

    List<WatchThread> findByUser(User user);
}
