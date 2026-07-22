package ai.synoptiq.chat;

import ai.synoptiq.chat.dto.ChatRequest;
import ai.synoptiq.chat.dto.ChatResponse;
import ai.synoptiq.chat.dto.MessageResponse;
import ai.synoptiq.chat.service.ChatService;
import ai.synoptiq.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatResponse response = chatService.chat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<MessageResponse>> getChatHistory() {
        return ResponseEntity.ok(chatService.getChatHistory());
    }
}
