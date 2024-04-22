package edu.neu.csye6200.ui;

import edu.neu.csye6200.handler.CsvFileHandlerFactory;
import edu.neu.csye6200.handler.DbHandler;
import edu.neu.csye6200.model.Employee;
import edu.neu.csye6200.model.MenuItem;
import edu.neu.csye6200.model.User;
import edu.neu.csye6200.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private CsvFileHandlerFactory csvFileHandlerFactory;
    private DbHandler dbHandler;
    private JLabel itemCountLabel;
    private JButton signInButton;
    private JButton signUpButton;
    // private JButton backButton;
    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

    public MainFrame() {
        initializeUI();
        this.csvFileHandlerFactory = CsvFileHandlerFactory.getInstance();
        this.dbHandler = DbHandler.getInstance();
        loadUserDetails();
    }

    public static void demo() {
        new MainFrame().setVisible(true);
    }

    private void initializeUI() {
        setTitle("Restaurant Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setupBackgroundPanel();
        setupActionListeners();
    }

    private void loadUserDetails() {
        csvFileHandlerFactory = CsvFileHandlerFactory.getInstance();
        List<User> users = csvFileHandlerFactory.readUserDetails(Constants.DATABASE_FILE_PATH);
        dbHandler.insertUsers(users);
    }

    private void performLogin(LoginDialog loginDialog) {
        if (loginDialog.isAuthenticated()) {
            // "Staff" or "Customer"
            String userRole = loginDialog.getUserType();
            getContentPane().removeAll();
            setupTable();
            setupSortAndItemCountPanel();
            readOperation();
            if ("Staff".equals(userRole)) {  //change to staff later
                setupStaffUI();
            } else if ("Customer".equals(userRole)) {
                setupCustomerUI();
            }
            getContentPane().revalidate(); // Revalidate container
            getContentPane().repaint(); // Repaint container
        } else {
            // User failed to authenticate or closed the login dialog
            dispose(); // Close the MainFrame
        }
    }

    public void performRegistration(SignUpDialog signUpDialog) {
        if(signUpDialog.isRegistered()) {
            User newUser = signUpDialog.getNewUser();
            if ("Customer".equals(newUser.getUserType())) {
                dbHandler.insertUser(newUser);
            } else if ("Staff".equals(newUser.getUserType())) {
                if (newUser instanceof Employee) {
                    dbHandler.insertEmployee((Employee) newUser);
                }
            }
            JOptionPane.showMessageDialog(this, "Registration successful. Please sign in.");
        }
    }

    private void setupActionListeners() {
        signUpButton.addActionListener(e -> {
            SignUpDialog signUpDialog = new SignUpDialog(this);
            signUpDialog.setVisible(true);
        });
        signInButton.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setVisible(true);
            // Perform login operation
            if (loginDialog.isAuthenticated()) {
                performLogin(loginDialog);
            }
        });
        // backButton.addActionListener(e -> {
        //     // Add your back button logic here
        //     // For example, you can hide the current frame and show the previous frame
        //     this.setVisible(false);
        //     // Dispose of the current frame to free resources
        //     // this.dispose();
        //     new MainFrame().setVisible(true);

            
        //     // Check if previousFrame is not null and then show it
            
        // });
    }

    private void setupBackgroundPanel() {

        // Relative path to the image within the resources folder
        String imagePath = "/images/Restaurant.png";
        URL imageUrl = getClass().getResource(imagePath);

        // Create the background panel with the image
        BackgroundPanel backgroundPanel;
        if (imageUrl != null) {
            backgroundPanel = new BackgroundPanel(imageUrl);
        } else {
            System.err.println("Image not found: " + imagePath);
            backgroundPanel = new BackgroundPanel(); // Fallback to a default background panel
        }

        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        setContentPane(backgroundPanel);

        signInButton = createCustomButton("Sign In"); //method that creates buttons and then calls another method to customize them.
        signUpButton = createCustomButton("Sign Up");
        // backButton = createCustomButton("Back");
        JLabel welcomeLabel = createWelcomeLabel(); //method that creates welcome label and then calls another method to customize it.

        // Add components to backgroundPanel
        backgroundPanel.add(Box.createVerticalGlue()); // Add space at the top
        backgroundPanel.add(welcomeLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add 10px vertical padding
        backgroundPanel.add(signInButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add 10px vertical padding
        backgroundPanel.add(signUpButton);
        backgroundPanel.add(Box.createVerticalGlue()); // Add space at the bottom
        // backgroundPanel.add(backButton);
    }


    private JLabel createWelcomeLabel() {
        JLabel label = new JLabel("Welcome to Our Restaurant Management System");
        customizeLabel(label);
        return label;
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        customizeButton(button);
        return button;
    }

    private void customizeLabel(JLabel label) {
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setOpaque(true);
        label.setBackground(new Color(245, 245, 245));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
    }

    private void customizeButton(JButton button) {
        int paddingWidth = 200;
        int paddingHeight = 50;
        button.setPreferredSize(new Dimension(paddingWidth, paddingHeight));
        button.setBackground(new Color(100, 149, 237)); // Light blue background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

    // private void setupBackButton() {
    //     JButton backButton = new JButton("Back");
    //     backButton.addActionListener(e -> {
    //         // Add your back button logic here
    //         // For example, you can hide the current frame and show the previous frame
    //     });
    //     add(backButton);
    // }

    private void setupTable() {
        tableModel = new DefaultTableModel(new String[]{"Name", "Price", "Type"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) {
                    return Double.class; // Price
                } else {
                    return String.class; // Name and Type
                }
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells uneditable
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        styleTable();

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(1).setCellRenderer(renderer);
    }
    private void setupSortAndItemCountPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(setupSortPanel(), BorderLayout.NORTH);
        topPanel.add(setupItemCountPanel(), BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
    }

    private JPanel setupSortPanel() {
        JPanel sortPanel = new JPanel();
        JComboBox<String> sortOptions = new JComboBox<>(new String[]{"Sort by Name", "Sort by Price", "Sort by Type"});
        JComboBox<String> sortOrderOptions = new JComboBox<>(new String[]{"Ascending", "Descending"});
        JButton sortButton = new JButton("Sort");

        sortPanel.add(new JLabel("Sort Field:"));
        sortPanel.add(sortOptions);
        sortPanel.add(new JLabel("Sort Order:"));
        sortPanel.add(sortOrderOptions);
        sortPanel.add(sortButton);

        sortButton.addActionListener(e -> sortTable(
                sortOptions.getSelectedIndex(),
                sortOrderOptions.getSelectedIndex() == 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING
        ));

        return sortPanel;
    }

    private JPanel setupItemCountPanel() {
        JPanel itemCountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        itemCountLabel = new JLabel();
        itemCountPanel.add(itemCountLabel);
        updateItemCount();
        return itemCountPanel;
    }

    private void styleTable() {
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(200, 200, 200)); // Light gray grid lines
        table.getTableHeader().setBackground(new Color(150, 150, 150)); // Darker gray header
        table.getTableHeader().setForeground(Color.WHITE); // White text for header
    }

    private void addButton(String text, JPanel panel, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        panel.add(button);
    }

    private void showEmployeeDetails() {
        JDialog employeeDetailsDialog = new JDialog(this, "Employee Details");
        employeeDetailsDialog.setLayout(new BorderLayout());

        JTable employeeTable = new JTable(); // Table to display employee details
        employeeDetailsDialog.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // Read employee details and populate the table
        List<Employee> employees = csvFileHandlerFactory.readEmployeeDetails(Constants.EMPLOYEE_FILE_PATH);
        populateEmployeeTable(employeeTable, employees);
        dbHandler.insertEmployees(employees);

        employeeDetailsDialog.setSize(400, 300); // Set the size of the dialog
        employeeDetailsDialog.setLocationRelativeTo(this); // Center it relative to the main frame
        employeeDetailsDialog.setVisible(true);

        CheckInDialog checkInDialog = new CheckInDialog(this);
        checkInDialog.setVisible(true);

        String employeeId = checkInDialog.getEmployeeId();
        if (employeeId != null && csvFileHandlerFactory.doesEmployeeExist(employeeId, Constants.EMPLOYEE_FILE_PATH)) {
            performCheckIn(employeeId);
        } else {
            JOptionPane.showMessageDialog(this, "Employee ID not found.");
        }
    }

    private void populateEmployeeTable(JTable table, List<Employee> employees) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Employee ID");
        model.addColumn("Age");
        model.addColumn("Role");
        model.addColumn("In Time");
        model.addColumn("Out Time");

        for (Employee employee : employees) {
            model.addRow(new Object[]{employee.getId(), employee.getAge(), employee.getRole(), employee.getInTime(), employee.getOutTime()});
        }

        table.setModel(model);
    }

    private void setupStaffUI() {
        JPanel buttonPanel = new JPanel();
        addButton("Create", buttonPanel, e -> createOperation());
        addButton("Update", buttonPanel, e -> updateOperation());
        addButton("Delete", buttonPanel, e -> deleteOperation());
        addButton("Employee Directory", buttonPanel, e -> showEmployeeDetails());

        add(buttonPanel, BorderLayout.SOUTH);

        logoutButton(buttonPanel);
    }

    private void setupCustomerUI() {
        // Customer-specific UI components
        JLabel welcomeLabel = new JLabel("Welcome, browse our menu!");
        add(welcomeLabel, BorderLayout.SOUTH);
        logoutButton(new JPanel());
    }

    private void logoutButton(JPanel buttonPanel) {
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void logout() {
        dispose(); // Dispose the current MainFrame
        // Clear any sensitive data or reset variables if necessary
        new MainFrame().setVisible(true); // Create a new instance of MainFrame
    }

    // CRUD Operations
    private void createOperation() {
        JTextField nameField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JComboBox<String> typeField = new JComboBox<>(new String[]{"Starter", "Main Course", "Dessert", "Beverage"});

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); // Use GridLayout for structured layout
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter New Menu Item Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String type = (String) typeField.getSelectedItem();

                MenuItem menuItem = new MenuItem(name, price, type);
                List<MenuItem> menuItems = csvFileHandlerFactory.readMenuItems(Constants.MENU_FILE_PATH);
                menuItems.add(menuItem);
                dbHandler = DbHandler.getInstance();
                dbHandler.insertMenuItems(menuItems);
                csvFileHandlerFactory.writeMenuItems(Constants.MENU_FILE_PATH, menuItems);
                updateItemCount();
                readOperation(); // Refresh data display
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid price.");
                logger.error("Invalid input. Please enter a valid price.");
            }
        }
    }

    private void readOperation() {
        List<MenuItem> menuItems = csvFileHandlerFactory.readMenuItems(Constants.MENU_FILE_PATH);
        tableModel.setRowCount(0); // Clear existing data
        for (MenuItem item : menuItems) {
            tableModel.addRow(new Object[]{item.getName(), item.getPrice(), item.getType()});
        }
    }

    private void updateOperation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Convert the view index to model index
            int modelIndex = table.convertRowIndexToModel(selectedRow);

            // Capture the details of the selected item
            String name = (String) table.getModel().getValueAt(modelIndex, 0);
            double price = (Double) table.getModel().getValueAt(modelIndex, 1);
            String type = (String) table.getModel().getValueAt(modelIndex, 2);

            JTextField nameField = new JTextField(name, 10);
            JTextField priceField = new JTextField(String.valueOf(price), 10);
            JComboBox<String> typeField = new JComboBox<>(new String[]{"Starter", "Main Course", "Dessert", "Beverage"});
            typeField.setSelectedItem(type);

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.add(new JLabel("Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Price:"));
            panel.add(priceField);
            panel.add(new JLabel("Type:"));
            panel.add(typeField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Edit Menu Item", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    List<MenuItem> menuItems = csvFileHandlerFactory.readMenuItems(Constants.MENU_FILE_PATH);
                    MenuItem itemToUpdate = menuItems.get(modelIndex);
                    MenuItem updatedItem = new MenuItem(nameField.getText(), Double.parseDouble(priceField.getText()), (String) typeField.getSelectedItem());
                    itemToUpdate.setName(nameField.getText());
                    itemToUpdate.setPrice(Double.parseDouble(priceField.getText()));
                    itemToUpdate.setType((String) typeField.getSelectedItem());
                    csvFileHandlerFactory.writeMenuItems(Constants.MENU_FILE_PATH, menuItems);
                    dbHandler = DbHandler.getInstance();
                    dbHandler.updateMenuItem(itemToUpdate, updatedItem);
                    updateItemCount();
                    readOperation(); // Refresh data display
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid price.");
                    logger.error("Invalid input. Please enter a valid price.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item to update.");
        }
    }

    private void deleteOperation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirmation = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this item?", "Delete Item", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    List<MenuItem> menuItems = csvFileHandlerFactory.readMenuItems(Constants.MENU_FILE_PATH);
                    // Convert the view index to the model index
                    int modelIndex = table.convertRowIndexToModel(selectedRow);
                    // Remove the item from the list
                    menuItems.remove(modelIndex);
                    // Write the updated list back to the CSV
                    csvFileHandlerFactory.writeMenuItems(Constants.MENU_FILE_PATH, menuItems);
                    dbHandler = DbHandler.getInstance();
                    dbHandler.deleteMenuItem(menuItems.get(modelIndex));
                    // Refresh the table display
                    updateItemCount();
                    readOperation();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error occurred while deleting the item.");
                    logger.error("Error occurred while deleting the item.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an item to delete.");
        }
    }

    private void sortTable(int sortOption, SortOrder sortOrder) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        // Ensure that price values are sorted numerically
        sorter.setComparator(1, Comparator.comparingDouble(value -> {
            if (value instanceof Double) {
                return (Double) value;
            }
            return Double.parseDouble(value.toString()); // Convert string to double for sorting
        }));

        // Determine the column to sort by
        int columnIndex;
        switch (sortOption) {
            case 0:
                columnIndex = 0;
                break; // Name
            case 1:
                columnIndex = 1;
                break; // Price
            case 2:
                columnIndex = 2;
                break; // Type
            default:
                return;
        }

        sorter.setSortKeys(List.of(new RowSorter.SortKey(columnIndex, sortOrder)));
    }

    private void updateItemCount() {
        csvFileHandlerFactory = CsvFileHandlerFactory.getInstance();
        List<MenuItem> menuItems = csvFileHandlerFactory.readMenuItems(Constants.MENU_FILE_PATH);
        Map<String, Long> countByType = menuItems.stream()
                .collect(Collectors.groupingBy(MenuItem::getType, Collectors.counting()));

        long totalCount = menuItems.size(); // Total count of all menu items

        StringBuilder countsText = new StringBuilder("Total: " + totalCount + "\t\t\t");
        countByType.forEach((type, count) -> countsText.append(type).append(": ").append(count).append("\t\t\t"));

        itemCountLabel.setText(countsText.toString());
    }

    private void performCheckIn(String employeeId) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // Log the check-in time
        dbHandler = DbHandler.getInstance();
        Employee teBeUpdatedEmployee = dbHandler.getEmployeeFromId(employeeId);
        if (teBeUpdatedEmployee == null) {
            JOptionPane.showMessageDialog(this, "Employee ID not found.");
            return;
        }
        teBeUpdatedEmployee.setInTime(formattedDateTime);
        dbHandler.updateEmployee(employeeId, teBeUpdatedEmployee);
        csvFileHandlerFactory.logEmployeeCheckIn(employeeId, formattedDateTime, Constants.EMPLOYEE_CHECKIN_FILE_PATH);

        JOptionPane.showMessageDialog(this, "Check-in recorded for Employee ID: " + employeeId + " at " + formattedDateTime);
    }

    public class CheckInDialog extends JDialog {
        private final JTextField employeeIdField;
        private boolean isSubmitted = false;

        public CheckInDialog(JFrame parent) {
            super(parent, "Employee Check-In", true);
            setLayout(new FlowLayout());

            employeeIdField = new JTextField(10);
            add(new JLabel("Employee ID:"));
            add(employeeIdField);

            JButton checkInButton = new JButton("Check-In");
            checkInButton.addActionListener(e -> {
                isSubmitted = true;
                setVisible(false);
            });
            add(checkInButton);

            setSize(300, 100);
            setLocationRelativeTo(parent);
        }

        public String getEmployeeId() {
            return isSubmitted ? employeeIdField.getText() : null;
        }
    }
}