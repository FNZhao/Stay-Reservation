package com.zhaofn.staybooking.config;


import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;


@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.address}")
    private String elasticsearchAddress;


    @Value("${elasticsearch.username}")
    private String elasticsearchUsername;


    @Value("${elasticsearch.password}")
    private String elasticsearchPassword;

    //另一种方法,跟final相比既保证了安全性又减小了scope
//    @Bean
//    public ElasticsearchConfig(
//            @Value("${elasticsearch.address}")
//            String elasticsearchAddress,
//            @Value("${elasticsearch.username}")
//            String elasticsearchUsername,
//            @Value("${elasticsearch.password}")
//            String elasticsearchPassword
//    ) {
//
//    }


    @Bean
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(elasticsearchAddress)
                .withBasicAuth(elasticsearchUsername, elasticsearchPassword)
                .build();


        return RestClients.create(clientConfiguration).rest();
    }
}
