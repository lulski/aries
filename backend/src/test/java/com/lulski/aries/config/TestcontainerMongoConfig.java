package com.lulski.aries.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

/**
 * Testcontainer config
 */
@Configuration
@Profile("testcontainer")
public class TestcontainerMongoConfig extends AbstractReactiveMongoConfiguration {

    public TestcontainerMongoConfig() {
        MongoDbContainerUtil.getMongoDbContainer().start();
    }

    @Override
    protected String getDatabaseName() {
        return "aries_testcontainer";
    }

    @Override
    public MongoClient reactiveMongoClient() {
        ConnectionString connectionString =
            new ConnectionString(MongoDbContainerUtil.getMongoDbContainer().getConnectionString());
        MongoClientSettings settings =
            MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(settings);
    }
}
