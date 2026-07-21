package ai.synoptiq.chat.service;

import ai.synoptiq.chat.dto.ChatRequest;
import ai.synoptiq.chat.dto.ChatResponse;
import ai.synoptiq.chat.dto.MessageResponse;
import ai.synoptiq.user.entity.User;

import java.util.List;

public interface ChatService {

    ChatResponse chat(ChatRequest request);
    List<MessageResponse> getChatHistory();
}
