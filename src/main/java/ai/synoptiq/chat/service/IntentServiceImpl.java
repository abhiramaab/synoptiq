package ai.synoptiq.chat.service;

import ai.synoptiq.chat.enums.ChatIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IntentServiceImpl implements IntentService {

    @Override
    public ChatIntent detectIntent(String message) {

        String text = message.toLowerCase();

        if (text.contains("email")
                || text.contains("gmail")
                || text.contains("mail")
                || text.contains("inbox")) {
            return ChatIntent.EMAIL;
        }
        return ChatIntent.GENERAL;
    }
}
