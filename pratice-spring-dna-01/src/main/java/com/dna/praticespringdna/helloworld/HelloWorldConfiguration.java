package com.dna.praticespringdna.helloworld;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

record Person(String name, int age, Address address) {
}

record Address(String firstLine, String city) {
}

@Configuration
public class HelloWorldConfiguration {
    
    @Bean
    public String name() {
        return "Ranga";
    }
    
    @Bean
    public int age() {
        return 24;
    }
    
    @Bean
    public Person person() {
        return new Person("Ravi", 20, new Address("Main Street", "Utrecht"));
    }
    
    @Bean
    public Person person2MethodCall() {
        return new Person(name(), age(), address()); // name, age
    }
    
    @Bean
    public Person person3Parameters(String name, int age, Address address3) { // name, age, address2
        return new Person(name, age, address3); // name, age
    }
    
    @Bean
    @Primary
    /*
    - No qualifying bean of type (package name).Address
    - available : expected single matching bean but found 2 - address2, address3
    */
    public Person person4Parameters(String name, int age, Address address) { // name, age, address2
        return new Person(name, age, address); // name, age
    }
    
    @Bean
    public Person person5Qualifier(String name, int age, @Qualifier("address3qualifier") Address address) { // name, age, address2
        return new Person(name, age, address); // name, age
    }
    
    @Bean(name = "address2")
    @Primary
    public Address address() {
        return new Address("Baker Street", "London");
    }
    
    
    @Bean(name = "address3")
    @Qualifier("address3qualifier")
    public Address address3() {
        return new Address("Baker Street", "London");
    }
}
