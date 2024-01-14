package com.dna.springboot.practicespringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/*
    http://localhost:8080/courses
    Course: id, name, author
*/

@RestController
public class CurrencyConfigurationController {
    @Autowired
    private CurrencyServiceConfiguration configuration;
    
    @RequestMapping("/currency-configuration")
    public CurrencyServiceConfiguration retrieveAllCourse() {
        return configuration;
    }
}
