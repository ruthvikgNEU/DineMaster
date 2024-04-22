package edu.neu.csye6200.handler;

import com.mongodb.client.FindIterable;
import edu.neu.csye6200.data.EmployeeDao;
import edu.neu.csye6200.data.MenuDao;
import edu.neu.csye6200.data.UserDao;
import edu.neu.csye6200.model.Employee;
import edu.neu.csye6200.model.MenuItem;
import edu.neu.csye6200.model.User;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DbHandler {
    private static DbHandler instance;
    private static final Logger logger = LoggerFactory.getLogger(DbHandler.class);

    public static synchronized DbHandler getInstance() {
        if (instance == null) {
            instance = new DbHandler();
        }
        return instance;
    }

    private EmployeeDao employeeDao;
    private UserDao userDao;
    private MenuDao menuDao;

    private DbHandler() {
        this.employeeDao = new EmployeeDao();
        this.userDao = new UserDao();
        this.menuDao = new MenuDao();
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> itemList = new ArrayList<>();
        FindIterable<Document> items = menuDao.findDocuments("menuItems");
        for (Document item : items) {
            itemList.add(new MenuItem(item.getString("name"), item.getDouble("price"), item.getString("type")));
        }

        return itemList;
    }

    public void insertMenuItems(List<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            try {
                this.menuDao.insertDocument(menuItem.toDocument());
            } catch (Exception e) {
                logger.error("Failed to insert menu item: " + menuItem.getName());
            }
        }
    }

    // insertUsers inserts the users into the database
    // it is private method and is called by readUserDetails
    public void insertUsers(List<User> users) {
        for (User user : users) {
            try {
                this.userDao.insertDocument(user.toDocument());
            } catch (Exception e) {
                logger.error("Failed to insert user: " + user.getUsername());
            }
        }
    }

    public void insertUser(User user) {
        try {
            this.userDao.insertDocument(user.toDocument());
        } catch (Exception e) {
            logger.error("Failed to insert user: " + user.getUsername());
        }
    }

    public void insertEmployees(List<Employee> employees) {
        EmployeeDao employeeDao = new EmployeeDao();
        for (Employee employee : employees) {
            try {
                employeeDao.insertDocument(employee.toDocument());
            } catch (Exception e) {
                logger.error("Failed to insert employee: " + employee.getId());
            }
        }
    }

    public void insertEmployee(Employee employee) {
        try {
            this.employeeDao.insertDocument(employee.toDocument());
        } catch (Exception e) {
            logger.error("Failed to insert employee: " + employee.getId());
        }
    }

    public void updateMenuItem(MenuItem originalItem, MenuItem updatedItem) {
        Document filter = new Document("name", originalItem.getName());
        Document update = updatedItem.toDocument();
        this.menuDao.updateDocument("menuItems", filter, update);
    }


    public boolean doesEmployeeExistsDb(String employeeId) {
        return employeeDao.doesEmployeeExist(employeeId);
    }

    public Employee getEmployeeFromId(String employeeId) {
        FindIterable<Document> employees = employeeDao.findDocuments(employeeId);
        Document employee = employees.first();
        if (employee != null) {
            Employee e = new Employee(null, null, null, employee.getString("employeeId"), employee.getInteger("age"), employee.getString("role"), employee.getString("intime"), employee.getString("outtime"));
            return e;
        }
        return null;
    }

    public User getUserFromUserName(String username) {
        FindIterable<Document> users = userDao.findDocuments(username);
        Document user = users.first();
        if (user != null) {
            User u = new User(user.getString("username"), user.getString("encryptedPassword"), user.getString("userType"));
            return u;
        }
        return null;
    }

    public void deleteMenuItem(MenuItem menuItem) {
        Document filter = new Document("name", menuItem.getName());
        this.menuDao.deleteDocument(filter);
    }

    public void updateEmployee(String employeeId, Employee updatedEmployee) {
        Document filter = new Document("employeeId", employeeId);
        Document update = updatedEmployee.toDocument();
        this.employeeDao.updateDocument("employees", filter, update);
    }
}
