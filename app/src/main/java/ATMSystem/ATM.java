package ATMSystem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ATM {
    //private fields
    private double cash;
    boolean authenticated;

    // constructor
    public ATM(double startingCash) {
        cash = startingCash;
    }

    // Enter > 3 times pin will block the card
    private void block() {

    }

    private boolean changeBalance() {

        return false;
    }

    public boolean authentication(Date issueDate, Date expDate, String isReported, String isBlocked, String pin) throws InterruptedException {;
        boolean isValid = true;
        // Check the issue date:
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        if (issueDate.after(currentDate)) {
            System.err.println("Sorry, this card is not issued yet.");
            isValid = false;
        }
        // Check the expDate:
        if(expDate.before(currentDate)) {
            System.err.println("Sorry, this card is expired");
            isValid = false;
        }
        // Check if the card has been reported lost/stolen
        if(isReported.equalsIgnoreCase("T")) {
            System.err.println("Sorry, this card has been reported lost/stolen.");
            isValid = false;
        }
        // Check if this card is blocked
        if(isBlocked.equalsIgnoreCase("T")) {
            System.err.println("Sorry, this card is blocked.");
            isValid = false;
        }
        // Lastly, ask user to enter the pin
        if(isValid) {
            System.out.println("Please enter your pin:");
            int counter = 0;
            App app = new App();
            while (true) {
                counter++;
                if (counter > 3) {
                    isValid = false;
                    // Block the card
                    block();
                    System.err.println("Sorry, you have exceeded the allowed number of attempts. Your card is blocked. Please contact the staff.");
                    break;
                }
                String pinNum = app.timer();
                if(pinNum == null) {
                    System.err.println("Time out!");
                    return false;
                }
                if (pinNum.equals(pin)) {
                    return true;
                } else {
                    System.err.printf("Wrong pin number, please enter again.\n" + "(You have %d more attempts)\n", (3 - counter));
                }
            }
        }
        return isValid;
    }

    // 1. Check if there's enough money left to be withdraw
    // 2. Change the card balance
    // 3. Change the ATM balance
    // 4. Check ATM balance
    public boolean withdraw(double amount) {

        return false;
    }

    public boolean deposit(double amount) {

        return false;
    }

    public void addCash(double amount) {
        cash += amount;
    }

    public void subCash(double amount) {
        cash -= amount;
    }


    public double getCash() {
        return cash;
    }

    
}
