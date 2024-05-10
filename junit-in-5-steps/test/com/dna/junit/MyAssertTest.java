package com.dna.junit;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyAssertTest {
    
    List<String> todos = Arrays.asList("AWS", "Azure", "DevOps");
    
    @Test
    void testAsserts() {
        boolean test = todos.contains("AWS");//Result
        boolean test2 = todos.contains("GCP");//Result
        int[] test3 = {1, 2};
        
        //assertEquals(true, test);
        assertTrue(test);
        assertFalse(test2);
        //assertNull, assertNotNull
        assertArrayEquals(new int[] {1, 2}, test3);
        assertEquals(3, todos.size());
    }
}