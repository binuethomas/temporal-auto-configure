package com.example.temporalautoconfigure.controller;

import com.example.temporalautoconfigure.service.WorkerService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@CommonsLog
public class TestController {
    private WorkerService workflowService;

    public TestController(WorkerService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping(value = "/api/v1/test")
    public String test(@RequestBody @Valid final TestRequest request) {
        log.info("api/v1/lock called : temporal request"+ request);
        return workflowService.execute(request);
    }
}
