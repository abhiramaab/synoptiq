package ai.synoptiq.ai.openai.controller;

import ai.synoptiq.ai.openai.service.OpenAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
@Tag(
        name = "AI Services",
        description = "Gemini AI operations."
)
public class OpenAiController {

    private final OpenAiService geminiService;

    @PostMapping("/test")
    @Operation(
            summary = "Gemini Test",
            description = "Sends a prompt to Gemini and returns the generated response."
    )
    public String test(@Valid @RequestBody String prompt) throws Exception {
        return geminiService.generateContent(prompt);
    }
}
