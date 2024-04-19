package org.example;

public class Employee {
    private int employeeID;
    private String employeeName;
    private int jobClassID;

    public Employee(int employeeID, String employeeName, int jobClassID) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.jobClassID = jobClassID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getJobClassID() {
        return jobClassID;
    }

    public void setJobClassID(int jobClassID) {
        this.jobClassID = jobClassID;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID=" + employeeID +
                ", employeeName='" + employeeName + '\'' +
                ", jobClassID=" + jobClassID +
                '}';
    }
}
