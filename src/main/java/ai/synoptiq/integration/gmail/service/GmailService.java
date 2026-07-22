package ai.synoptiq.integration.gmail.service;

import ai.synoptiq.integration.gmail.dto.GmailMessageDTO;
import ai.synoptiq.user.entity.User;

import java.util.List;

public interface GmailService {

    List<GmailMessageDTO> getEmails(User user) throws Exception;

}