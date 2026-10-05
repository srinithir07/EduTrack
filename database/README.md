# Database Setup Guide (MySQL & XAMPP)

## Database Overview
The application connects to MySQL using Spring Data JPA & Hibernate.
- **Database Name**: `academic_performance_db`
- **Port**: `3306`
- **Default Username**: `root`
- **Default Password**: *(empty)*

---

## Instructions for XAMPP Users

1. Open **XAMPP Control Panel**.
2. Click **Start** next to **MySQL**.
3. (Optional) Open `http://localhost/phpmyadmin/` in your browser.
4. The database `academic_performance_db` will automatically be created on Spring Boot startup thanks to `createDatabaseIfNotExist=true` in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/academic_performance_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   ```
5. On initial launch, Hibernate will create 4 tables (`students`, `subjects`, `semesters`, `performances`) with appropriate foreign keys, and the `DataInitializer` class will automatically populate initial sample records.

---

## Instructions for Standalone MySQL Users

If you run MySQL with a password or custom port, edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/academic_performance_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```
