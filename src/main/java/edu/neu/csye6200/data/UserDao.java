package edu.neu.csye6200.data;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.neu.csye6200.db.Db;
import edu.neu.csye6200.db.IDb;
import org.bson.Document;

public class UserDao implements IDb {
    private MongoCollection<Document> collection;

    public UserDao() {
        Db db = Db.getInstance();
        this.collection = db.getDatabase().getCollection("users");
    }

    @Override
    public void insertDocument(Document document) {
        Document filter = new Document("username", document.get("username"));
        if (collection.countDocuments(filter) == 0) {
            collection.insertOne(document);
        } else {
            System.out.println("User " + document.get("username") + " already exists. Skipping insertion.");
        }
    }

    @Override
    public FindIterable<Document> findDocuments(String username) {
        Document filter = new Document("username", username);
        return collection.find(filter);
    }

    @Override
    public void updateDocument(String collectionName, Document filter, Document update) {

    }

    @Override
    public void deleteDocument(Document filter) {

    }
}