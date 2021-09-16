package ATMSystem;
import java.sql.*;

public class ATM {
    // private fields:
    // - cash: represents the cash in the ATM. An ATM object is initialised with some 
    //         amount of cash.
    // - authenticated: 
    private double cash;
    private String cardNumber; // cardNumber == null if and only if a user is not authenticated.

    // constructor
    public ATM(double startingCash) {
        cash = startingCash;
    }

    private double getBalance() {
        /*
        This method returns the balance of the card currently authenticated. If 
        the atm is not in the authenticated state (i.e. cardNumber = -1), then -1
        is returned.
        */
        //TODO
        if (null == cardNumber) return -1;
        return 0;

    }
    
    private void block() {
        /*
        This method blocks the currently authenticated card.
        */
        //TODO
    }

    private void confiscate() {
        /*
        sets the confiscated to 
        */
        //TODO
    }

    private boolean changeBalance() {
        //TODO
        return false;
    }

    private boolean cardFormatCheck(String cardNum, String pin) {
        // TODO
        return false;
    }

    public boolean authentication(String cardNum, String pin) {
        /*
        This method checks if the cardNum and pin represent a row in the card table, if yes 
        then true is returned. If cardNum or pin are invalid or the combination does not exist
        in the card table, false is returned.
        */
        //TODO: fix and add parameter test
       
        Connection c = null;
        Statement stmt = null;
        String query = "SELECT * FROM atmserver.\"Card\" WHERE card_number = '" + cardNum  + "' AND pin = '" + pin + "';";

        try {
           // Class.forName("org.postgresql.Driver");
            c = DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/a12412", "alien", "fuck"); 
                // I should change the password, when I made it I forgot that I would need to type it into source 
                // code - ali
           c.setAutoCommit(false); 
           System.out.println("Connected to database successfully!");
           
           stmt = c.createStatement();
           ResultSet rs = stmt.executeQuery(query);
           if (rs.next()) {
           cardNum = rs.getString("card_number");
           this.cardNumber = cardNum;
           System.out.println(cardNum);
           }
           if (rs.next()) pin = rs.getString(pin);
           //testing
           if (cardNumber == null) System.out.println("CardNumber, pin combination does not exist");
           else System.out.println("Card number: " + cardNumber + " with pin" + pin + " exists");
           rs.close();
           stmt.close();
           c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": "+e.getMessage());
            System.exit(0);
        }
        return false;
    }

    public boolean withdraw(double amount) {
        //TODO
        return false;
    }

    public boolean deposit(double amount) {
        //TODO
        return false;
    }

    public void reset_authentication() {cardNumber = null;} 

    public double balanceCheck() {return getBalance();}

    public void addCash(double amount) {cash += amount;}

    public double getCash() {return cash;}

    
}
