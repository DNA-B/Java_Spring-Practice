package com.dna.praticespringdna.game;

import org.springframework.stereotype.Component;

@Component
public class PackManGame implements GamingConsole {
    public PackManGame() {
    }
    
    public void up() {
        System.out.println("Go up");
    }
    
    public void down() {
        System.out.println("Go down");
    }
    
    public void left() {
        System.out.println("Go back");
    }
    
    public void right() {
        System.out.println("Go ahead");
    }
}
