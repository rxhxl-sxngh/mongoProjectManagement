package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProjectView extends JFrame {
    private final JTextField projectNameField;
    private final JTextField leaderIdField;
    private final JTextField totalChargeField;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> projectComboBox;
    private final MongoDataAdapter mongoDataAdapter = new MongoDataAdapter();

    public ProjectView() throws SQLException {
        setTitle("Project View");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add new project logic here
                String projectName = projectNameField.getText();
                int leaderId = Integer.parseInt(leaderIdField.getText());
                double totalCharge = 0.0;
                Project project = new Project(0, projectName, leaderId, totalCharge);
                mongoDataAdapter.addProject(project);
                refreshProjectList();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the project ID from the user
                int projectId = Integer.parseInt(JOptionPane.showInputDialog("Enter Project ID:"));
                try {
                    // Retrieve the project from the database
                    Project project = mongoDataAdapter.getProjectByID(projectId);
                    if (project != null) {
                        // Update project name or leader ID based on user input
                        String newName = JOptionPane.showInputDialog("Enter New Project Name (Leave blank to keep current name):");
                        if (!newName.isEmpty()) {
                            project.setProjectName(newName);
                        }
                        String newLeaderId = JOptionPane.showInputDialog("Enter New Leader ID (Leave blank to keep current leader):");
                        if (!newLeaderId.isEmpty()) {
                            int leaderId = Integer.parseInt(newLeaderId);
                            project.setLeaderID(leaderId);
                        }
                        // Update the project in the database
                        mongoDataAdapter.updateProject(project);
                        // Refresh the project list and table
                        refreshProjectList();
                        refreshTable(project);
                    } else {
                        JOptionPane.showMessageDialog(ProjectView.this, "Project not found.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add employee to project logic here
                int selectedIndex = projectComboBox.getSelectedIndex();
                if (selectedIndex != -1) {
                    // Get the selected project from the list of projects
                    Project selectedProject = mongoDataAdapter.getAllProjects().get(selectedIndex);
                    int projectId = selectedProject.getProjectID(); // Get the ProjectID of the selected project
                    int employeeId = Integer.parseInt(JOptionPane.showInputDialog("Enter Employee ID:"));
                    double hoursBilled = Double.parseDouble(JOptionPane.showInputDialog("Enter Hours Billed:"));
                    try {
                        mongoDataAdapter.addOrUpdateProjectAssignment(projectId, employeeId, hoursBilled);
                        refreshProjectList();
                        refreshTable(mongoDataAdapter.getProjectByID(projectId));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(ProjectView.this, "Please select a project first.");
                }
            }
        });

        // Action Listener for projectComboBox
        projectComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected project index
                int selectedIndex = projectComboBox.getSelectedIndex();
                if (selectedIndex != -1) {
                    try {
                        // Get the selected project from the list of projects
                        Project selectedProject = mongoDataAdapter.getAllProjects().get(selectedIndex);
                        // Fill the fields with the selected project information
                        projectNameField.setText(selectedProject.getProjectName());
                        leaderIdField.setText(String.valueOf(selectedProject.getLeaderID()));
                        totalChargeField.setText(String.valueOf(selectedProject.getTotalChargeForProject()));
                        // Refresh the table with employee details for the selected project
                        refreshTable(selectedProject);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Refresh project list and table
        refreshProjectList();
        if (projectComboBox.getItemCount() > 0) {
            projectComboBox.setSelectedIndex(0);
            refreshTable(mongoDataAdapter.getProjectByID(0));
        }
    }

    // Method to refresh the project list in the combo box
    private void refreshProjectList() {
        projectComboBox.removeAllItems();
        List<Project> projects = null;
        projects = mongoDataAdapter.getAllProjects();
        for (Project project : projects) {
            projectComboBox.addItem(project.getProjectName());
        }
    }

    // Method to refresh the table with employee details for the selected project
    private void refreshTable(Project project) throws SQLException {
        tableModel.setRowCount(0); // Clear existing rows
        if (project != null) { // Check if project is not null
            for (Map.Entry<Integer, Double> entry : project.getEmployeeHoursBilled().entrySet()) {
                int employeeId = entry.getKey();
                double hoursBilled = entry.getValue();
                double wages = hoursBilled * mongoDataAdapter.getHourlyRateByEmployeeID(employeeId);
                tableModel.addRow(new Object[]{employeeId, hoursBilled, wages});
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ProjectView().setVisible(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

