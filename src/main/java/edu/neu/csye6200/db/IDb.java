package edu.neu.csye6200.db;
import org.bson.Document;
import com.mongodb.client.FindIterable;


public interface IDb {
    // Create operation
    void insertDocument(Document document);

    // Read operation
    FindIterable<Document> findDocuments(String collectionName);

    // Update operation
    void updateDocument(String collectionName, Document filter, Document update);

    // Delete operation
    void deleteDocument(Document filter);
}
