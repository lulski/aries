package com.lulski.aries.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@Profile("testcontainer")
public class TestcontainerMongoConfig extends AbstractReactiveMongoConfiguration {

    public TestcontainerMongoConfig() {
        MongoDBContainerUtil.getMongoDbContainer().start();
    }

    @Override
    protected String getDatabaseName() {
        return "aries_testcontainer";
    }

    @Override
    public MongoClient reactiveMongoClient() {
        ConnectionString connectionString = new ConnectionString(MongoDBContainerUtil.getMongoDbContainer()
                .getConnectionString());
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        return MongoClients.create(settings);
    }
}
