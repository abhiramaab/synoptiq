package ai.synoptiq.ai.openai.service.impl;

import ai.synoptiq.ai.openai.service.OpenAiService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Override
    public String generateContent(String prompt) throws Exception {

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-5-mini",
                "messages", new Object[]{
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                }
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        JsonNode root = objectMapper.readTree(response.getBody());

        if (root.has("error")) {
            throw new RuntimeException(
                    root.path("error")
                            .path("message")
                            .asText("Unknown OpenAI error")
            );
        }

        return root.path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText()
                .trim();
    }
}