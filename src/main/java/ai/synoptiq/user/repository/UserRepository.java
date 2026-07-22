package ai.synoptiq.user.repository;

import ai.synoptiq.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ai.synoptiq.user.entity.User, Long> {

    Optional<User> findByEmail(String username);
    boolean existsByEmail(String email);
}
