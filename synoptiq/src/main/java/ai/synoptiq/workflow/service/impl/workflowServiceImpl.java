package ai.synoptiq.workflow.service.impl;

import ai.synoptiq.workflow.service.workflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class workflowServiceImpl implements workflowService {


    @Override
    public String postToX() {

        String content = "Hello from Synoptiq!";

        return "";
    }
}
