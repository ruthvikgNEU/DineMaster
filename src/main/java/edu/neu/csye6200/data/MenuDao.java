package edu.neu.csye6200.data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.neu.csye6200.db.Db;
import edu.neu.csye6200.db.IDb;
import org.bson.Document;

public class MenuDao implements IDb {
    private MongoCollection<Document> collection;

    public MenuDao() {
        Db db = Db.getInstance();
        this.collection = db.getDatabase().getCollection("menuItems");
    }

    @Override
    public void insertDocument(Document document) {
        String menuItemName = (String) document.get("name");
        Document filter = new Document("name", menuItemName);

        // Check if a document with the given name already exists
        if (collection.find(filter).limit(1).first() == null) {
            // Document with the given name doesn't exist, so insert it
            collection.insertOne(document);
            System.out.println("Inserted menu item: " + menuItemName);
        } else {
            System.out.println("Menu item with name " + menuItemName + " already exists. Skipping insertion.");
        }
    }

    @Override
    public FindIterable<Document> findDocuments(String collectionName) {
        return collection.find();
    }

    @Override
    public void updateDocument(String collectionName, Document filter, Document update) {
        collection.updateOne(filter, new Document("$set", update));
    }

    @Override
    public void deleteDocument( Document filter) {
        collection.deleteOne(filter);

    }
}
