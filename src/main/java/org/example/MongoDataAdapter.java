package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
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

    // Method to update employees in the MongoDB collection
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

    // Method to delete employees from the MongoDB collection
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

    // Method to add a jobclass to the MongoDB collection
    public void addJobClass(Job job) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("jobclass");
            Document doc = new Document("JobClassID", job.getJobClassID())
                    .append("JobClassName", job.getJobTitle())
                    .append("HourlyRate", job.getHourlyWage());
            collection.insertOne(doc);
            System.out.println("Job Class added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to add Job Class.");
        }
    }

    // Method to get a jobclass by ID from the MongoDB collection
    public Job getJobClassByID(int jobClassID) {
        Job job = null;
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("jobclass");
            Document query = new Document("JobClassID", jobClassID);
            Document result = collection.find(query).first();
            if (result != null) {
                job = new Job(result.getInteger("JobClassID"),
                        result.getString("JobClassName"),
                        result.getDouble("HourlyRate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return job;
    }

    // Method to get all job classes from the MongoDB collection
    public List<Job> getAllJobClasses() {
        List<Job> jobList = new ArrayList<>();
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("jobclass");
            for (Document doc : collection.find()) {
                int jobClassID = doc.getInteger("JobClassID");
                String jobTitle = doc.getString("JobClassName");
                Object hourlyRateObj = doc.get("HourlyRate");
                double hourlyWage = 0.0;
                if (hourlyRateObj instanceof Double) {
                    hourlyWage = (double) hourlyRateObj;
                } else {
                    int hourlyWageInt = (int) hourlyRateObj;
                    hourlyWage = (double) hourlyWageInt;
                }
                Job job = new Job(jobClassID, jobTitle, hourlyWage);
                jobList.add(job);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobList;
    }

    // Method to update a jobclass in the MongoDB collection
    public void updateJobClass(Job job) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("jobclass");
            Document filter = new Document("JobClassID", job.getJobClassID());
            Document update = new Document("$set", new Document("JobClassName", job.getJobTitle())
                    .append("HourlyRate", job.getHourlyWage()));
            UpdateResult result = collection.updateOne(filter, update);
            if (result.getModifiedCount() == 1) {
                System.out.println("Job Class updated successfully.");
            } else {
                System.out.println("Job Class not found or failed to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to update Job Class.");
        }
    }

    // Method to delete a jobclass in the MongoDB Database
    public void deleteJobClass(int jobClassID) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("jobclass");
            Document query = new Document("JobClassID", jobClassID);
            DeleteResult result = collection.deleteOne(query);
            if (result.getDeletedCount() == 1) {
                System.out.println("Job Class deleted successfully.");
            } else {
                System.out.println("Job Class not found or failed to delete.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
