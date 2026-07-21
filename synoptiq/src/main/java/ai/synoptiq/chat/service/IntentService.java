package ai.synoptiq.chat.service;

import ai.synoptiq.chat.enums.ChatIntent;

public interface IntentService {

    ChatIntent detectIntent(String message);
}
