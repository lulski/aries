package com.lulski.aries.config;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.lang.NonNull;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final MongoProperties mongoProperties;

    @Override
    @NonNull
    protected String getDatabaseName() {
       return mongoProperties.getDatabase();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        //super.configureClientSettings(builder);
        builder.applyConnectionString(new ConnectionString(mongoProperties.getUri()));
//                .readConcern(ReadConcern.SNAPSHOT)
//                .writeConcern(WriteConcern.MAJORITY);
    }

    public MongoConfig(MongoProperties mongoProperties) {
        this.mongoProperties = mongoProperties;
    }
}
