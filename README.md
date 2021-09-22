#XYZ Bank ATM System

---

An ATM application written in Java.

##Table of Contents

---
* [General Info](#general-info)
* [Setup](#setup)
* [Run the Program](#run-the-program)
* [Functionalities & Test](#functionalities-&-test)
* [Reference](#reference)


---

## General Info

---
This project is a simple ATM software in Java. It can perform standard ATM services such as 'Withdraw'
, 'Deposit' and 'Check Balance'. It can also perform routine maintenance. The user's interface for this application is CLI.

## Setup

---
1. ### **Installing Gradle**
- **Windows**
      
  Firstly, generate the necessary gradle wrapper files:

      gradle wrapper

  Then run this command:
      
      ./gradlew wrapper --gradle-version 4.2

  Now, you must use the “gradlew” program in the current folder to build your tasks under this new specified version.
  The version may be changed.

  For more information on gradle wrapper, please refer to this link:
  https://docs.gradle.org/current/userguide/gradle_wrapper.html
      

      
- **Mac OS / Linux**
    
  Firstly, please check your Java version in your machine (you will need Java JDK version 7 or higher):

      java -version
    
  You should see something like this:

      java 9.0.4
      Java(TM) SE Runtime Environment (build 9.0.4+11)
      Java HotSpot(TM) 64-Bit Server VM (build 9.0.4+11, mixed mode)

  To install Gradle in your own machine, you can find the detailed instruction here:
  https://gradle.org/install/
  
2. ### Installing PostgreSQL
- Please click the link below to download PostgreSQL:
  
    https://www.postgresql.org/download/

3. ### Setup Database

    1. Create a new Datebase. 
    2. Create a schema called `atmserver` and a table called `card`.
    
       Copy and paste the following code into the query tool then run it:

    ```
    DROP SCHEMA IF EXISTS atmserver CASCADE;
    CREATE SCHEMA atmserver;
    SET search_path to 'atmserver';

    DROP TABLE IF EXISTS Card CASCADE;
    CREATE TABLE Card (
    card_number CHAR(5) PRIMARY KEY NOT NULL,
    pin CHAR(4) NOT NULL,
    issue_date DATE NOT NULL,
    exp_date DATE NOT NULL,
    blocked BOOLEAN NOT NULL,
    balance DECIMAL NOT NULL,
    confiscated BOOLEAN NOT NULL
    );

    BEGIN TRANSACTION;

    SET search_path to 'atmserver';

    COMMIT;
    ```
    3. Insert your data into the `card` table.
    
    4. Connect the program to your local database.
    
       Please change the code on line 68 of `App.java`:
    ```
    DB db = new DB(cardNumber, "<your_username>", "<your_password>", "jdbc:postgresql://localhost:<your_port>/atmserver");
    ```
    
    Now you have connected your local database to the program. You are ready to go!

## Run the Program

---
Firstly, please run the command:

`gradle build`

If build successful, you will see something like this:

```
Starting a Gradle Daemon (subsequent builds will be faster)

BUILD SUCCESSFUL in 13s
7 actionable tasks: 7 executed
```

To run the program, please use the command:

`gradle run`

If run successful, you will see the greeting:

```
> Task :app:run
Good Morning, welcome to XYZ Bank! Please enter your card number:
```
Now, you can use the application :)

## Functionalities & Test

---




## Reference

---
SOFT 2412 - Tutorial 3 - Build Automation with Gradle
https://edstem.org/au/courses/6848/lessons/14709/slides/106167

    