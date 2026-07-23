package ai.synoptiq.user.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String username;
    private String profilePicture;
}