package com.dna.praticespringdna;

import com.dna.praticespringdna.game.GamingConsole;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App03GamingSpringBean {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(GamingSpringBeanConfiguration.class)) {
            context.getBean(GamingConsole.class).up();
            context.getBean(GameRunner.class).run();
        }
    }
}
