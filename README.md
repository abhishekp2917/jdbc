# jdbc
This repo demonstrate implementation of JDBC using various JDBC drivers. In this repo, MySQL is used as a DBMS.

# Database Setup

SQL queries to create and set up the database and its tables which has been used in the code. This queries are more focused to MySQL.

## Create Database

```sql
CREATE DATABASE 'temp'
```

## Employee Table

```sql
CREATE TABLE 'employee' (
  'ID' int NOT NULL AUTO_INCREMENT,
  'Name' varchar(20) NOT NULL,
  'Age' int DEFAULT NULL,
  'Salary' float NOT NULL,
  PRIMARY KEY ('ID')
)
```

## Person Table

```sql
CREATE TABLE 'person' (
  'ID' int NOT NULL AUTO_INCREMENT,
  'Image' mediumblob,
  'Description' text,
  PRIMARY KEY ('ID')
)
```

## Policy Table

```sql
CREATE TABLE 'policy' (
  'policyNumber' int NOT NULL AUTO_INCREMENT,
  'effectiveDate' date NOT NULL,
  'expirationDate' date NOT NULL,
  PRIMARY KEY ('policyNumber')
)
```

##  Student Table

```sql
CREATE TABLE 'student' (
  'ID' int DEFAULT NULL,
  'FirstName' varchar(20) DEFAULT NULL,
  'LastName' varchar(20) DEFAULT NULL,
  'Age' int DEFAULT NULL
)
```

## Users Table

```sql
CREATE TABLE 'users' (
  'UserID' int NOT NULL,
  'Username' varchar(20) NOT NULL,
  'Password' varchar(20) NOT NULL,
  PRIMARY KEY ('UserID')
)
```