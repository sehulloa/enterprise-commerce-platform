package com.company.platform.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheckController {

    @GetMapping("/test-log")
    public String testLog() {
        log.info("Test log endpoint invoked");
        return "ok";
    }

}
