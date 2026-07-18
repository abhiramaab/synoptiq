package ai.synoptiq.email.service.impl;

import ai.synoptiq.ai.openai.service.OpenAiService;
import ai.synoptiq.common.exception.NotFoundException;
import ai.synoptiq.email.dto.request.EmailFilterRequest;
import ai.synoptiq.email.dto.response.EmailListResponse;
import ai.synoptiq.email.dto.response.EmailResponse;
import ai.synoptiq.email.dto.response.EmailStatsResponse;
import ai.synoptiq.email.dto.response.EmailSummaryResponse;
import ai.synoptiq.email.entity.Email;
import ai.synoptiq.email.repository.EmailRepository;
import ai.synoptiq.email.service.EmailService;
import ai.synoptiq.integration.gmail.service.GmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final GmailService gmailService;
    private final OpenAiService openAiService;

    @Override
    public void syncEmails() throws Exception {

        var gmailEmails = gmailService.getEmails();

        for (var gmailEmail : gmailEmails) {

            if (emailRepository.findByGmailId(gmailEmail.getId()).isPresent()) {
                continue;
            }

            Email email = Email.builder()
                    .gmailId(gmailEmail.getId())
                    .sender(gmailEmail.getFrom())
                    .subject(gmailEmail.getSubject())
                    .snippet(gmailEmail.getSnippet())
                    .body(gmailEmail.getBody())
                    .summary(null)
                    .summarized(false)
                    .receivedAt(LocalDateTime.now())
                    .build();

            emailRepository.save(email);
        }
    }

    @Override
    public String summarizeEmail(Long id) throws Exception {

        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Email not found"));

        if (Boolean.TRUE.equals(email.getSummarized())
                && email.getSummary() != null
                && !email.getSummary().isBlank()) {
            return email.getSummary();
        }

        String prompt = """

You are Synoptiq AI, a professional email assistant.

Your task is to summarize emails in a clean and consistent format.

Rules:

- Maximum 70 words.
- Use short bullet points.
- Remove greetings, signatures, unsubscribe links and legal disclaimers.
- Highlight important amounts, dates, companies and action items.
- For transaction emails mention merchant, amount and status.
- For promotional emails summarize the offer only.
- For newsletters summarize only the important news.
- Never say "The email contains..." or "This email is about...".
- Return plain text only.

Email:

                """ + email.getBody();

        String summary = openAiService.generateContent(prompt);

        email.setSummary(summary);
        email.setSummarized(true);

        emailRepository.save(email);

        return summary;
    }

    @Override
    public Page<EmailListResponse> getAllEmails(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "receivedAt")
        );

        return emailRepository.findAll(pageable)
                .map(this::mapToListResponse);
    }

    @Override
    public EmailResponse getEmailById(Long id) {

        Email email = emailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Email not found"));

        return mapToResponse(email);
    }

    private EmailResponse mapToResponse(Email email) {

        return EmailResponse.builder()
                .id(email.getId())
                .sender(email.getSender())
                .subject(email.getSubject())
                .snippet(email.getSnippet())
                .body(email.getBody())
                .summary(email.getSummary())
                .summarized(email.getSummarized())
                .receivedAt(email.getReceivedAt())
                .build();
    }

    private EmailListResponse mapToListResponse(Email email) {

        return EmailListResponse.builder()
                .id(email.getId())
                .sender(email.getSender())
                .subject(email.getSubject())
                .snippet(email.getSnippet())
                .summarized(email.getSummarized())
                .receivedAt(email.getReceivedAt())
                .build();
    }

    @Override
    public Page<EmailListResponse> searchEmails(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "receivedAt")
        );

        return emailRepository
                .findBySenderContainingIgnoreCaseOrSubjectContainingIgnoreCaseOrSnippetContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        pageable
                )
                .map(this::mapToListResponse);
    }

    @Override
    public List<EmailSummaryResponse> summarizeEmailsByDateRange(
            LocalDateTime start,
            LocalDateTime end) throws Exception {

        List<Email> emails = emailRepository.findByReceivedAtBetween(start, end);

        List<EmailSummaryResponse> summaries = new ArrayList<>();

        for (Email email : emails) {

            summaries.add(
                    EmailSummaryResponse.builder()
                            .id(email.getId())
                            .sender(email.getSender())
                            .subject(email.getSubject())
                            .receivedAt(email.getReceivedAt())
                            .summary(summarizeEmail(email.getId()))
                            .build()
            );
        }

        return summaries;
    }

    @Override
    public List<EmailSummaryResponse> summarizeEmails(EmailFilterRequest request) throws Exception {

        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        switch (request.getFilter()) {

            case TODAY:
                start = LocalDate.now().atStartOfDay();
                break;

            case YESTERDAY:
                start = LocalDate.now().minusDays(1).atStartOfDay();
                end = LocalDate.now().atStartOfDay();
                break;

            case LAST_7_DAYS:
                start = LocalDateTime.now().minusDays(7);
                break;

            case LAST_30_DAYS:
                start = LocalDateTime.now().minusDays(30);
                break;

            case THIS_MONTH:
                start = LocalDate.now()
                        .withDayOfMonth(1)
                        .atStartOfDay();
                break;

            case CUSTOM:

                if (request.getStart() == null || request.getEnd() == null) {
                    throw new IllegalArgumentException(
                            "Start and End date are required for CUSTOM filter."
                    );
                }

                start = request.getStart();
                end = request.getEnd();
                break;

            default:
                throw new IllegalArgumentException("Invalid filter.");
        }

        List<Email> emails = emailRepository.findByReceivedAtBetween(start, end);

        List<EmailSummaryResponse> summaries = new ArrayList<>();

        for (Email email : emails) {

            summaries.add(
                    EmailSummaryResponse.builder()
                            .id(email.getId())
                            .sender(email.getSender())
                            .subject(email.getSubject())
                            .receivedAt(email.getReceivedAt())
                            .summary(summarizeEmail(email.getId()))
                            .build()
            );
        }

        return summaries;
    }

    @Override
    public EmailStatsResponse getEmailStats() {

        long total = emailRepository.count();

        long summarized = emailRepository.countBySummarizedTrue();

        long unsummarized = emailRepository.countBySummarizedFalse();

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

        long today = emailRepository.countByReceivedAtAfter(startOfToday);

        return new EmailStatsResponse(
                total,
                summarized,
                unsummarized,
                today
        );
    }
}