package org.example;

import java.util.HashMap;
import java.util.Map;

public class Project {
    private int projectID;
    private String projectName;
    private int leaderID;
    private double totalChargeForProject;
    private Map<Integer, Double> employeeHoursBilled; // Map to store employee ID and hours billed

    public Project(int projectID, String projectName, int leaderID, double totalChargeForProject) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.leaderID = leaderID;
        this.totalChargeForProject = totalChargeForProject;
        this.employeeHoursBilled = new HashMap<>();
    }

    // Getters and Setters
    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getLeaderID() {
        return leaderID;
    }

    public void setLeaderID(int leaderID) {
        this.leaderID = leaderID;
    }

    public double getTotalChargeForProject() {
        return totalChargeForProject;
    }

    public void setTotalChargeForProject(double totalChargeForProject) {
        this.totalChargeForProject = totalChargeForProject;
    }

    public Map<Integer, Double> getEmployeeHoursBilled() {
        return employeeHoursBilled;
    }

    public void setEmployeeHoursBilled(Map<Integer, Double> employeeHoursBilled) {
        this.employeeHoursBilled = employeeHoursBilled;
    }

    // Method to add or update hours billed for an employee
    public void addOrUpdateEmployeeHoursBilled(int employeeID, double hoursBilled) {
        employeeHoursBilled.put(employeeID, hoursBilled);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", projectName='" + projectName + '}';
    }
}
