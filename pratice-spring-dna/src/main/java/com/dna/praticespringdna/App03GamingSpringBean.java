package com.dna.praticespringdna;

import com.dna.praticespringdna.game.GamingConsole;
import com.dna.praticespringdna.game.PackManGame;
import com.dna.praticespringdna.helloworld.HelloWorldConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App03GamingSpringBean {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(GamingSpringBeanConfiguration.class)){
            context.getBean(GamingConsole.class).up();
            context.getBean(GameRunner.class).run();
        }
    }
}
