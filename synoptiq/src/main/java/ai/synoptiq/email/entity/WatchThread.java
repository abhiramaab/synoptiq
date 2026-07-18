package ai.synoptiq.email.entity;

import ai.synoptiq.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "watched_threads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String threadId;

    private String subject;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}