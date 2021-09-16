package ATMSystem;
import java.sql.*;

public class DB {
	private String cardNumber; // If cardNumber is null, it means no card is authenticated.	
	
	private String query() {
		//TODO 
		return null;
	}
	
	public String getCardNumber() {return this.cardNumber;}
	public void confiscate() {} //TODO
	public void block() {} //TODO
	public void subtractBalance(double x) {} //TODO
	public void addBalance(double x) {} //TODO
	public double getBalance() {return 0;} //TODO
	public boolean authenticate(String cardNumber, String pin) {return false;} //TODO
		
}
