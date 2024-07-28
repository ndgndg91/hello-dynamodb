package com.ndgndg91.hellodynamodb.global.config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Lazy


@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.ndgndg91.hellodynamodb"])
class DynamoDBConfig {
    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        val amazonDynamoDB = AmazonDynamoDBClient.builder()
            .withCredentials(DefaultAWSCredentialsProviderChain())
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "ap-northeast-2"))
            .build()

        return amazonDynamoDB
    }
    @Bean
    fun dynamoDBMapper(): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDB())
    }
}