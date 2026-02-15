package com.lulski.aries.config;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.mongodb.autoconfigure.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

/**
 * Config for mongodb
 */
@Configuration
@Profile("!mock")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final MongoProperties mongoProperties;

    public MongoConfig(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }

    @Override
    @NonNull
    protected String getDatabaseName() {
        return mongoProperties.getDatabase();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.@NonNull Builder builder) {
        builder.applyConnectionString(new ConnectionString(mongoProperties.getUri()));
    }
}
