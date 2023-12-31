package com.dna.praticespringdna.game;

public class SuperContraGame implements GamingConsole {
    public SuperContraGame() {
    }
    
    public void up() {
        System.out.println("Jump");
    }
    
    public void down() {
        System.out.println("Sit down");
    }
    
    public void left() {
        System.out.println("Go back");
    }
    
    public void right() {
        System.out.println("Shoot a bullet");
    }
}
