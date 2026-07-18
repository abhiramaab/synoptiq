package ai.synoptiq.email.entity;

import ai.synoptiq.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private boolean isRead;
    private String title;

    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Email email;
}
