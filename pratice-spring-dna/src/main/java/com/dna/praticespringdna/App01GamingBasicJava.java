package com.dna.praticespringdna;

import com.dna.praticespringdna.game.GameRunner;
import com.dna.praticespringdna.game.PackManGame;

public class App01GamingBasicJava {
    public static void main(String[] args) {
        var game = new PackManGame(); // 1: Object Creation
        var gameRunner = new GameRunner(game); // 2: Object Creation + Wiring of Dependencies
        gameRunner.run();
    }
}
