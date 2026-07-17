package ai.synoptiq.email.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "emails")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gmailId;

    private String sender;

    private String subject;


    @Column(columnDefinition = "TEXT")
    private String body;


    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String snippet;

    @Builder.Default
    private Boolean summarized = false;

    private LocalDateTime receivedAt;
}