# MySQL Database Setup Guide

## Quick Setup Instructions

### 1. Install MySQL (if not already installed)
- Download MySQL Community Server from [mysql.com](https://dev.mysql.com/downloads/mysql/)
- Or use XAMPP/WAMP which includes MySQL

### 2. Start MySQL Service
```bash
# Windows (if MySQL is installed as service)
net start MySQL80

# Or start via XAMPP/WAMP control panel
```

### 3. Create Database (Optional - auto-created by Spring Boot)
The application will automatically create the database `student_admission_crm` on first run.

If you want to create it manually:
```sql
CREATE DATABASE student_admission_crm;
```

### 4. Update Credentials (if needed)
Edit `application.properties` if your MySQL credentials are different:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 5. Run the Application
```bash
cd Backend
mvn spring-boot:run
```

## Configuration Details

**Database Name:** `student_admission_crm`  
**Port:** 3306 (default MySQL port)  
**Username:** root (change in application.properties)  
**Password:** root (change in application.properties)  

**Tables Created Automatically:**
- `users` - User authentication (existing)
- `leads` - Lead management (new)

## Hibernate DDL Mode

`spring.jpa.hibernate.ddl-auto=update`
- Creates tables if they don't exist
- Updates schema when entities change
- **Safe for development/demo**
- For production, use `validate` or Flyway/Liquibase

## File Upload Limits

- Max file size: 10MB
- Max request size: 10MB
- Sufficient for Excel files with thousands of leads

## Troubleshooting

### Error: Access denied for user 'root'@'localhost'
**Solution:** Update username/password in `application.properties`

### Error: Communications link failure
**Solution:** Ensure MySQL service is running

### Error: Unknown database 'student_admission_crm'
**Solution:** The database should be auto-created. If not, create it manually:
```sql
CREATE DATABASE student_admission_crm;
```

### Error: Table 'leads' doesn't exist
**Solution:** Ensure `spring.jpa.hibernate.ddl-auto=update` is set. Spring Boot will create tables automatically.

## Cloud Deployment (Railway/Render)

For cloud deployment, update `application.properties` with environment variables:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Railway/Render will provide these environment variables when you add a MySQL addon.
