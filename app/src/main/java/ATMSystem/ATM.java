package ATMSystem;

public class ATM {
    //private fields
    private double cash;
    boolean authenticated;

    // constructor
    public ATM(double startingCash) {cash = startingCash;}
    
    private double getBalance() {
        //TODO
        return 0;
    }
    
    private void block() {
        //TODO
    }

    private void confiscate() {
        //TODO
    }

    private boolean changeBalance() {
        //TODO
        return false;
    }

    public boolean authentication(int cardNum, int pin) {
        //TODO
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

    public double balanceCheck() {return getBalance();}

    public void addCash(double amount) {cash += amount;}

    public double getCash() {return cash;}

    
}
