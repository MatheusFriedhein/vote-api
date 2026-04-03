package com.example.votacao;

import com.example.votacao.config.properties.UserInfoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(UserInfoProperties.class)
public class AssemblyVotingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssemblyVotingApiApplication.class, args);
    }
}
