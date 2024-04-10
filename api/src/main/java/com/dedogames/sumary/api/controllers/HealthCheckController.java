package com.dedogames.sumary.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dedogames.sumary.shared.observability.SimpleLogger;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private  SimpleLogger logger = new  SimpleLogger(HealthCheckController.class );

    @GetMapping("/status")
    public String welcome() {
        logger.info("Status ok");
        return "return Up and Running";
    }
}
