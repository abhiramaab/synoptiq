package ai.synoptiq.chat.service;


import ai.synoptiq.chat.entity.Message;
import ai.synoptiq.chat.repository.MessageRepository;
import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptBuilderServiceImpl implements PromptBuilderService {

    private final MessageRepository messageRepository;

    @Override
    public String buildEmailPrompt(String userMessage, List<GmailMessageDTO> emails) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are Synoptiq AI.
                
                                You are an intelligent assistant that helps users understand
                                and manage their emails.
                """);

        prompt.append("User Request:\n")
                .append(userMessage)
                .append("\n");

        prompt.append("Emails:\n\n");

        for (GmailMessageDTO email : emails) {
            prompt.append("From: ")
                    .append(email.getFrom())
                    .append("\n");

            prompt.append("Subject: ")
                    .append(email.getSubject())
                    .append("\n");

            prompt.append("Snippet: ")
                    .append(email.getSnippet())
                    .append("\n");

            if (email.getBody() != null && !email.getBody().isBlank()) {
                prompt.append("Body: ")
                        .append(email.getBody(), 0, Math.min(email.getBody().length(), 500))
                        .append("\n");
            }
            prompt.append("------------------------------\n");
        }

        prompt.append("""
                
                Instructions
                
                                • Answer ONLY using these emails.
                
                                • If the user asks for summaries,
                                  summarize them.
                
                                • If the answer is not available,
                                  clearly say so.
                
                                • Never invent information.
                
                                • Be concise.
                                
                """);
        return prompt.toString();
    }

    @Override
    public String buildConversationPrompt(String currentMessage, List<Message> messages) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("""
                You are Synoptiq AI.
                
                Continue the conversation naturally.
                
                Previous Conversation:
        """);

        for (Message message : messages) {
            prompt.append(message.getRole())
                    .append(": ")
                    .append(message.getContent())
                    .append("\n");
        }

        prompt.append("\nCurrent User Message:\n");
        prompt.append(currentMessage);
        return prompt.toString();
    }
}
