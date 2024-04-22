# Java Swing Restaurant Management System
> Group 4, SEC 05 Fall 2023 [BOS-2-TR]

## Description
A simple restaurant management system that allows users to log in as staff or customers. Staff members have access to CRUD operations for managing menu items, viewing employee details, and recording employee check-in times. Customers can view menu items.


### Prerequisites
1. Java JDK 11 or higher installed.
2. Access to a command line interface (CLI) or an Integrated Development Environment (IDE) like Eclipse, IntelliJ IDEA, or NetBeans.

### Setup
1. Clone the Repository:
```
Github URL goes here.....
```

2. connect to mongoDB instance using docker:
```
docker pull mongo
docker run -d -p 27017:27017 --name resturantdb mongo
```

3. Run
```
src/main/java/edu/neu/csye6200/Driver.java
```

## Features
- Login System: Differentiate user experience based on user role (Staff or Customer).
- Menu Management: Staff can create, read, update, and delete menu items.
- Employee Check-In: Staff can record employee check-in times.
- View Menu: Customers can view menu items.
