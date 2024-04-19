package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class JobView extends JFrame {
    private JTextField jobClassIDField;
    private JTextField jobTitleField;
    private JTextField hourlyWageField;
    private final JComboBox<String> operationComboBox;
    private final JPanel panel = new JPanel();
    private final MongoDataAdapter mongoDataAdapter = new MongoDataAdapter();

    public JobView() {
        setTitle("Job View");
        setSize(300, 200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        panel.setLayout(new GridLayout(4, 2));

        JLabel operationLabel = new JLabel("Select Operation:");
        String[] operations = {"Show Job Classes", "Add Job Class", "Update Job Class", "Delete Job Class"};
        operationComboBox = new JComboBox<>(operations);
        panel.add(operationLabel);
        panel.add(operationComboBox);

        operationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOperation = (String) operationComboBox.getSelectedItem();
                assert selectedOperation != null;
                switch (selectedOperation) {
                    case "Show Job Classes":
                        showJobClasses();
                        break;
                    case "Add Job Class":
                        addJob();
                        break;
                    case "Update Job Class":
                        updateJob();
                        break;
                    case "Delete Job Class":
                        deleteJob();
                        break;
                }
            }
        });

        add(panel);
    }

    private void showJobClasses() {
        panel.removeAll();
        setSize(600, 400);
        panel.setLayout(new BorderLayout());

        List<Job> jobList = mongoDataAdapter.getAllJobClasses();

        String[] columnNames = {"Job Class ID", "Job Title", "Hourly Wage"};
        Object[][] data = new Object[jobList.size()][3];

        for (int i = 0; i < jobList.size(); i++) {
            Job job = jobList.get(i);
            data[i][0] = job.getJobClassID();
            data[i][1] = job.getJobTitle();
            data[i][2] = job.getHourlyWage();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = getBackButton();
        panel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }


    private void addJob() {
        panel.removeAll(); // Clear previous components
        panel.setLayout(new GridLayout(4, 2));

        JLabel jobClassIDLabel = new JLabel("Job Class ID:");
        jobClassIDField = new JTextField();
        panel.add(jobClassIDLabel);
        panel.add(jobClassIDField);

        JLabel jobTitleLabel = new JLabel("Job Title:");
        jobTitleField = new JTextField();
        panel.add(jobTitleLabel);
        panel.add(jobTitleField);

        JLabel hourlyWageLabel = new JLabel("Hourly Wage:");
        hourlyWageField = new JTextField();
        panel.add(hourlyWageLabel);
        panel.add(hourlyWageField);

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
                // Add job class
                int jobClassID = Integer.parseInt(jobClassIDField.getText());
                String jobTitle = jobTitleField.getText();
                double hourlyWage = Double.parseDouble(hourlyWageField.getText());
                Job job = new Job(jobClassID, jobTitle, hourlyWage);
                mongoDataAdapter.addJobClass(job);
                // Show operation selection view
                showOperationSelectionView();
            }
        });
        return addButton;
    }

    private void updateJob() {
        panel.removeAll(); // Clear previous components
        panel.setLayout(new GridLayout(4, 2));

        JLabel jobClassIDLabel = new JLabel("Job Class ID:");
        jobClassIDField = new JTextField();
        panel.add(jobClassIDLabel);
        panel.add(jobClassIDField);

        JLabel jobTitleLabel = new JLabel("Job Title:");
        jobTitleField = new JTextField();
        panel.add(jobTitleLabel);
        panel.add(jobTitleField);

        JLabel hourlyWageLabel = new JLabel("Hourly Wage:");
        hourlyWageField = new JTextField();
        panel.add(hourlyWageLabel);
        panel.add(hourlyWageField);

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
                // Update job class
                int jobClassID = Integer.parseInt(jobClassIDField.getText());
                String jobTitle = jobTitleField.getText();
                double hourlyWage = Double.parseDouble(hourlyWageField.getText());
                Job job = new Job(jobClassID, jobTitle, hourlyWage);
                mongoDataAdapter.updateJobClass(job);
                // Show operation selection view
                showOperationSelectionView();
            }
        });
        return updateButton;
    }

    private void deleteJob() {
        panel.removeAll(); // Clear previous components
        panel.setLayout(new GridLayout(2, 2));

        JLabel jobClassIDLabel = new JLabel("Job Class ID:");
        jobClassIDField = new JTextField();
        panel.add(jobClassIDLabel);
        panel.add(jobClassIDField);

        JButton deleteButton = getDeleteButton();
        panel.add(deleteButton);

        JButton backButton = getBackButton();
        panel.add(backButton);

        revalidate(); // Refresh the layout
        repaint(); // Repaint the component
    }

    private JButton getDeleteButton() {
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete job class
                int jobClassID = Integer.parseInt(jobClassIDField.getText());
                 mongoDataAdapter.deleteJobClass(jobClassID);
                // Show operation selection view
                showOperationSelectionView();
            }
        });
        return deleteButton;
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
            JobView jobView = new JobView();
            jobView.setVisible(true);
        });
    }
}
