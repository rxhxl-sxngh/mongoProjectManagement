package org.example;

import java.util.List;
import java.util.Map;
public interface DataAccess {
//    void addJobClass(Job job);
    void addEmployee(Employee employee);
//    Job getJobClassByID(int jobClassID);
    Employee getEmployeeByID(int employeeID);
//    List<Job> getAllJobClasses();
    List<Employee> getAllEmployees();
//    void updateJobClass(Job job);
    void updateEmployee(Employee employee);
//    void deleteJobClass(int jobClassID);
    void deleteEmployee(int employeeID);
//    void addProject(Project project);
//    Project getProjectByID(int projectID);
//    List<Project> getAllProjects();
//    void addOrUpdateProjectAssignment(int projectId, int employeeId, double hoursBilled);
}

