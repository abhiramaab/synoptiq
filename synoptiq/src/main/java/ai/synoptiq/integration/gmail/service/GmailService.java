package ai.synoptiq.integration.gmail.service;

import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import com.google.api.services.gmail.model.ListMessagesResponse;

import java.io.IOException;
import java.util.List;

public interface GmailService {


    List<GmailMessageDTO> getEmails() throws Exception;
}
