package ai.synoptiq.chat.dto;

import ai.synoptiq.chat.enums.MessageRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private MessageRole role;
    private String content;
    private LocalDateTime createdAt;
}
