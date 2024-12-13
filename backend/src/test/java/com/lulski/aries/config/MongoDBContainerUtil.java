package com.lulski.aries.config;

import org.testcontainers.containers.MongoDBContainer;

public class MongoDBContainerUtil {

    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:4.0.10");

    public static MongoDBContainer getMongoDbContainer() {
        return MONGO_DB_CONTAINER;
    }
}
