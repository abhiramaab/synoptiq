package ai.synoptiq.integration.gmail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GmailMessageDTO {

    private String id;
    private String from;
    private String subject;
    private String snippet;
    private String body;
    private String summary;
}
