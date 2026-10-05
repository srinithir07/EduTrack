# EduTrack - Academic Performance & CGPA Management System

[![Java Version](https://img.shields.io/badge/Java-21-emerald.svg)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-MySQL-blue.svg)](https://www.mysql.com/)

**EduTrack** is a clean, modern, and fully functional Academic Performance & CGPA Management System built with **Java 21**, **Spring Boot**, **Spring Data JPA**, **Hibernate**, and **Jakarta Validation**. The frontend is built using pure **HTML5**, **Vanilla CSS3**, and **Vanilla JavaScript** (no external framework build step required), served directly via Spring Boot's embedded Tomcat web server.

---

## Key Features

1. **Student Management**: Full CRUD operations for student records (Register Number, Name, Email, Department, Batch).
2. **Subject Management**: Manage course subjects with assigned credit weights (1 to 10 credits).
3. **Semester Management**: Track academic terms and academic calendar years.
4. **Marks & Performance Entry**: Enter student marks (0 to 100); the backend service automatically calculates Grade and Grade Point based on standardized academic scales.
5. **Credit-Weighted GPA Calculation**: Backend service computes semester-specific Grade Point Average (GPA).
6. **Overall CGPA Calculation**: Calculates Cumulative Grade Point Average (CGPA) across all completed semesters.
7. **Academic Standing Evaluation**: Automatically categorizes academic status (*Excellent*, *Good*, *Satisfactory*, *Needs Improvement*).
8. **Academic Summary Transcript View**: Comprehensive report page showing student details, overall CGPA, status badge, and semester-wise subject breakdown tables.
9. **Centralized Exception Handling**: Provides informative error responses (404 Not Found, 400 Bad Request) via `@ControllerAdvice`.

---

## Technology Stack

### Backend
- **Language**: Java 21
- **Framework**: Spring Boot 3.2.5
- **Modules**: Spring Web, Spring Data JPA, Jakarta Validation
- **ORM / Persistence**: Hibernate 6
- **Build Tool**: Apache Maven (Maven Wrapper included)

### Database
- **Database Engine**: MySQL 8.x / XAMPP MySQL
- **In-Memory Test DB**: H2 Database (used automatically during unit/integration tests)

### Frontend
- **HTML5**: Semantic web UI structure
- **CSS3**: Custom design system using Emerald Green & White palette (No Bootstrap / No Tailwind)
- **Vanilla JavaScript**: Asynchronous fetch API calls to backend REST controllers

---

## Academic Grading Scale & Formulas

### 1. Grading Scale (Centralized in `Grade.java` Service Layer)
| Marks Range | Grade Code | Grade Point | Description |
| :--- | :---: | :---: | :--- |
| **Marks >= 90** | `O` | **10** | Outstanding |
| **Marks >= 80** | `A+` | **9** | Excellent |
| **Marks >= 70** | `A` | **8** | Very Good |
| **Marks >= 60** | `B+` | **7** | Good |
| **Marks >= 50** | `B` | **6** | Above Average |
| **Marks >= 40** | `C` | **5** | Average |
| **Marks < 40** | `F` | **0** | Fail |

### 2. Semester GPA Formula
$$\text{Semester GPA} = \frac{\sum (\text{Grade Point} \times \text{Subject Credits})}{\sum \text{Subject Credits}}$$

*Example*:
- Subject 1: Grade Point = 9 (A+), Credits = 4 $\rightarrow$ $9 \times 4 = 36$
- Subject 2: Grade Point = 8 (A), Credits = 3 $\rightarrow$ $8 \times 3 = 24$
- $\text{GPA} = \frac{36 + 24}{4 + 3} = \frac{60}{7} = 8.57$

### 3. Overall CGPA Formula
$$\text{Overall CGPA} = \frac{\sum (\text{Grade Point} \times \text{Subject Credits})}{\sum \text{Total Completed Credits}}$$

### 4. Academic Standing Scale
- **CGPA >= 8.5**: `Excellent`
- **CGPA >= 7.0**: `Good`
- **CGPA >= 6.0**: `Satisfactory`
- **CGPA < 6.0**: `Needs Improvement`

---

## Database & XAMPP Setup

1. Open **XAMPP Control Panel** and click **Start** next to **MySQL**.
2. No manual database creation step is required; the connection string includes `createDatabaseIfNotExist=true`.
3. Default database settings in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/academic_performance_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=
   ```

---

## How to Run the Project

### Option A: Using Windows Terminal (Maven Wrapper)

Open Command Prompt / PowerShell in the project root directory:

1. **Run Unit & Integration Tests**:
   ```cmd
   mvnw.cmd clean test
   ```

2. **Start the Spring Boot Application**:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

3. **Open Application in Browser**:
   Navigate to [http://localhost:8080/](http://localhost:8080/)

---

### Option B: Using IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click **File -> Open** and select the project root folder.
3. IntelliJ will detect `pom.xml` and automatically load Maven dependencies.
4. Open `src/main/java/com/edutrack/academic/EduTrackApplication.java`.
5. Click the green **Run** button (or press `Shift + F10`).
6. Access the UI at [http://localhost:8080/](http://localhost:8080/).

---

## REST API Endpoints Summary

| Entity / Function | Method | Endpoint URL | Description |
| :--- | :---: | :--- | :--- |
| **Students** | `GET` | `/api/students` | List all students |
| | `GET` | `/api/students/{id}` | Get student details |
| | `POST` | `/api/students` | Register a new student |
| | `PUT` | `/api/students/{id}` | Update student details |
| | `DELETE` | `/api/students/{id}` | Delete student record |
| **Subjects** | `GET` | `/api/subjects` | List all subjects |
| | `POST` | `/api/subjects` | Add a new subject |
| | `PUT` | `/api/subjects/{id}` | Update subject |
| | `DELETE` | `/api/subjects/{id}` | Delete subject |
| **Semesters** | `GET` | `/api/semesters` | List all semesters |
| | `POST` | `/api/semesters` | Add new semester |
| **Performance** | `GET` | `/api/performance` | List all performance records |
| | `POST` | `/api/performance` | Record marks (Backend calculates grade) |
| | `DELETE` | `/api/performance/{id}` | Delete performance record |
| **Academic** | `GET` | `/api/academic/students/{id}/summary` | Full transcript, CGPA, GPA & status |
| | `GET` | `/api/academic/students/{id}/cgpa` | Get overall CGPA |
| | `GET` | `/api/academic/students/{id}/semesters/{semId}/gpa` | Get semester GPA |

---

## Postman Testing Guide

1. Open **Postman**.
2. Click **Import** and select `postman/AcademicPerformance.postman_collection.json`.
3. The collection contains pre-configured requests with JSON body templates for testing all CRUD endpoints and academic calculation APIs.

---
## License & Author
Academic Project - Built for Academic Viva & Evaluation Demonstration.
