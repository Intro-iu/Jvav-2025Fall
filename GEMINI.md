# Project Context: News Information Management System (Jvav-2025Fall)

## Project Overview
This project is a course design assignment to build a **News Information Management System**. It is a desktop application designed for news editors to manage, publish, categorize, and search for news articles.

### Technical Stack & Constraints
*   **Language:** Java
*   **GUI Framework:** Swing (Strict requirement: No third-party GUI libraries).
*   **Database:** MySQL.
*   **Architecture:** Object-Oriented Programming (OOP) with clear separation of concerns (likely MVC or DAO pattern recommended).
*   **Dependencies:** No third-party libraries allowed, except for the MySQL JDBC driver.

## Current State
The project is currently in the **initial scaffolding phase**.
*   `src/App.java` contains a basic "Hello, World!" entry point.
*   `lib/` directory exists but is currently empty (needs MySQL Connector).
*   No database schema or additional logic has been implemented yet.

## Directory Structure
*   `src/`: Source code directory. Currently contains the main entry point `App.java`.
*   `lib/`: Directory for external JAR dependencies (specifically the MySQL JDBC driver).
*   `bin/`: Destination for compiled `.class` files (ignored by git).
*   `.vscode/`: Local IDE configuration (ignored by git).

## Development Guide

### Prerequisites
1.  **JDK**: Java Development Kit installed (likely Java 8 or higher).
2.  **MySQL**: MySQL Server installed and running.
3.  **JDBC Driver**: The MySQL Connector/J JAR file must be downloaded and placed in the `lib/` directory.

### Building and Running (CLI)
Since this is a raw Java project without a build tool like Maven/Gradle, use the following commands from the project root:

**1. Compile:**
```bash
# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files in src to bin, including lib in classpath
javac -d bin -cp "lib/*" -sourcepath src src/App.java
```

**2. Run:**
```bash
# Run the main class, including bin and lib in classpath
# Note: Use ';' instead of ':' for classpath separator on Windows.
java -cp "bin:lib/*" App
```

### Roadmap / Next Steps
1.  **Environment Setup**: Download `mysql-connector-j-*.jar` and place it in `lib/`.
2.  **Database Design**: Create the MySQL database and tables (News, Category, User/Editor).
3.  **Architecture**: Create packages (e.g., `model`, `view`, `controller`, `util`).
4.  **Implementation**: Start implementing the Login/Main UI and Database connection utility.
