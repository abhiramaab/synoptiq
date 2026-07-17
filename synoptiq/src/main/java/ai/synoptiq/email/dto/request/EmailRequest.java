package ai.synoptiq.email.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {


    private String gmailId;

    private String sender;

    private String subject;


    private String snippet;


    private String body;



    private String summary;

    private Boolean summarized = false;

    private LocalDateTime receivedAt;
}
