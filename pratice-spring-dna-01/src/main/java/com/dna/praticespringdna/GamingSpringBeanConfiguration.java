package com.dna.praticespringdna;

import com.dna.praticespringdna.game.GamingConsole;
import com.dna.praticespringdna.game.PackManGame;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GamingSpringBeanConfiguration {
    @Bean()
    public GamingConsole game() {
        var game = new PackManGame();
        return game;
    }
    
    @Bean()
    public GameRunner gameRunner(GamingConsole game) {
        var gameRunner = new GameRunner(game);
        return gameRunner;
    }
}
