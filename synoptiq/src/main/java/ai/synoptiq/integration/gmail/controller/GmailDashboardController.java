package ai.synoptiq.integration.gmail.controller;

import ai.synoptiq.integration.gmail.dto.GmailDashboardResponse;
import ai.synoptiq.integration.gmail.service.GmailDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gmail")
@RequiredArgsConstructor
public class GmailDashboardController {

    private final GmailDashboardService gmailDashboardService;

    @GetMapping("/dashboard")
    public GmailDashboardResponse getDashboard() {
        return gmailDashboardService.getDashboard();
    }
}
