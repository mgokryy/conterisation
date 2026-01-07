package com.ingnum.rentalservice.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RentalController {

    private final Environment environment;

    public RentalController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/customer/{name}")
    public String bonjour(@PathVariable String name) {
        String url = environment.getProperty("customer.service.url");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
