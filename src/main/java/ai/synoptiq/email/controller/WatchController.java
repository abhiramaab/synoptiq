package ai.synoptiq.email.controller;

import ai.synoptiq.email.service.WatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/watch")
@RequiredArgsConstructor
public class WatchController {

    private final WatchService watchService;

    @PostMapping("/{emailId}")
    public String watch(@PathVariable Long emailId) {
        watchService.watchEmail(emailId);
        return "Thread added to watch list.";
    }
}
