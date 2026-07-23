package ai.synoptiq.user.dto;

import ai.synoptiq.common.constants.Provider;
import ai.synoptiq.common.constants.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String profilePicture;
    private Provider provider;
    private Role role;
    private LocalDateTime memberSince;

    // Real, derived stats (no fake numbers)
    private List<String> connectedIntegrations;
    private Long aiMessagesLast30Days;
}