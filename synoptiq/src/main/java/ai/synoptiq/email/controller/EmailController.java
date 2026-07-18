package ai.synoptiq.email.controller;

import ai.synoptiq.email.dto.request.EmailFilterRequest;
import ai.synoptiq.email.dto.response.*;
import ai.synoptiq.email.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(
        name = "Email Management",
        description = "Operations for synchronizing, retrieving, searching and summarizing emails."
)
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sync")
    @Operation(
            summary = "Synchronize Gmail",
            description = "Fetches emails from Gmail and stores only new emails in PostgreSQL."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Synchronization completed"),
            @ApiResponse(responseCode = "500", description = "Synchronization failed")
    })
    public ResponseEntity<String> syncEmails() throws Exception {

        emailService.syncEmails();

        return ResponseEntity.ok("Email Sync Completed");
    }

    @PostMapping("/{id}/summarize")
    @Operation(
            summary = "Summarize Email",
            description = "Generates an AI summary for the selected email. If already summarized, returns the cached summary."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Summary generated successfully"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "500", description = "Gemini service unavailable")
    })
    public ResponseEntity<SummaryResponse> summarizeEmail(@PathVariable Long id) throws Exception {

        String summary = emailService.summarizeEmail(id);

        return ResponseEntity.ok(new SummaryResponse(summary));
    }

    @GetMapping
    @Operation(
            summary = "Get Emails",
            description = "Returns paginated emails sorted by received date."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emails fetched successfully"),
            @ApiResponse(responseCode = "401", description = "User is not authenticated with Gmail"),
            @ApiResponse(responseCode = "500", description = "Failed to retrieve Gmail messages")
    })
    public ResponseEntity<Page<EmailListResponse>> getAllEmails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(emailService.getAllEmails(page, size));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Email",
            description = "Returns a single email by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EmailResponse> getEmailById(@PathVariable Long id) {

        return ResponseEntity.ok(emailService.getEmailById(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search Emails",
            description = "Search emails by sender, subject or snippet."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<EmailListResponse>> searchEmails(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                emailService.searchEmails(keyword, page, size)
        );
    }

    @GetMapping("/summarize/date-range")
    @Operation(
            summary = "Summarize Emails by Date Range",
            description = "Summarizes all emails received between the given start and end date."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emails summarized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date range"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EmailSummaryResponse>> summarizeEmailsByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
    ) throws Exception {

        return ResponseEntity.ok(
                emailService.summarizeEmailsByDateRange(start, end)
        );
    }

    @PostMapping("/summarize")
    @Operation(
            summary = "Summarize Emails",
            description = "Summarizes emails based on the selected filter."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Emails summarized successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EmailSummaryResponse>> summarizeEmails(
            @Valid @RequestBody EmailFilterRequest request
    ) throws Exception {

        return ResponseEntity.ok(
                emailService.summarizeEmails(request)
        );
    }

    @GetMapping("/stats")
    @Operation(
            summary = "Get email statistics",
            description = "Retrieves overall email statistics including total emails, summarized emails, unsummarized emails, and emails received today."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email statistics retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmailStatsResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<EmailStatsResponse> getEmailStats() {
        return ResponseEntity.ok(emailService.getEmailStats());
    }

}