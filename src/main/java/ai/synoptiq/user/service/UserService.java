package ai.synoptiq.user.service;

import ai.synoptiq.user.dto.UpdateProfileRequest;
import ai.synoptiq.user.dto.UserProfileResponse;

public interface UserService {

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateCurrentUserProfile(UpdateProfileRequest request);
}