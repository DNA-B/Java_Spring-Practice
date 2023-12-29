package com.dna.praticespringdna;

import com.dna.praticespringdna.game.GamingConsole;
import com.dna.praticespringdna.game.MarioGame;
import com.dna.praticespringdna.game.PackManGame;
import com.dna.praticespringdna.game.SuperContraGame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

record GameRunner(GamingConsole game) {
    public void run() {
        System.out.println("Running game: " + this.game);
        game.up();
        game.down();
        game.left();
        game.right();
    }
}

@Configuration
public class GamingSpringBeanConfiguration {
    @Bean()
    public GamingConsole game() {
        var game = new PackManGame();
        return game;
    }
    
    @Bean()
    public GameRunner gameRunner(GamingConsole game){
        var gameRunner = new GameRunner(game);
        return gameRunner;
    }
}
