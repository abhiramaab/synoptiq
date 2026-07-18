package ai.synoptiq.email.service;

import ai.synoptiq.email.dto.request.EmailFilterRequest;
import ai.synoptiq.email.dto.response.EmailListResponse;
import ai.synoptiq.email.dto.response.EmailResponse;
import ai.synoptiq.email.dto.response.EmailStatsResponse;
import ai.synoptiq.email.dto.response.EmailSummaryResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailService {

    void syncEmails() throws Exception;

    String summarizeEmail(Long id) throws Exception;

    Page<EmailListResponse> getAllEmails(int page, int size);

    EmailResponse getEmailById(Long id);

    Page<EmailListResponse> searchEmails(String keyword, int page, int size);

    List<EmailSummaryResponse> summarizeEmailsByDateRange(LocalDateTime start, LocalDateTime end) throws Exception;

    List<EmailSummaryResponse> summarizeEmails(EmailFilterRequest request) throws Exception;

    EmailStatsResponse getEmailStats();
}