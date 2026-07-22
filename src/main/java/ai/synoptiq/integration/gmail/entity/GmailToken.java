package ai.synoptiq.integration.gmail.entity;

import ai.synoptiq.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name="gmail_tokens")
@Data
public class GmailToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Column(length = 5000)
    private String accessToken;

    @Column(length = 5000)
    private String refreshToken;

    private Instant expiryTime;
}
