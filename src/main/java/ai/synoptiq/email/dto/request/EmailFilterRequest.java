package ai.synoptiq.email.dto.request;

import ai.synoptiq.email.enums.EmailFilter;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailFilterRequest {

    @NotNull(message = "Filter is required")
    private EmailFilter filter;

    private LocalDateTime start;

    private LocalDateTime end;
}
