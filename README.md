# Personal Expense Tracker
A modern, full-stack expense tracking application built with Spring Boot and Angular. Track, manage, and analyze your expenses with ease.

# Features

User Authentication — Secure login and registration with JWT tokens
Expense Management — Create, update, delete, and view expenses
Real-time Updates — Instant sync across all devices
RESTful API — Clean and intuitive REST endpoints
Database Persistence — Secure data storage with PostgreSQL
Comprehensive Logging — Detailed application logs with timestamps
Auto-Deployment — CI/CD pipeline with GitHub Actions
Error Handling — Robust error management and validation

# Quick Start
Prerequisites

Java 21 or higher
PostgreSQL database
Git
Gradle

# Installation
1. Clone the repository:
   bashgit clone https://github.com/jonahmutua/personal-expense-tracker-backend.git
   cd personal-expense-tracker-backend
2. Configure database:
   Edit config/application.yml:
   yamlspring:
   datasource:
   url: jdbc:postgresql://localhost:5432/expenses
   username: admin
   password: your_password
3. Build the application:
   bash./gradlew clean build
4. Run the application:
   bash./start.sh
   

# Project Structure
personal-expense-tracker-backend/
├── src/
│   ├── main/
│   │   ├── java/com/jonah/
│   │   │   ├── controller/      # REST API endpoints
│   │   │   ├── service/         # Business logic
│   │   │   ├── repository/      # Database access
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── dto/             # Data transfer objects
│   │   │   └── utils/           # Utility classes
│   │   └── resources/
│   │       └── application.yml  # Configuration
│   └── test/                    # Unit tests
├── config/
│   └── application.yml          # Deployment configuration
├── logs/
│   └── app.log                  # Application logs
├── lib/
│   └── *.jar                    # Built JAR files
├── build.gradle                 # Dependencies and build config
├── gradlew                      # Gradle wrapper
└── start.sh                     # Startup script

# API Endpoints
# Authentication
POST /api/auth/register — Register new user
POST /api/auth/login — Login user
POST /api/auth/logout — Logout user

# Expenses
GET /api/expenses — Get all expenses
GET /api/expenses/{id} — Get specific expense
POST /api/expenses — Create new expense
PUT /api/expenses/{id} — Update expense
DELETE /api/expenses/{id} — Delete expense

# Sample Response Format
json{
"success": true,
"message": "Operation completed successfully",
"data": {
"id": 1,
"description": "Groceries",
"amount": 45.50,
"date": "2025-11-23"
},
"location": "/api/expenses/1"
}

# Database Schema
Users Table
sql CREATE TABLE app_user (
id BIGINT PRIMARY KEY,
username VARCHAR(255) UNIQUE,
email VARCHAR(255) UNIQUE,
password VARCHAR(255),
);

Expenses Table
sqlCREATE TABLE expense (
id BIGINT PRIMARY KEY,
description VARCHAR(255),
amount DECIMAL(10, 2),
date DATE,
user_id BIGINT REFERENCES app_user(id)
);

Configuration
Environment Variables
Set these in your .env or system environment:
bash# Database
DB_URL=jdbc:postgresql://localhost:port/expenses
DB_USERNAME= database_username
DB_PASSWORD= database_password

# Server
PORT=port_number

# Timezone
TZ=Your/Timezone
Logging Configuration
Logs are configured in application.yml:
yamllogging:
level:
root: INFO
com.jonah: DEBUG
file:
name: logs/app.log
max-size: 10MB
pattern:
file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

Deployment
VPS Deployment with GitHub Actions
The project includes automated deployment via GitHub Actions.
1. Set up GitHub Secrets:
   Go to Settings → Secrets and variables → Actions and add:
   VPS_SSH_KEY         (your private SSH key)
   VPS_USER            (SSH username)
   VPS_IP              (VPS IP address)
   APP_PATH            (deployment path, e.g., /opt/expensesapp)
2. Automatic Deployment
   Push to main branch and the workflow will:

a) Build the application
b) Copy JAR to deployment directory
c) Restart the service

Manual Deployment
bash# SSH into VPS
ssh user@vps-ip

# Navigate to app directory
cd /opt/app_name

# Pull latest code
git pull origin main

# Build
./gradlew clean build 

# Copy JAR
cp build/libs/*.jar lib/

# Restart service
sudo systemctl restart appservicename

# Check logs
sudo journalctl -u appservicename -n 20

Logging
Application logs are saved to logs/app.log with the following format:
2025-11-23 15:30:45.123 [http-nio-8080-exec-1] INFO  com.jonah.controller.ExpenseController - GET /api/expenses
2025-11-23 15:30:46.456 [http-nio-8080-exec-2] DEBUG com.jonah.service.ExpenseService - Processing expense creation
Log Rotation:

Maximum file size: 10MB
Maximum history: 10 files
Total cap: 100MB

Dependencies
Key dependencies in build.gradle:

Spring Boot 3.x — Web framework
Spring Data JPA — ORM
PostgreSQL Driver — Database
Lombok — Boilerplate reduction
Spring Security — Authentication
JUnit 5 — Testing


Troubleshooting
Application won't start
Check logs:
bashtail -f logs/app.log
Common issues:

Database connection failed → Check DB_URL, username, password
Missing JAR → Run ./gradlew clean build


📝 Git Workflow
bash# Create feature branch
git checkout -b feature/new-feature

# Make changes and commit
git add .
git commit -m "Add new feature"

# Push to GitHub
git push origin feature/new-feature

# Create Pull Request and merge to main
# Deployment happens automatically!

 Security
a) JWT token-based authentication
b) Password hashing 
c) SQL injection protection via JPA
d) CORS configuration
e) Environment variables for sensitive data

License
This project is licensed under the MIT License — see the LICENSE file for details.

Author
Jonah Mutua
GitHub: @jonahmutua