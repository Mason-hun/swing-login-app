# Swing Login Application

A Java desktop login application built with **Java Swing**, **MySQL**, and **JDBC**. It authenticates users against an existing database account and displays a welcome screen after successful login.

## Features

- User login through a Java Swing graphical interface.
- MySQL database integration using JDBC.
- Authentication against existing user accounts.
- Secure password storage using PBKDF2 with HMAC-SHA256.
- Prepared statements for database queries.
- Background authentication using `SwingWorker` to keep the interface responsive.
- Welcome screen displaying the authenticated user's username.
- Generic error messages for invalid login credentials.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| Java Swing | Graphical user interface |
| MySQL 8 | Database management |
| JDBC | Database connectivity |
| Maven | Dependency management and project build |
| MySQL Connector/J | MySQL JDBC driver |
| IntelliJ IDEA | Development environment |

## Project Structure

```text
SwingLoginApp/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── ngari/
                    ├── Main.java
                    ├── database/
                    │   └── DBConnection.java
                    ├── model/
                    │   └── User.java
                    ├── security/
                    │   └── PasswordUtil.java
                    ├── dao/
                    │   └── UserDAO.java
                    └── gui/
                        ├── LoginFrame.java
                        └── WelcomeFrame.java
```

## Database Setup

1. Install and start MySQL Server.
2. Open MySQL Workbench.
3. Execute the following SQL statements:

```sql
CREATE DATABASE login_system;

USE login_system;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);
```

4. Create a dedicated database account for the application:

```sql
CREATE USER 'login_app'@'localhost'
IDENTIFIED BY 'YOUR_DATABASE_PASSWORD';

GRANT SELECT ON login_system.users
TO 'login_app'@'localhost';
```

The application account only needs permission to read existing user records.

## Environment Variables

The application reads its database credentials from environment variables rather than storing them in source code.

Configure these Windows environment variables:

```text
DB_USER=login_app
DB_PASSWORD=YOUR_DATABASE_PASSWORD
```

Restart IntelliJ IDEA after configuring the variables so the IDE can access them.

## Adding Users

User accounts must already exist in the database before they can log in.

Passwords should be hashed using the application's `PasswordUtil.hashPassword()` method before being inserted into the `users` table.

The application does not currently provide a registration interface.

## Running the Application

1. Clone or download the repository.
2. Open the project in IntelliJ IDEA.
3. Ensure the MySQL server is running.
4. Configure the database environment variables.
5. Allow Maven to resolve the project dependencies.
6. Run the application's login entry point:

```text
com.ngari.gui.LoginFrame
```

Enter the credentials of an existing database user.

A successful login opens the welcome screen. Invalid credentials display an error message.

## Security Measures

- Passwords are stored as PBKDF2-HMAC-SHA256 hashes rather than plaintext.
- Random salts are generated when passwords are hashed.
- Prepared statements are used to parameterize SQL queries.
- Database credentials are supplied through environment variables.
- The application uses a dedicated database account instead of the MySQL root account.
- Authentication runs in the background to prevent the GUI from freezing during database operations.

## Project Scope

This project demonstrates desktop GUI development, database connectivity, user authentication, password hashing, and basic separation of application responsibilities using Java.

It is an academic demonstration project and is not intended to be a production-ready authentication system.