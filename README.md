# XYZ Bank ATM System

---

An ATM application written in Java.

## Table of Contents

---
* [General Info](#general-info)
* [Setup](#setup)
* [Run the Program](#run-the-program)
* [Functionalities and Usages](#functionalities-and-usages)
* [Tests](#tests)
* [Project Contributors](#project-contributors)
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

    You are connected to our shared database. The link to the shared database is:

    ```
    http://13.211.7.238/pgadmin4/browser/
   ```

    Username: `dbmasteruser`
    Password: `A>XV>D*7r-V{y_wL}}I{+U=8zEtj1*T<`
   
   If you wish to use your own database, you can follow the instruction:

    1. Open PostgreSQL.
    2. Create a new Datebase. 
    3. Copy and paste the following code into the query tool then run it.
       It will create a schema called `atmserver` and a table called `card` for you.

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
    
    4. To connect the program to your local database:
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
BUILD SUCCESSFUL in 13s
7 actionable tasks: 7 executed
```

To run the program, please use the command:

`gradle run --console=plain`

If run successful, you will see the greeting:

```
> Task :app:run
Good Morning, welcome to XYZ Bank! Please enter your card number:
```
Now, you can use the application :)

## Functionalities and Usages

---
- ### **User's Interface**

    The User's interface we made for the application is CLI. The program will start with a greeting then 
    the user will be asked to provide their card number:
    ```
    Good Morning, welcome to XYZ Bank! Please enter your card number:
    ```
    To interact with the program, user should type their response in terminal via keyboard.

- ### **Timer for User's Response**

    A two minutes timer is set for user's response. If no user's response in two minutes, the program will return
    to the main page:
    ```
    Time out!
    Ejecting card...
  
    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.

    Returning to the main page...

    --------------------------------------------------------------------

    Good Evening, welcome to XYZ Bank! Please enter your card number:
    ```
    
    Note: There's no timer set for the user's response in main page.

- ### **Check the Validity of the Card**
    After the user enters the card number, the program will check if the card number provided
    is legal. A legal card number should consist of 5 digits. If the card number is illegal,
    an error message will be printed out in terminal:
  
    ```
    Invalid card number. Please enter the card number again:
    ```
    User will have 5 attempts in total. If they used up all 5 attempts, an error message will be 
    printed out in terminal, then the program will return to main page:

    ```
    Exceed the enter limit, please contact the front desk to check on your card number.
    
    Ejecting card...
  
    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.
  
    Returning to the main page...

    --------------------------------------------------------------------

    Good Evening, welcome to XYZ Bank! Please enter your card number:

    ```
    If the card number provided is not recorded in the database, an error message will be printed to the 
    terminal and then the program will return to the main page:
    ```
    Sorry, this card is not recorded in our system.
    Please contact the staff.

    Ejecting card...
  
    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.
  
    Returning to the main page...

    --------------------------------------------------------------------

    Good Evening, welcome to XYZ Bank! Please enter your card number:

    ```
  
    If the card number is legal and exists in the database, it's issue date, expire date and state will be checked.
    
    If any of above condition is invalid, a related error message will be printed out to the terminal, 
    and the program will return to main page. Example:
  
    ```
    Sorry, this card is reported stolen or lost.
    Sorry, this card is expired.
    Ejecting card...

    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.

    Returning to the main page...

    --------------------------------------------------------------------

    Good Evening, welcome to XYZ Bank! Please enter your card number:
    ```
    
  
- ### **Check the Pin**
    If the card is valid, user will be asked to enter the pin:
    ```
    Please enter the pin: 
    ```
    If the pin entered is not what has been recorded in the database, an error message will be printed out to 
    the terminal:
    ```
    Wrong pin. Please enter again:
    (You have 2 more attempts)
    ```
    User has three attempts to enter the pin, if they exceed the limit, their card will be blocked. An
    message will be printed out in the terminal. Then the program will return to the main page:
    ```
    Sorry, you have exceeded the allowed attempts, your card is blocked.
    Please contact the staff if you need assistance.

    Ejecting card...

    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.

    Returning to the main page...

    --------------------------------------------------------------------

    Good Evening, welcome to XYZ Bank! Please enter your card number:
    ```
- ### **Choose Service**

    This application can perform three services: enter `1` for 'Withdraw', `2` for 'Deposit', and `3` for 'Check Balance'.
    If the user wants to stop the transaction, they can enter `4` for 'Cancel'.
    ```
    Please choose a service:
    1.Withdraw    2.Deposit    3.Balance Check    4.Cancel
    If you want to end the transaction, please choose cancel.
    ```

- ### **1 - Withdraw**
    If user entered `1` which is 'Withdraw', the user will be asked to enter the amount to be withdrawn:
    ```
    Please enter the amount you want to withdraw:
    ```
    
    If the withdraw is approved, a recepit will be printed out to the terminal, and the program
    will then return to the main page:
    ```
    Transaction Succeed. Printing Receipt...

    >>>>>>>> XYZ Bank --------------------------------------------------
    Transaction Type: Withdraw
    Card Number: 10485
    Amount Withdrawn: $100.00
    Account Balance: $9,299.55
    Transcation Number: 4a7e3975-a213-4c54-a835-44258d519ed9
    --------------------------------------------------------------------

    End of transaction...
  
    Ejecting card...
  
    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.
  
    Returning to the main page...

    --------------------------------------------------------------------

    Good Afternoon, welcome to XYZ Bank! Please enter your card number:
    ```
    If the card has an insufficient amount available, an error message will be printed out to the
    terminal. The account balance will be shown as well. User will be able to choose the service again:：
    ```
    Insufficient balance.
    Available Account Balance: $10,000.00

    --------------------------------------------------------------------

    Please choose a service:
    1.Withdraw    2.Deposit    3.Balance Check    4.Cancel
    If you want to end the transaction, please choose cancel.
    ```

- ### **2 - Deposit**
    If user entered `2` which is 'Deposit', the user will be asked to enter the amount to be deposited:
    ```
    Please enter the amount you want to deposit:
    ```
    If the deposit is approved, a receipt will be printed out to the terminal, and the program
    will then return to the main page:
  
    ```
    Transaction Succeed. Printing Receipt...

    >>>>>>>> XYZ Bank --------------------------------------------------
    Transaction Type: Deposit
    Card Number: 10485
    Amount Deposited: $500.00
    Account Balance: $9,799.55
    Transcation Number: cfe0ebdf-a886-43d9-a819-4b8cda835501
    --------------------------------------------------------------------

    End of transaction...
  
    Ejecting card...
  
    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.
  
    Returning to the main page...

    --------------------------------------------------------------------

    Good Evening, welcome to XYZ Bank! Please enter your card number:
    ```
    Coins can not be deposited, therefore an error message will be printed to the terminal
    if the user tried to deposit coins. They will be able to choose the service again:
    ```
    Sorry, no coins can be deposited.
    --------------------------------------------------------------------


    Please choose a service number list below:
    1.Withdraw    2.Deposit    3.Balance Check    4.Cancel
    If you want to end the transaction, please choose cancel.
    ```

- ### **3 - Check Balance**
    If user entered `3` which is 'Check Balance', the account balance will be shown in the terminal.
    They will be able to choose the service again::
    ```
    --------------------------------------------------------------------
    Account Balance: $9,299.55
    --------------------------------------------------------------------
    
    Please choose a service number list below:
    1.Withdraw    2.Deposit    3.Balance Check    4.Cancel
    If you want to end the transaction, please choose cancel.
    ```
  

- ### **4 - Cancel the Transaction**

    If user entered `4` which is 'cancel', the transaction will be ended, the program will return to main
    page.
    ```
    End of transaction...
  
    Ejecting card...
  
    Thank you for using XYZ Bank ATM!
    Please don't forget to take your card.
    Looking forward to your next visit.
  
    Returning to the main page...

    --------------------------------------------------------------------

    Good Afternoon, welcome to XYZ Bank! Please enter your card number:
    ```

- ### **Invalid Service Number**
  
  If the user entered invalid service number for more than three times, an error message will be
  printed in the terminal. The card will be ejected, and the program will return to the 
  main page.
  
  ```
  End of transaction...

  Ejecting card...

  Thank you for using XYZ Bank ATM!
  Please don't forget to take your card.
  Looking forward to your next visit.
  
  Returning to the main page...

  --------------------------------------------------------------------

  Good Evening, welcome to XYZ Bank! Please enter your card number:
  ```

- ### **Routine Maintenance**
  If the ATM has insufficient cash available, the transaction will be ended, cash will be added to the ATM:
  ```
  --------------------------------------------------------------------
  Sorry, this ATM has insufficient cash available.

  End of Transaction...
  Waiting staff to add cash...

  Cash added.
  
  Returning to main page.

  --------------------------------------------------------------------

  Good Evening, welcome to XYZ Bank! Please enter your card number:
  ```

- ### **Terminate the Application**
  To terminate the application, type the passcode `CLOSE`.


## **Tests**

---
 Tests cases are available in :
`/app/src/test/java/ATMSystem/AppTest.java`

Note that the testcases are made based on the data we have made in our shared database. 
If you wish to use youe own database to run the tests, please insert the following data into your database:

```
BEGIN TRANSACTION;

SET search_path to 'atmserver';

INSERT INTO Card VALUES (10485,4455,'2020-01-13','2025-1-13',false,10000,false); -- required for DB testin
INSERT INTO Card VALUES (10000,0000,'2020-01-13','2025-1-13',false,10000,false); -- required for DB testin
INSERT INTO Card VALUES (11111,2222,'2020-01-13','2025-1-13',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99901,2222,'2020-11-23','2021-1-20',true,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99902,2222,'2020-11-23','2021-1-20',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99903,2222,'2020-11-23','2026-1-21',true,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99904,2222,'2020-11-23','2026-1-21',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99905,2222,'2020-11-23','2021-1-20',true,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99906,2222,'2022-11-23','2021-1-20',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99907,2222,'2022-11-23','2026-1-21',true,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99908,2222,'2022-11-23','2026-1-21',false,10000,true); -- required for DB testing
INSERT INTO Card VALUES (99909,2222,'2020-11-23','2021-1-20',true,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99910,2222,'2020-11-23','2021-1-20',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99911,2222,'2020-11-23','2026-1-21',true,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99912,2222,'2020-11-23','2026-1-21',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99913,2222,'2022-11-23','2021-1-20',true,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99914,2222,'2022-11-23','2021-1-20',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99915,2222,'2022-11-23','2026-1-21',true,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99916,2222,'2022-11-23','2026-1-21',false,10000,false); -- required for DB testing
INSERT INTO Card VALUES (99917,2222,'2022-11-23','2026-1-21',false,10000,false); -- rwequired for DB testing

COMMIT;
```

 You can run the tests and get a code coverage report by running the following commands:

```
gradle clean build
gradle test jacocoTestReport
```

You can find the code coverage report here:
`/app/build/reports/jacoco/test/html/index.html`

Or 

You can conduct black box testing with the information provided in 'Functionalities & Usages'
section.



## **Project Contributors**

---

Ali Lieberman -- https://github.sydney.edu.au/alie0302

Chao (Charles) Luo -- https://github.sydney.edu.au/cluo3734

Jiayin (Natalie) Lu -- https://github.sydney.edu.au/jilu8927

Linxuan (John) Jiang -- https://github.sydney.edu.au/ljia0550


## Reference

---
SOFT 2412 - Tutorial 3 - Build Automation with Gradle
https://edstem.org/au/courses/6848/lessons/14709/slides/106167

    