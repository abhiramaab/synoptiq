package ai.synoptiq.chat.service;

import ai.synoptiq.chat.entity.Message;
import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;

import java.util.List;

public interface PromptBuilderService {

    String buildEmailPrompt(String userMessage, List<GmailMessageDTO> emails);
    String buildConversationPrompt(String currentMessage, List<Message> messages);
}
