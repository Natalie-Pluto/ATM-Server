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
        authenticated = true;
    }

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
            Scanner input = new Scanner(System.in);
            while (true) {
                counter++;
                if (counter > 3) {
                    isValid = false;
                    System.err.println("Sorry, you have exceeded the allowed number of attempts. Your card is blocked. Please contact the staff.");
                    break;
                }
                String pinNum = input.nextLine();
                if (pinNum.equals(pin)) {
                    return true;
                } else {
                    System.err.printf("Wrong pin number, please enter again.\n" + "(You have %d more attempts)\n", (3 - counter));
                }
            }
        }
        return isValid;
    }

    public boolean withdraw(double amount) {

        return false;
    }

    public boolean deposit(double amount) {

        return false;
    }

    public void addCash(double amount) {
        cash += amount;
    }

    public double getCash() {
        return cash;
    }

    
}