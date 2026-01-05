package com.ingnum.rentalservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RentalController {

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    @GetMapping("/customer/{name}")
    public String bonjour(@PathVariable String name) {

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(customerServiceUrl, String.class);
    }
}
