package ai.synoptiq.email.dto.response;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmailSummaryResponse {

    private Long id;
    private String sender;
    private String subject;
    private LocalDateTime receivedAt;
    private String summary;


}
