package org.example;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MongoDataAdapter implements DataAccess {
    private static final String DB_URL = "mongodb+srv://rahul_2003:test123@cluster0.zr0gdzt.mongodb.net";
    private static final String DB_NAME = "projectManagement";

    // Method to get MongoDB database instance
    private static MongoDatabase getMongoDatabase() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
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
                Object hourlyRateObj = result.get("HourlyRate");
                double hourlyWage = 0.0;
                if (hourlyRateObj instanceof Double) {
                    hourlyWage = (double) hourlyRateObj;
                } else {
                    int hourlyWageInt = (int) hourlyRateObj;
                    hourlyWage = (double) hourlyWageInt;
                }
                job = new Job(result.getInteger("JobClassID"),
                        result.getString("JobClassName"),
                        hourlyWage);
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

    // Method to add a project
    public void addProject(Project project) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("project");

            Document projectDoc = new Document("ProjectID", project.getProjectID())
                    .append("ProjectName", project.getProjectName())
                    .append("Leader", project.getLeaderID())
                    .append("TotalChargeForProject", project.getTotalChargeForProject())
                    .append("Assignments", new ArrayList<Document>());

            List<Document> assignments = new ArrayList<>();
            for (Map.Entry<Integer, Double> entry : project.getEmployeeHoursBilled().entrySet()) {
                Document assignmentDoc = new Document("EmployeeID", entry.getKey())
                        .append("HoursBilled", entry.getValue())
                        .append("TotalChargeFromEmployee", calculateTotalCharge(entry.getValue(), getHourlyRateByEmployeeID(entry.getKey())));
                assignments.add(assignmentDoc);
            }
            projectDoc.put("Assignments", assignments);

            collection.insertOne(projectDoc);
            System.out.println("Project added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to add Project.");
        }
    }

    // Helper methods to add a project
    private double calculateTotalCharge(double hoursBilled, double hourlyRate) {
        // Implement your calculation logic here
        return hoursBilled * hourlyRate;
    }

    public double getHourlyRateByEmployeeID(int employeeID) {
        double hourlyRate = 0.0;
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("employee");
            Document query = new Document("ID", employeeID);
            Document result = collection.find(query).first();
            if (result != null) {
                int jobClassID = result.getInteger("JobClassID");
                hourlyRate = getHourlyRateByJobClassID(jobClassID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hourlyRate;
    }

    private double getHourlyRateByJobClassID(int jobClassID) {
        double hourlyRate = 0.0;
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("jobclass");
            Document query = new Document("JobClassID", jobClassID);
            Document result = collection.find(query).first();
            if (result != null) {
                Object hourlyRateObj = result.get("HourlyRate");
                if (hourlyRateObj instanceof Double) {
                    hourlyRate = (double) hourlyRateObj;
                } else {
                    int hourlyRateInt = (int) hourlyRateObj;
                    hourlyRate = (double) hourlyRateInt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hourlyRate;
    }

    public Project getProjectByID(int projectID) {
        Project project = null;
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("project");
            Document query = new Document("ProjectID", projectID);
            Document result = collection.find(query).first();
            if (result != null) {
                Object totalChargeForProjectObj = result.get("TotalChargeForProject");
                double totalChargeForProject = 0.0;
                if (totalChargeForProjectObj instanceof Double) {
                    totalChargeForProject = (double) totalChargeForProjectObj;
                } else {
                    int totalCharge = (int) totalChargeForProjectObj;
                    totalChargeForProject = (double) totalCharge;
                }
                project = new Project(result.getInteger("ProjectID"),
                        result.getString("ProjectName"),
                        result.getInteger("Leader"),
                        totalChargeForProject);


                project.setEmployeeHoursBilled(getProjectAssignments(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return project;
    }

    private Map<Integer, Double> getProjectAssignments(Document projectDoc) {
        Map<Integer, Double> assignments = new HashMap<>();
        List<Document> employees = (List<Document>) projectDoc.get("Assignments");
        if (employees != null) {
            for (Document employee : employees) {
                int employeeID = employee.getInteger("EmployeeID");
                Object hoursBilledObj = employee.get("HoursBilled");
                double hoursBilled = 0.0;
                if (hoursBilledObj instanceof Double) {
                    hoursBilled = (double) hoursBilledObj;
                } else {
                    int hourlyWageInt = (int) hoursBilledObj;
                    hoursBilled = (double) hourlyWageInt;
                }
                assignments.put(employeeID, hoursBilled);
            }
        }
        return assignments;
    }

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("project");
            for (Document projectDoc : collection.find()) {
                int projectID = projectDoc.getInteger("ProjectID");
                String projectName = projectDoc.getString("ProjectName");
                int leaderID = projectDoc.getInteger("Leader");
                Object totalChargeForProjectObj = projectDoc.get("TotalChargeForProject");
                double totalChargeForProject = 0.0;
                if (totalChargeForProjectObj instanceof Double) {
                    totalChargeForProject = (double) totalChargeForProjectObj;
                } else {
                    int totalCharge = (int) totalChargeForProjectObj;
                    totalChargeForProject = (double) totalCharge;
                }
                Project project = new Project(projectID, projectName, leaderID, totalChargeForProject);
                project.setEmployeeHoursBilled(getProjectAssignments(projectDoc));
                projects.add(project);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    public void addOrUpdateProjectAssignment(int projectID, int employeeId, double hoursBilled) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("project");

            // Check if the project exists and has the employee assignment
            Document query = new Document("ProjectID", projectID);
            query.append("Assignments.EmployeeID", employeeId);
            FindIterable<Document> result = collection.find(query);

            if (result.iterator().hasNext()) {
                // If the project assignment exists, update the hours billed
                Document updateQuery = new Document("ProjectID", projectID);
                updateQuery.append("Assignments.EmployeeID", employeeId);
                Document updateFields = new Document("Assignments.$.HoursBilled", hoursBilled);
                collection.updateOne(updateQuery, new Document("$set", updateFields));
                updateTotalChargeForProject(collection, projectID);
                System.out.println("Project assignment updated successfully.");
            } else {
                // If the project assignment doesn't exist, insert a new record
                Document assignment = new Document("EmployeeID", employeeId)
                        .append("HoursBilled", hoursBilled)
                        .append("TotalChargeFromEmployee", calculateTotalCharge(hoursBilled, getHourlyRateByEmployeeID(employeeId)));
                Document updateFields = new Document("Assignments", assignment);
                collection.updateOne(new Document("ProjectID", projectID), new Document("$push", updateFields));
                updateTotalChargeForProject(collection, projectID);
                System.out.println("Project assignment added successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to add or update project assignment.");
        }
    }

    private void updateTotalChargeForProject(MongoCollection<Document> collection, int projectId) {
        try {
            Document query = new Document("ProjectID", projectId);
            List<Document> projectAssignments = (List<Document>) collection.find(query).first().get("Assignments");

            double totalCharge = 0;
            // MAY HAVE TO FIX THIS LATER
            for (Document assignment : projectAssignments) {
                totalCharge += (Double) assignment.get("TotalChargeFromEmployee");
            }

            Document updateFields = new Document("TotalChargeForProject", totalCharge);
            collection.updateOne(query, new Document("$set", updateFields));
            System.out.println("Total charge for project updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to update total charge for project.");
        }
    }

    public void updateProject(Project project) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("project");

            // Create a query to find the project by projectID
            Document query = new Document("ProjectID", project.getProjectID());

            // Create an update document with the new values
            Document updateFields = new Document("ProjectName", project.getProjectName())
                    .append("Leader", project.getLeaderID());

            // Perform the update operation
            UpdateResult result = collection.updateOne(query, new Document("$set", updateFields));

            // Check if the update was successful
            if (result.getModifiedCount() > 0) {
                System.out.println("Project updated successfully.");
            } else {
                System.out.println("Project not found or failed to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to update project.");
        }
    }

    public void deleteProject(int projectID) {
        try {
            MongoDatabase database = getMongoDatabase();
            MongoCollection<Document> collection = database.getCollection("project");

            // Create a query to find the project by projectID
            Document query = new Document("ProjectID", projectID);

            // Perform the delete operation
            DeleteResult result = collection.deleteOne(query);

            // Check if the delete was successful
            if (result.getDeletedCount() > 0) {
                System.out.println("Project deleted successfully.");
            } else {
                System.out.println("Project not found or failed to delete.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to delete project.");
        }
    }


}
