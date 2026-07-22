package ai.synoptiq.email.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailListResponse {

    private Long id;
    private String sender;
    private String subject;
    private String snippet;
    private Boolean summarized;
    private LocalDateTime receivedAt;

}