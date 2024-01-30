package com.dna.praticespringdna.test;

import java.io.Serializable;

class Pojo {
    private String text;
    private int number;
    
    public String toString() {
        return text + " : " + number;
    }
}

class JavaBean implements Serializable { // EJB (Enterprise Java Bean)
    private String text;
    private int number;
    
    // 1. public no-argument constructor
    public JavaBean() {
    
    }
    
    public String toString() {
        return text + " : " + number;
    }
    
    // 2. getters and setters
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
    
    // 3. implements Serializable
}

public class SpringBeanVSJavaBean {
    public static void main(String[] args) {
        Pojo pojo = new Pojo();
        
        System.out.println(pojo);
    }
}
