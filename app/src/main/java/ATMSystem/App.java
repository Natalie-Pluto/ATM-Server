/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ATMSystem;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
public class App {
    private double atmBalance;
    private static App instance;
    public App(double atmBalance) {
        this.atmBalance = atmBalance;
    }
    public static void main(String[] args) throws InterruptedException {
        instance = new App(100000);
        instance.run();
    }

    public void run() throws InterruptedException {
        // Greetings and ask user to insert their card for validity check.
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        System.out.println(instance.greetings(timeOfDay));
        Scanner userInput = new Scanner(System.in);
        while (userInput.hasNext()) {
            // If the service for an user is over, return to the greeting page.
            boolean isServiceOver = false;
            // Count the times of enter
            int cardEnterCounter = 0;
            service:
            while (!isServiceOver) {
                // First, we ask user to enter their card number
                String cardNumber;
                if (cardEnterCounter == 0) {
                    // The first user's input does not have timer
                    cardNumber = userInput.nextLine();
                    if(cardNumber.equals("CLOSE")) {
                        System.exit(0);
                    }
                } else {
                    // 120s timer
                    cardNumber = instance.timer();
                    if (cardNumber == null) {
                        instance.timeOut();
                        break;
                    }
                }
                // Check the format of the card number entered by user
                if (cardNumber.length() != 5 || !cardNumber.chars().allMatch(Character::isDigit)) {
                    cardEnterCounter++;
                    // User only allowed to enter 5 times
                    if (cardEnterCounter > 4) {
                        instance.illegalCardMsg();
                        break;
                    }
                    System.err.println("Invalid card number. Please enter the card number again:");
                } else {
                    // The format of card number entered is correct, now check card validity.
                    DB db = new DB(cardNumber, "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
                    boolean isCardExist;
                    isCardExist = db.isCardexist(cardNumber);
                    // If card not exists, output error msg to stderr and return to greeting page.
                    if (!isCardExist) {
                        instance.cardNotexistMsg();
                        break;
                    } else {
                        // Further card checking (issue & expired date)
                        boolean correctCardinfo;
                        correctCardinfo = db.isCardInfoMatch(cardNumber);
                        if (!correctCardinfo) {
                            printGreetings();
                            break;
                        }
                        // ask user to enter the pin
                        int pinCounter = 0;
                        System.out.println("Please enter the pin: ");
                        while (pinCounter < 3) {
                            pinCounter++;
                            String pin = instance.timer();
                            if (!db.authenticate(cardNumber, pin)) {
                                instance.pinMsg(pinCounter);
                                if (pinCounter == 3) {
                                    db.setBlocked(true);
                                    break service;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    // The card id valid, ask user to choose a service.
                    // Print the services options
                    boolean isContinue = true;
                    int invalidCounter = 0;
                    while (isContinue) {
                        instance.serviceMsg();
                        // Get the input (user got 120s to choose)
                        String service = instance.timer();
                        if (service == null) {
                            instance.timeOut();
                            break service;
                        }
                        switch (service) {
                            // Withdraw
                            case "1":
                                System.out.println("Please enter the amount you want to withdraw:");
                                String withdraw = instance.timer();
                                if (withdraw == null) {
                                    instance.timeOut();
                                    break service;
                                }
                                // Check if the balance is enough
                                if (db.getBalance() < Double.parseDouble(withdraw)) {
                                    instance.withdrawInsMsg(cardNumber);
                                    break;
                                } else {
                                    // Update ATM balance
                                    if (Double.parseDouble(withdraw) > instance.getAtmBalance()) {
                                        instance.atmMsg();
                                        // add cash to the atm
                                        instance.addAtmCash(1000000);
                                        break service;
                                    } else {
                                        // Update card balance
                                        db.setBalance(db.getBalance() - Double.parseDouble(withdraw));
                                        instance.setAtmBalance(instance.getAtmBalance() - Double.parseDouble(withdraw));
                                        // Print the receipt
                                        instance.receipt(cardNumber, Double.parseDouble(withdraw), "Withdraw", db.getBalance());
                                        // End of transaction
                                        endTrans();
                                        isContinue = false;
                                        isServiceOver = true;
                                    }
                                }
                                break;
                            case "2":
                                // Deposit
                                System.out.println("Please enter the amount you want to deposit:");
                                String deposit = instance.timer();
                                if (deposit == null) {
                                    instance.timeOut();
                                    break service;
                                }
                                // Can't deposit coins -> check if deposit is an integer
                                try {
                                    Integer.parseInt(deposit);
                                } catch (NumberFormatException ex) {
                                    instance.depositMsg();
                                    break;
                                }
                                // Update card balance
                                db.setBalance(db.getBalance() + Double.parseDouble(deposit));
                                // Update ATM balance
                                instance.setAtmBalance(instance.getAtmBalance() + Double.parseDouble(deposit));
                                // Print the receipt
                                instance.receipt(cardNumber, Double.parseDouble(deposit), "Deposit", db.getBalance());
                                // End of transaction
                                endTrans();
                                isContinue = false;
                                isServiceOver = true;
                                break;
                            case "3":
                                // Balance check
                                instance.balance(cardNumber);
                                break;
                            case "4":
                                // End of transaction
                                endTrans();
                                isContinue = false;
                                isServiceOver = true;
                                break;
                            default:
                                // Invalid service number is entered.
                                invalidCounter++;
                                if(invalidCounter > 2) {
                                    endTrans();
                                    isContinue = false;
                                    isServiceOver = true;
                                    break;
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    // Print greeting message according to the time.
    public String greetings (int timeOfDay) {
        if(timeOfDay < 6){
            return ("Good Night, welcome to XYZ Bank! Please enter your card number:");
        }else if(timeOfDay < 12){
            return ("Good Morning, welcome to XYZ Bank! Please enter your card number:");
        }else if(timeOfDay < 18){
            return ("Good Afternoon, welcome to XYZ Bank! Please enter your card number:");
        }else if(timeOfDay < 24){
            return ("Good Evening, welcome to XYZ Bank! Please enter your card number:");
        } else {
            return ("Good Evening, welcome to XYZ Bank! Please enter your card number:");
        }
    }

    public void printGreetings() {
        // Greetings and ask user to insert their card for validity check.
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        System.out.println(greetings(timeOfDay));
    }

    // Set a timer of 120s for user's input
    // Source from: https://stackoverflow.com/questions/49578598/timelimit-for-valid-java-input-without-system-exit
    public String timer() throws InterruptedException {
        BlockingDeque<String> deque = new LinkedBlockingDeque<>();

        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input;
            try {
                do {
                    if (System.in.available() > 0) {
                        input = scanner.nextLine();
                        deque.add(input);
                    } else
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            break;
                        }
                } while (true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
        int i = 0;
        String str;
        do {
            str = deque.poll(2, TimeUnit.SECONDS);
            i++;
        } while (i < 1);

        if(str != null) {
            if (str.equals("CLOSE")) {
                System.exit(0);
            }
        }

        thread.interrupt();
        return str;
    }

    // Print receipt
    public void receipt(String cardNum, double amount, String transType, double cardBalance) throws InterruptedException {
        UUID transNum = UUID.randomUUID();
        DecimalFormat dollarFormat = new DecimalFormat("####,###,###.00");
        System.out.print("\nTransaction Succeed." + " Printing Receipt...");
        Thread.sleep(2000);
        System.out.print("\n" + "\n");
        System.out.println(">>>>>>>> XYZ Bank --------------------------------------------------");
        System.out.println("Transaction Type: " + transType);
        System.out.println("Card Number: " + cardNum);
        if(transType.equals("Withdraw")) {
            System.out.println("Amount Withdrawn: $" + dollarFormat.format(amount));
        } else if (transType.equals("Deposit")) {
            System.out.println("Amount Deposited: $" + dollarFormat.format(amount));
        }
        System.out.println("Account Balance: $" + dollarFormat.format(cardBalance));
        System.out.println("Transcation Number: " +transNum);
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
    }

    // End of Transaction
    public void endTrans() throws InterruptedException {
        // End of transaction
        System.out.println("End of transaction...\n");
        System.out.println("Ejecting card...\n");
        Thread.sleep(2000);
        System.out.print("Thank you for using XYZ Bank ATM!\nPlease don't forget to take your card.\nLooking forward to your next visit.\n\n");
        Thread.sleep(3000);
        System.out.println("Returning to the main page...\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
        Thread.sleep(3000);
        printGreetings();
    }


    public void setAtmBalance(double amount) {

        this.atmBalance = amount;
    }

    public double getAtmBalance() {
        return atmBalance;
    }

    public void addAtmCash(double amount) throws InterruptedException {
        System.out.println("Waiting staff to add cash...\n");
        Thread.sleep(2000);
        this.atmBalance = getAtmBalance() + amount;
        System.out.println("Cash added.\n");
        System.out.println("Returning to main page.\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
        Thread.sleep(2000);
        printGreetings();
    }

    public void timeOut() throws InterruptedException {
        System.err.println("Time out!\n");
        System.out.println("Ejecting card...\n");
        Thread.sleep(2000);
        System.out.print("Thank you for using XYZ Bank ATM!\nPlease don't forget to take your card.\nLooking forward to your next visit.\n\n");
        Thread.sleep(2000);
        System.out.println("Returning to the main page...\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
        Thread.sleep(2000);
        printGreetings();
    }

    public void illegalCardMsg() throws InterruptedException {
        System.err.println("Exceed the enter limit, please contact the front desk to check on your card number.\n");
        Thread.sleep(2000);
        System.out.println("Ejecting card...\n");
        Thread.sleep(2000);
        System.out.print("Thank you for using XYZ Bank ATM!\nPlease don't forget to take your card.\nLooking forward to your next visit.\n\n");
        Thread.sleep(2000);
        System.out.println("Returning to the main page...\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
        Thread.sleep(2000);
        printGreetings();
    }

    public void cardNotexistMsg() throws InterruptedException {
        System.err.println("Sorry, this card is not recorded in our system.\nPlease contact the staff.\n");
        Thread.sleep(2000);
        System.out.println("Ejecting card...\n");
        Thread.sleep(2000);
        System.out.print("Thank you for using XYZ Bank ATM!\nPlease don't forget to take your card.\nLooking forward to your next visit.\n\n");
        Thread.sleep(2000);
        System.out.println("Returning to the main page...\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
        Thread.sleep(2000);
        printGreetings();
    }

    public void pinMsg(int num) throws InterruptedException {
        if (num == 1) {
            System.err.print("Wrong pin. Please enter again:\n" + "(You have 2 more attempts)\n");
        } else if (num == 2){
            System.err.print("Wrong pin. Please enter again:\n" + "(You have 1 more attempt)\n");
        } else if (num == 3) {
            System.err.print("Sorry, you have exceeded the allowed attempts, your card is blocked.\n" + "Please contact the staff if you need assistance.\n\n");
            Thread.sleep(3000);
            System.out.println("Ejecting card...\n");
            Thread.sleep(2000);
            System.out.print("Thank you for using XYZ Bank ATM!\nPlease don't forget to take your card.\nLooking forward to your next visit.\n\n");
            Thread.sleep(2000);
            System.out.println("Returning to the main page...\n");
            System.out.println("--------------------------------------------------------------------");
            System.out.print("\n");
            Thread.sleep(3000);
            printGreetings();
        }
    }

    public void serviceMsg() {
        System.out.print("Please choose a service number list below:\n" + "1.Withdraw    2.Deposit    3.Balance Check    4.Cancel\n");
        System.out.println("If you want to end the transaction, please choose cancel.");
    }

    public void withdrawInsMsg(String cardNumber) {
        DB db = new DB(cardNumber, "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        System.out.println("Insufficient balance.");
        DecimalFormat dollarFormat = new DecimalFormat("####,###,###.00");
        System.out.println("Available Account Balance: $" + dollarFormat.format(db.getBalance()) + "\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
    }

    public void atmMsg() throws InterruptedException {
        System.err.println("Sorry, this ATM has insufficient cash available.\n");
        System.err.println("End of Transaction...\n");
        System.out.println("Ejecting card...\n");
        Thread.sleep(2000);
        System.out.print("Thank you for using XYZ Bank ATM!\nPlease don't forget to take your card.\nLooking forward to your next visit.\n");
        Thread.sleep(2000);
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
    }

    public void depositMsg() {
        System.err.println("Sorry, no coins can be deposited.\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
    }

    public void balance(String cardNumber) {
        DB db = new DB(cardNumber, "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        DecimalFormat dollarFormat = new DecimalFormat("####,###,###.00");
        System.out.print("\n" + "\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Account Balance: $" + dollarFormat.format(db.getBalance()));
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n" + "\n");
    }

}
