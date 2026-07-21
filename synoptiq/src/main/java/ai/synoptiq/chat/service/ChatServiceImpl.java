package ai.synoptiq.chat.service;

import ai.synoptiq.ai.openai.service.OpenAiService;
import ai.synoptiq.chat.dto.ChatRequest;
import ai.synoptiq.chat.dto.ChatResponse;
import ai.synoptiq.chat.dto.MessageResponse;
import ai.synoptiq.chat.entity.Conversation;
import ai.synoptiq.chat.entity.Message;
import ai.synoptiq.chat.enums.ChatIntent;
import ai.synoptiq.chat.enums.MessageRole;
import ai.synoptiq.chat.repository.ConversationRepository;
import ai.synoptiq.chat.repository.MessageRepository;
import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import ai.synoptiq.integration.gmail.service.GmailService;
import ai.synoptiq.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final GmailService gmailService;
    private final OpenAiService openAiService;
    private final PromptBuilderService promptBuilderService;
    private final IntentService intentService;

    @Override
    public ChatResponse chat(ChatRequest request) {
        String userMessage = request.getMessage();
        User user = getCurrentUser();
        Conversation conversation = getOrCreateConversation(user);

        saveMessage(conversation, MessageRole.USER, userMessage);
        ChatIntent intent = intentService.detectIntent(userMessage);

        try {
            if (intent == ChatIntent.EMAIL) {
                return handleEmailRequest(userMessage, user, conversation);
            }

            List<Message> messages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
            String prompt = promptBuilderService.buildConversationPrompt(userMessage, messages);
            String aiResponse = openAiService.generateContent(prompt);
            saveMessage(conversation, MessageRole.ASSISTANT, aiResponse);
            return new ChatResponse(aiResponse);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<MessageResponse> getChatHistory() {
        User user = getCurrentUser();

        Conversation conversation = conversationRepository.findByUser(user).orElseThrow(() -> new RuntimeException("No conversation found"));

        List<Message> messages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
        return messages.stream().map(this::mapToMessageResponse).toList();
    }

    private ChatResponse handleEmailRequest(String userMessage, User user, Conversation conversation) throws Exception {

        List<GmailMessageDTO> emails = gmailService.getEmails(user);

        String prompt = promptBuilderService.buildEmailPrompt(userMessage, emails);

        String aiResponse = openAiService.generateContent(prompt);

        saveMessage(conversation, MessageRole.ASSISTANT, aiResponse);

        return new ChatResponse(aiResponse);
    }


    private User getCurrentUser() {

        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof User user)) {
            throw new RuntimeException("User not authenticated");
        }

        return user;
    }

    private Conversation getOrCreateConversation(User user) {

        return conversationRepository.findByUser(user).orElseGet(() ->{
            Conversation conversation = Conversation.builder()
                    .user(user)
                    .build();

            return conversationRepository.save(conversation);
        });
    }

    private void saveMessage(Conversation conversation, MessageRole role, String content) {
        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .build();

        messageRepository.save(message);
    }

    private MessageResponse mapToMessageResponse(Message message) {
        return MessageResponse.builder()
                .role(message.getRole())
                .content(message.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
