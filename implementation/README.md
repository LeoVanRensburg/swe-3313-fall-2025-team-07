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

If you do not have either Java 21+ or Maven 3.9+ installed, please install them by following their respective install instructions
on their respective websites:
- [Maven](https://maven.apache.org/install.html)
- [Java](https://www.oracle.com/java/technologies/downloads/)

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
- Create an admin user (`admin@example.com` with password `admin123`)
- Create a regular user (`user@example.com` with password `user123`) 
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

### AI Usage
AI was used to help write and debug portions of code that was used in this project. 