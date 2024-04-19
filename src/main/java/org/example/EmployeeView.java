package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeView extends JFrame {
    private JTextField employeeIDField;
    private JTextField employeeNameField;
    private JTextField jobClassIDField;
    private final JComboBox<String> operationComboBox;
    private final JPanel panel = new JPanel();
    private final MongoDataAdapter mongoDataAdapter = new MongoDataAdapter();

    public EmployeeView() {
        setTitle("Employee View");
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panel.setLayout(new GridLayout(4, 2));

        JLabel operationLabel = new JLabel("Select Operation:");
        String[] operations = {"Show Employees", "Add Employee", "Update Employee", "Delete Employee"};
        operationComboBox = new JComboBox<>(operations);
        panel.add(operationLabel);
        panel.add(operationComboBox);

        operationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOperation = (String) operationComboBox.getSelectedItem();
                assert selectedOperation != null;
                switch (selectedOperation) {
                    case "Show Employees":
                        showEmployees();
                        break;
                    case "Add Employee":
                        addEmployee();
                        break;
                    case "Update Employee":
                        updateEmployee();
                        break;
                    case "Delete Employee":
                        deleteEmployee();
                        break;
                }
            }
        });

        add(panel);
    }

    private void showEmployees() {
        panel.removeAll();
        setSize(600, 400);
        panel.setLayout(new BorderLayout());
        List<Employee> employeeList = mongoDataAdapter.getAllEmployees();

        String[] columnNames = {"Employee ID", "Employee Name", "Job Class ID"};
        Object[][] data = new Object[employeeList.size()][3];

        for (int i = 0; i < employeeList.size(); i++) {
            Employee employee = employeeList.get(i);
            data[i][0] = employee.getEmployeeID();
            data[i][1] = employee.getEmployeeName();
            data[i][2] = employee.getJobClassID();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = getBackButton();
        panel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void addEmployee() {
        panel.removeAll(); // Clear previous components
        panel.setLayout(new GridLayout(4, 2));

        JLabel employeeIDLabel = new JLabel("Employee ID:");
        employeeIDField = new JTextField();
        panel.add(employeeIDLabel);
        panel.add(employeeIDField);

        JLabel employeeNameLabel = new JLabel("Employee Name:");
        employeeNameField = new JTextField();
        panel.add(employeeNameLabel);
        panel.add(employeeNameField);

        JLabel jobClassIDLabel = new JLabel("Job Class ID:");
        jobClassIDField = new JTextField();
        panel.add(jobClassIDLabel);
        panel.add(jobClassIDField);

        JButton addButton = getAddButton();
        panel.add(addButton);

        JButton backButton = getBackButton();
        panel.add(backButton);

        revalidate(); // Refresh the layout
        repaint(); // Repaint the component
    }

    private JButton getAddButton() {
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add employee
                int employeeID = Integer.parseInt(employeeIDField.getText());
                String employeeName = employeeNameField.getText();
                int jobClassID = Integer.parseInt(jobClassIDField.getText());
                Employee employee = new Employee(employeeID, employeeName, jobClassID);
                mongoDataAdapter.addEmployee(employee);
                // Show operation selection view
                showOperationSelectionView();
            }
        });
        return addButton;
    }

    private void updateEmployee() {
        panel.removeAll(); // Clear previous components
        panel.setLayout(new GridLayout(4, 2));

        JLabel employeeIDLabel = new JLabel("Employee ID:");
        employeeIDField = new JTextField();
        panel.add(employeeIDLabel);
        panel.add(employeeIDField);

        JLabel employeeNameLabel = new JLabel("Employee Name:");
        employeeNameField = new JTextField();
        panel.add(employeeNameLabel);
        panel.add(employeeNameField);

        JLabel jobClassIDLabel = new JLabel("Job Class ID:");
        jobClassIDField = new JTextField();
        panel.add(jobClassIDLabel);
        panel.add(jobClassIDField);

        JButton updateButton = getUpdateButton();
        panel.add(updateButton);

        JButton backButton = getBackButton();
        panel.add(backButton);

        revalidate(); // Refresh the layout
        repaint(); // Repaint the component
    }

    private JButton getUpdateButton() {
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update employee
                int employeeID = Integer.parseInt(employeeIDField.getText());
                String employeeName = employeeNameField.getText();
                int jobClassID = Integer.parseInt(jobClassIDField.getText());
                Employee employee = new Employee(employeeID, employeeName, jobClassID);
                mongoDataAdapter.updateEmployee(employee);
                // Show operation selection view
                showOperationSelectionView();
            }
        });
        return updateButton;
    }

    private void deleteEmployee() {
        panel.removeAll(); // Clear previous components
        panel.setLayout(new GridLayout(2, 2));

        JLabel employeeIDLabel = new JLabel("Employee ID:");
        employeeIDField = new JTextField();
        panel.add(employeeIDLabel);
        panel.add(employeeIDField);

        JButton removeButton = getRemoveButton();
        panel.add(removeButton);

        JButton backButton = getBackButton();
        panel.add(backButton);

        revalidate(); // Refresh the layout
        repaint(); // Repaint the component
    }

    private JButton getRemoveButton() {
        JButton removeButton = new JButton("Delete");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove employee
                int employeeID = Integer.parseInt(employeeIDField.getText());
                mongoDataAdapter.deleteEmployee(employeeID);
                // Show operation selection view
                showOperationSelectionView();
            }
        });
        return removeButton;
    }

    private JButton getBackButton() {
        JButton backButton = new JButton("Go Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show operation selection view
                showOperationSelectionView();
            }
        });
        return backButton;
    }

    private void showOperationSelectionView() {
        panel.removeAll();
        setSize(300, 200);
        panel.setLayout(new GridLayout(4, 2));
        JLabel operationLabel = new JLabel("Select Operation:");
        panel.add(operationLabel);
        panel.add(operationComboBox);
        panel.revalidate();
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeView employeeView = new EmployeeView();
            employeeView.setVisible(true);
        });
    }
}
