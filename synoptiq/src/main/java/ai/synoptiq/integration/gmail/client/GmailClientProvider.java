package ai.synoptiq.integration.gmail.client;

import ai.synoptiq.user.entity.User;
import com.google.api.services.gmail.Gmail;

public interface GmailClientProvider {

    Gmail getClient(User user) throws Exception;

}