package com.nibado.example.geneticgraphcolor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public GraphFactory getMarkovChain() {
        return new GraphFactory();
    }
}
