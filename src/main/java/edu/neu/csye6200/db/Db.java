package edu.neu.csye6200.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


public class Db implements IDatabase{
    private static Db instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private Db() {
        // Connect to MongoDB
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("resturantdb");
        System.out.println("Connected to MongoDB");
    }

    public static Db getInstance() {
        if (instance == null) {
            instance = new Db();
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

}