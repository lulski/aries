package com.lulski.aries.config;

import com.lulski.aries.user.UserRepository;
import com.lulski.aries.user.UserService;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
public class TestMongoConfig extends AbstractReactiveMongoConfiguration {

    public TestMongoConfig() {
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
