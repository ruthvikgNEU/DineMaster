package edu.neu.csye6200.data;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import edu.neu.csye6200.model.Employee;
import edu.neu.csye6200.db.Db;
import edu.neu.csye6200.db.IDb;
import org.bson.Document;

public class EmployeeDao implements IDb {
    private MongoCollection<Document> collection;

    public EmployeeDao() {
        Db db = Db.getInstance();
        this.collection = db.getDatabase().getCollection("employees");
    }

    public void insertEmployee(Employee employee) {
        Document filter = new Document("employeeId", employee.getId());
        if (collection.countDocuments(filter) == 0) {
            Document doc = new Document("employeeId", employee.getId())
                    .append("age", employee.getAge())
                    // .append("role", employee.getRole())
                    .append("intime", employee.getInTime())
                    .append("outtime", employee.getOutTime());
            collection.insertOne(doc);
        } else {
            System.out.println("Employee " + employee.getId() + " already exists. Skipping insertion.");
        }
    }

    @Override
    public void insertDocument(Document document) {
        Document filter = new Document("employeeId", document.get("employeeId"));
        if (collection.countDocuments(filter) == 0) {
            collection.insertOne(document);
        } else {
            System.out.println("employeeId " + document.get("employeeId") + " already exists. Skipping insertion.");
        }
    }

    @Override
    public FindIterable<Document> findDocuments(String employeeId) {
            Document filter = new Document("employeeId", employeeId);
            return collection.find(filter);
    }

    @Override
    public void updateDocument(String collectionName, Document filter, Document update) {
        collection.updateOne(filter, new Document("$set", update));

    }

    @Override
    public void deleteDocument(Document filter) {

    }

    public boolean doesEmployeeExist(String employeeId) {
        Document filter = new Document("employeeId", employeeId);
        return collection.countDocuments(filter) > 0;
    }

}
