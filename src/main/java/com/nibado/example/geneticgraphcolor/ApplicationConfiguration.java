package com.nibado.example.geneticgraphcolor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public GraphFactory getGraphFactory() {
        return new GraphFactory();
    }

    @Bean
    public ColorFactory getColorFactory() {
        return new ColorFactory();
    }

    @Bean
    @Scope("singleton")
    public GraphSolver getGraphSolver() {
        return new GraphSolver();
    }
}
