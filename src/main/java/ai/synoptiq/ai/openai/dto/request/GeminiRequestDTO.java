package ai.synoptiq.ai.openai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeminiRequestDTO {

    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;
}
