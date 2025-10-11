package com.lulski.aries.config;

import org.testcontainers.containers.MongoDBContainer;

/**
 * Util class to setup testcontainer mongodb
 */
public class MongoDbContainerUtil {

    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:4.0.10");

    public static MongoDBContainer getMongoDbContainer() {
        return MONGO_DB_CONTAINER;
    }
}
