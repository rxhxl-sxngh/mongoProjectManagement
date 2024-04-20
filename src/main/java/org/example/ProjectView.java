package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProjectView extends JFrame {
    private final JTextField projectNameField;
    private final JTextField leaderIdField;
    private final JTextField totalChargeField;
    private final DefaultTableModel tableModel;
    private final JComboBox<Project> projectComboBox;
    private final MongoDataAdapter mongoDataAdapter = new MongoDataAdapter();
    private List<Project> cachedProjects;
    private Map<Integer, Double> employeeHourlyRates = new HashMap<>();
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public ProjectView() throws SQLException {
        setTitle("Project View");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                executorService.shutdownNow(); // Stop all threads when the window is closed
            }
        });
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        JLabel projectNameLabel = new JLabel("Project Name:");
        projectNameField = new JTextField();
        JLabel leaderIdLabel = new JLabel("Leader ID:");
        leaderIdField = new JTextField();
        JLabel totalChargeLabel = new JLabel("Total Charge:");
        totalChargeField = new JTextField();
        // Set totalChargeField non-editable
        totalChargeField.setEditable(false);
        JButton addButton = new JButton("Add Project");
        JButton editButton = new JButton("Edit Project");
        JButton addEmployeeButton = new JButton("Add Employee");
        projectComboBox = new JComboBox<>();

        inputPanel.add(new JLabel("Select Project:"));
        inputPanel.add(projectComboBox);
        inputPanel.add(projectNameLabel);
        inputPanel.add(projectNameField);
        inputPanel.add(leaderIdLabel);
        inputPanel.add(leaderIdField);
        inputPanel.add(totalChargeLabel);
        inputPanel.add(totalChargeField);
        inputPanel.add(addButton);
        inputPanel.add(editButton);

        add(inputPanel, BorderLayout.NORTH);

        // Employee Table
        tableModel = new DefaultTableModel(new Object[]{"Employee ID", "Hours Billed", "Wages"}, 0);
        JTable employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add Employee Button
        add(addEmployeeButton, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> {
            String projectName = projectNameField.getText();
            int leaderId = Integer.parseInt(leaderIdField.getText());
            Project project = new Project(0, projectName, leaderId, 0.0);
            mongoDataAdapter.addProject(project);
            // Add the new project to the cached list
            if (cachedProjects != null) {
                cachedProjects.add(project);
            }
            refreshProjectList();
        });

        editButton.addActionListener(e -> {
            int selectedIndex = projectComboBox.getSelectedIndex();
            if (selectedIndex != -1) {
                Project selectedProject = (Project) projectComboBox.getSelectedItem();
                // Update project name or leader ID based on user input
                String newName = JOptionPane.showInputDialog("Enter New Project Name (Leave blank to keep current name):");
                if (!newName.isEmpty()) {
                    selectedProject.setProjectName(newName);
                }
                String newLeaderId = JOptionPane.showInputDialog("Enter New Leader ID (Leave blank to keep current leader):");
                if (!newLeaderId.isEmpty()) {
                    int leaderId = Integer.parseInt(newLeaderId);
                    selectedProject.setLeaderID(leaderId);
                }
                mongoDataAdapter.updateProject(selectedProject);
                refreshProjectList();
                refreshTable(selectedProject);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.");
            }
        });

        addEmployeeButton.addActionListener(e -> {
            Project selectedProject = (Project) projectComboBox.getSelectedItem();
            if (selectedProject != null) {
                int employeeId = Integer.parseInt(JOptionPane.showInputDialog("Enter Employee ID:"));
                double hoursBilled = Double.parseDouble(JOptionPane.showInputDialog("Enter Hours Billed:"));
                mongoDataAdapter.addOrUpdateProjectAssignment(selectedProject.getProjectID(), employeeId, hoursBilled);
                refreshTable(selectedProject);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a project first.");
            }
        });

        // Action Listener for projectComboBox
        projectComboBox.addActionListener(e -> {
            Project selectedProject = (Project) projectComboBox.getSelectedItem();
            if (selectedProject != null) {
                projectNameField.setText(selectedProject.getProjectName());
                leaderIdField.setText(String.valueOf(selectedProject.getLeaderID()));
                totalChargeField.setText(String.valueOf(selectedProject.getTotalChargeForProject()));
                // Fetch employee data and hourly rates in a background thread
                executorService.submit(() -> {
                    refreshEmployeeHourlyRates(selectedProject);
                    refreshTable(selectedProject);
                });
            }
        });

        // Refresh project list and table
        refreshProjectList();
    }

    // Method to refresh the project list in the combo box
    private void refreshProjectList() {
        projectComboBox.removeAllItems();
        // Fetch project data in a background thread
        executorService.submit(() -> {
            if (cachedProjects == null) {
                cachedProjects = mongoDataAdapter.getAllProjects();
            }
            for (Project project : cachedProjects) {
                projectComboBox.addItem(project);
            }
        });
    }

    // Method to refresh the table with employee details for the selected project
    private void refreshTable(Project project) {
        tableModel.setRowCount(0); // Clear existing rows
        if (project != null) { // Check if project is not null
            for (Map.Entry<Integer, Double> entry : project.getEmployeeHoursBilled().entrySet()) {
                int employeeId = entry.getKey();
                double hoursBilled = entry.getValue();
                double wages = hoursBilled * getEmployeeHourlyRate(employeeId);
                tableModel.addRow(new Object[]{employeeId, hoursBilled, wages});
            }
        }
    }

    // Method to refresh the employee hourly rates for the selected project
    private void refreshEmployeeHourlyRates(Project project) {
        employeeHourlyRates.clear(); // Clear existing rates
        if (project != null) { // Check if project is not null
            for (int employeeId : project.getEmployeeHoursBilled().keySet()) {
                double hourlyRate = mongoDataAdapter.getHourlyRateByEmployeeID(employeeId);
                employeeHourlyRates.put(employeeId, hourlyRate);
            }
        }
    }

    // Method to get the hourly rate for a given employee ID from the cached data
    private double getEmployeeHourlyRate(int employeeId) {
        return employeeHourlyRates.getOrDefault(employeeId, 0.0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ProjectView().setVisible(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

