package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MongoDataAdapter implements DataAccess {
    private static final String DB_URL = "mongodb+srv://rahul_2003:test123@cluster0.zr0gdzt.mongodb.net";
    private static final String DB_NAME = "projectManagement";

    // Method to get MongoDB database instance
    private static MongoDatabase getMongoDatabase() {
        ConnectionString connectionString = new ConnectionString(DB_URL);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase(DB_NAME);
    }

    // Method to add an employee to the MongoDB collection
    public void addEmployee(Employee employee) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("employee");
            Document doc = new Document("ID", employee.getEmployeeID())
                    .append("Name", employee.getEmployeeName())
                    .append("JobClassID", employee.getJobClassID());
            collection.insertOne(doc);
            System.out.println("Employee added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to add Employee.");
        }
    }

    // Method to get an employee by ID from the MongoDB collection
    public Employee getEmployeeByID(int employeeID) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("employee");
            Document query = new Document("ID", employeeID);
            Document result = collection.find(query).first();
            if (result != null) {
                return new Employee(result.getInteger("ID"),
                        result.getString("Name"),
                        result.getInteger("JobClassID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get all employees from the MongoDB collection
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("employee");
            for (Document doc : collection.find()) {
                employees.add(new Employee(doc.getInteger("ID"),
                        doc.getString("Name"),
                        doc.getInteger("JobClassID")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public void updateEmployee(Employee employee) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("employee");
            Document filter = new Document("ID", employee.getEmployeeID());
            Document update = new Document("$set", new Document("Name", employee.getEmployeeName())
                    .append("JobClassID", employee.getJobClassID()));
            collection.updateOne(filter, update);
            System.out.println("Employee updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to update Employee.");
        }
    }

    public void deleteEmployee(int employeeID) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("employee");
            Document query = new Document("ID", employeeID);
            collection.deleteOne(query);
            System.out.println("Employee deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to delete Employee.");
        }
    }
}
