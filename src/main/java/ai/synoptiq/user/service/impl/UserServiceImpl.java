package ai.synoptiq.user.service.impl;

import ai.synoptiq.chat.enums.MessageRole;
import ai.synoptiq.chat.repository.MessageRepository;
import ai.synoptiq.integration.gmail.repository.GmailTokenRepository;
import ai.synoptiq.user.dto.UpdateProfileRequest;
import ai.synoptiq.user.dto.UserProfileResponse;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import ai.synoptiq.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final GmailTokenRepository gmailTokenRepository;
    private final MessageRepository messageRepository;

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        User user = getCurrentUser();
        return toProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateCurrentUserProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername().trim());
        }

        if (request.getProfilePicture() != null) {
            // Allow clearing it out with an empty string, but not blank whitespace
            String pic = request.getProfilePicture().trim();
            user.setProfilePicture(pic.isEmpty() ? null : pic);
        }

        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.saveAndFlush(user);

        return toProfileResponse(saved);
    }

    private UserProfileResponse toProfileResponse(User user) {

        List<String> connected = new ArrayList<>();

        if (gmailTokenRepository.findByUser(user).isPresent()) {
            connected.add("Gmail");
        }

        Long aiMessages30d = messageRepository.countByRoleAndConversation_UserAndCreatedAtAfter(
                MessageRole.ASSISTANT,
                user,
                LocalDateTime.now().minusDays(30)
        );

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .provider(user.getProvider())
                .role(user.getRole())
                .memberSince(user.getCreatedAt())
                .connectedIntegrations(connected)
                .aiMessagesLast30Days(aiMessages30d)
                .build();
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
}