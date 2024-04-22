package edu.neu.csye6200.handler;


import edu.neu.csye6200.model.Employee;
import edu.neu.csye6200.model.MenuItem;
import edu.neu.csye6200.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvFileHandlerFactory {
    private static final Logger logger = LoggerFactory.getLogger(CsvFileHandlerFactory.class);

    public static CsvFileHandlerFactory getInstance() {
        return new CsvFileHandlerFactory();
    }

    public List<MenuItem> readMenuItems(String filePath) {
        List<MenuItem> menuItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    String name = values[0];
                    double price = Double.parseDouble(values[1]);
                    String type = values[2];
                    menuItems.add(new MenuItem(name, price, type));
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            logger.error("Error parsing double: " + e.getMessage());
        }
        return menuItems;
    }

    public void writeMenuItems(String filePath, List<MenuItem> menuItems) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (MenuItem item : menuItems) {
                pw.println(item.getName() + "," + item.getPrice() + "," + item.getType());
            }
        } catch (IOException e) {
            logger.error("Error writing to file: " + e.getMessage());
        }
    }

    // Method to read user details from a CSV file
    public List<User> readUserDetails(String filePath) {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 3) {
                    String username = values[0];
                    String encryptedPassword = values[1];
                    String userType = values[2];
                    User user = new User(username, encryptedPassword, userType);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file: " + e.getMessage());
        }
        return users;
    }



    // Method to write user details to a CSV file
    public void writeUserDetails(String filePath, List<User> users) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (User user : users) {
                pw.println(user.getUsername() + "," + user.getEncryptedPassword() + "," + user.getUserType());
            }
        } catch (IOException e) {
            logger.error("Error writing to file: " + e.getMessage());
        }
    }

    public List<Employee> readEmployeeDetails(String filePath) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming CSV values are comma-separated
                if (values.length >= 5) {
                    String employeeId = values[0].trim();
                    int age = Integer.parseInt(values[1].trim());
                    String role = values[2].trim();
                    String inTime = values[3].trim();
                    String outTime = values[4].trim();

                    // Employee employee = new Employee(employeeId, age, role, inTime, outTime);
                    Employee employee = new Employee(null, null, null, employeeId, age, role, inTime, outTime);

                    employees.add(employee);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file: " + e.getMessage());
        }
        return employees;
    }


    public boolean doesEmployeeExist(String employeeId, String filePath) {
        List<Employee> employees = readEmployeeDetails(filePath);
        for (Employee employee : employees) {
            if (employee.getId().equals(employeeId)) {
                return true;
            }
        }
        return false;
    }

    public void logEmployeeCheckIn(String employeeId, String checkInTime, String filePath) {
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(employeeId + "," + checkInTime);
        } catch (IOException e) {
            logger.error("Error writing to file: " + e.getMessage());
        }
    }

}
