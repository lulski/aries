package com.lulski.aries.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.lang.NonNull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Config for mongodb
 */
@Configuration
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
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
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        // super.configureClientSettings(builder);
        builder.applyConnectionString(new ConnectionString(mongoProperties.getUri()));
        // .readConcern(ReadConcern.SNAPSHOT)
        // .writeConcern(WriteConcern.MAJORITY);
    }
}
