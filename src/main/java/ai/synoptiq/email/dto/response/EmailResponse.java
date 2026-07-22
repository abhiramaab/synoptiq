package ai.synoptiq.email.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponse {

    private Long id;
    private String sender;
    private String subject;
    private String snippet;
    private String body;
    private String summary;
    private Boolean summarized;
    private LocalDateTime receivedAt;
}
