package com.example.aibouauth.core.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;




@Configuration
public class KafkaPurchaseTopicConfig {
    @Bean
    public NewTopic purchaseTopic(){
        return TopicBuilder
                .name("purchase-topic")
                .build();
    }
}
