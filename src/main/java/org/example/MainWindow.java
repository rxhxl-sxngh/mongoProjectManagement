package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Main Window");
        setSize(1000, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a JLabel for the title
        JLabel titleLabel = new JLabel("Project Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set a large font
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align the text

        // Buttons to open different views
        JButton openJobViewButton = new JButton("Open Job View");
        openJobViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JobView jobView = new JobView(); // Open Job View
                jobView.setVisible(true);
            }
        });

        JButton openEmployeeViewButton = new JButton("Open Employee View");
        openEmployeeViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmployeeView employeeView = new EmployeeView(); // Open Employee View
                employeeView.setVisible(true);
            }
        });

        JButton openProjectViewButton = new JButton("Open Project View");
        openProjectViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProjectView projectView = null;
                try {
                    projectView = new ProjectView(); // Open Project View
                    projectView.setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Panel to hold the title and buttons
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // Use BorderLayout for positioning
        panel.add(titleLabel, BorderLayout.NORTH); // Add the title label at the top
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel to hold buttons
        buttonPanel.add(openJobViewButton);
        buttonPanel.add(openEmployeeViewButton);
        buttonPanel.add(openProjectViewButton);
        panel.add(buttonPanel, BorderLayout.CENTER); // Add the buttons below the title

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}




