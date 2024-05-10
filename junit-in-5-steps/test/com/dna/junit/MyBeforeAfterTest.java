package com.dna.junit;

import org.junit.jupiter.api.*;

class MyBeforeAfterTest {
    @BeforeEach
    void beforeEach() {
        System.out.println("Before Each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After Each");
    }
    
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before All");
    }
    
    @AfterAll
    static void afterAll() {
        System.out.println("After All");
    }
    
    @Test
    void test1() {
        System.out.println("test1");
    }
    
    @Test
    void test2() {
        System.out.println("test2");
    }
    
    @Test
    void test3() {
        System.out.println("test3");
    }
}