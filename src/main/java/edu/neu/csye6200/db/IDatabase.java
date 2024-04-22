package edu.neu.csye6200.db;

import com.mongodb.client.MongoDatabase;

public interface IDatabase {
    MongoDatabase getDatabase();
}
