package ai.synoptiq.user.controller;

import ai.synoptiq.user.dto.UpdateProfileRequest;
import ai.synoptiq.user.dto.UserProfileResponse;
import ai.synoptiq.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponse getCurrentUser() {
        return userService.getCurrentUserProfile();
    }

    @PatchMapping("/me")
    public UserProfileResponse updateCurrentUser(@RequestBody UpdateProfileRequest request) {
        return userService.updateCurrentUserProfile(request);
    }
}