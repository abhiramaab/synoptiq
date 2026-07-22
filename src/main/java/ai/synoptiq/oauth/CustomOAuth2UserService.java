package ai.synoptiq.oauth;

import ai.synoptiq.common.constants.Provider;
import ai.synoptiq.common.constants.Role;
import ai.synoptiq.user.entity.User;
import ai.synoptiq.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

@PostConstruct
public void init() {
    System.out.println(">>> CustomOAuth2UserService bean created");
}


@Override
public OAuth2User loadUser(OAuth2UserRequest request)
        throws OAuth2AuthenticationException {

    throw new RuntimeException("CUSTOM OAUTH SERVICE CALLED");
}
}
