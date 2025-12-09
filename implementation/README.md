# Implementation

This guide explains how to install and run the Big 5 Shop e-commerce shop.

## Environment Setup

Before running the application, ensure you have the following installed:

- **Java 21** or higher
- **Apache Maven 3.9+** for building the project

### Verify Installation

```bash
java --version
mvn --version
```

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/LeoVanRensburg/swe-3313-fall-2025-team-07.git
   cd swe-3313-fall-2025-team-07
   ```

2. **Build the application**

   ```bash
   mvn clean install
   ```

   This will download all dependencies and compile the project.

## Data Storage Setup

Before running the application for the first time, you need to populate the database with initial product data:

```bash
mvn exec:java -Dexec.mainClass="org.big5.shop.SeedDatabase"
```

This script will:
- Create an admin user (`admin@example.com` with password `admin`)
- Populate the database with video game items from various collections (Pokemon, Zelda, Mario, Kirby, Kingdom Hearts, Minecraft)

## How to Start and Login

Start the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

### Accessing the Application

Once the application is running, open your browser and navigate to:

```
http://localhost:8080
```

## Database

The application uses SQLite with a database file (`big5shop.db`) located in the project root. No additional database setup is required beyond running the seed script.

## Admin Panel (Sales Report, Promote User to Admin, Add Items)
The application comes with an admin panel that can be accessed through clicking the User icon in the top right corner. The admin panel allows you to add items to the shop, promote users to admins, and view the sales report.
