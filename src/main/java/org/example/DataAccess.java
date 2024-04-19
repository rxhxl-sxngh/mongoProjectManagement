package org.example;

import java.util.List;
import java.util.Map;
public interface DataAccess {
    void addEmployee(Employee employee);
    Employee getEmployeeByID(int employeeID);
    List<Employee> getAllEmployees();
    void updateEmployee(Employee employee);
    void deleteEmployee(int employeeID);


    void addJobClass(Job job);
    Job getJobClassByID(int jobClassID);
    List<Job> getAllJobClasses();
    void updateJobClass(Job job);
    void deleteJobClass(int jobClassID);

//    void addProject(Project project);
//    Project getProjectByID(int projectID);
//    List<Project> getAllProjects();
//    void addOrUpdateProjectAssignment(int projectId, int employeeId, double hoursBilled);
}

