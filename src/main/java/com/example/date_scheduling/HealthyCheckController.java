package com.example.date_scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin
public class HealthyCheckController {
    @GetMapping("/")
    public String check(){
        log.info("server is running....");
        return "server is running....";
    }

    @GetMapping("/hello123")
    public String check123(){
        log.info("hello123....");
        return "안녕하세요";
    }
}
