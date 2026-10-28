# Library Management System

A simple console application for managing a library, built with **Java (OOP)**, **JDBC**, and **MySQL**.

## Features
- Add, list, search, and delete books
- Add and list members
- Borrow and return books (using database transactions)
- View active and overdue loans

## Project Structure
| File | Purpose |
|------|---------|
| `Book`, `Member`, `Loan` | Model classes |
| `BookDAO`, `MemberDAO`, `LoanDAO` | Database access (JDBC + SQL) |
| `DBConnection` | MySQL connection settings |
| `Main` | Console menu |
| `schema.sql` | Database schema and sample data |

## Requirements
- JDK 17 or newer
- MySQL Server
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (Platform Independent `.jar`)

## Setup
1. Run `schema.sql` in MySQL to create the database and tables.
2. Edit `DBConnection.java` and set your MySQL password.
3. Put the Connector/J `.jar` file in the project folder.

## Compile and Run
Windows:
```
javac -cp ".;mysql-connector-j-X.X.X.jar" *.java
java -cp ".;mysql-connector-j-X.X.X.jar" Main
```
Mac/Linux:
```
javac -cp ".:mysql-connector-j-X.X.X.jar" *.java
java -cp ".:mysql-connector-j-X.X.X.jar" Main
```
Replace `X.X.X` with your Connector/J version.

## Ideas for Improvement
- Late fees for overdue books
- Search by ISBN
- Edit book details
- User login
