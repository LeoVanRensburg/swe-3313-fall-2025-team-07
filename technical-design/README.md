# Technical Design

## Table of Contents

- [Implementation Language(s)](#a-implementation-languages)
- [Implementation Framework(s)](#b-implementation-frameworks)
- [Data Storage Plan](#c-data-storage-plan)
- [Entity Relationship Diagram](#d-entity-relationship-diagram)
- [Entity/Field Descriptions](#e-entityfield-descriptions)
- [Data Examples](#f-data-examples)
- [Database Seed Data](#g-database-seed-data)
- [Authentication and Authorization Plan](#h-authentication-and-authorization-plan)
- [Coding Style Guide](#i-coding-style-guide)
- [Technical Design Presentation](#technical-design-presentation)

## A. Implementation Language(s)

## B. Implementation Framework(s)

## C. Data Storage Plan
We chose to use a SQLite database to have our application store our data. To access the SQLite database, we will be using the SQLite-JDBC driver for Java. 

To write and access our database we will:
1. Add the SQLite-JDBC dependency to our pom.xml file for Maven
2. Connect to the SQLite database using the JDBC URL string.
3. Create our tables with necessary seed data (if database is not found, meaning it's the programs first run)
4. Store Data using prepared statements for persistent inserts/updates
5. Retrieve Data by querying the database
6. Close Resources by closing connections to ensure data is flushed to the disk

We will ensure that the database is stored in a physical `.db` file on the disk, so any changes to the database persist. 

[Here](./DatabaseExample.java) is some example code for how we plan on modifying and accessing our database. 

Below are some article that show examples for how to access a SQLite database in Java using the SQLite-JDBC driver:
1. [Article 1](https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/)
2. [Article 2](https://www.tutorialspoint.com/sqlite/sqlite_java.htm)

## D. Entity Relationship Diagram

## E. Entity/Field Descriptions

## F. Data Examples

## G. Database Seed Data

## H. Authentication and Authorization Plan

## I. Coding Style Guide

## Technical Design Presentation