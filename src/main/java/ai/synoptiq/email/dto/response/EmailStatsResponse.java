package ai.synoptiq.email.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailStatsResponse {

    private long totalEmails;
    private long summarizedEmails;
    private long unsummarizedEmails;
    private long todayEmails;
}
