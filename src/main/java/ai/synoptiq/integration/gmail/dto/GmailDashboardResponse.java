package ai.synoptiq.integration.gmail.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GmailDashboardResponse {

    private Long emailSynced;
    private Long unread;
    private Long summariesCreated;
    private Long watchlist;
    private Long notifications;
    private List<GmailMessageDTO> latestEmails;
}
