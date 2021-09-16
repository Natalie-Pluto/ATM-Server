package ATMSystem;
import java.sql.*;

public class DB {
	static final String DB_URL = "localhost";
	static final String USERNAME = "alien";
	static final String PASSWORD = "fuck"; 

	private String cardNumber; // If cardNumber is null, it means no card is authenticated.	
	
	private boolean sql_update(String query) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query); 
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String sql_select() {
		return null;
	}
	
	public String getCardNumber() {return this.cardNumber;}
	public void confiscate() {} //TODO
	public boolean block() {
		return sql_update("UPDATE atmserver.\"Card\" SET blocked = true WHERE card_number = '" + cardNumber + ";");
	} 
	public boolean unblock() {
		return sql_update("UPDATE atmserver.\"Card\" SET blocked = true WHERE card_number = '" + cardNumber + ";");
	}
	public void subtractBalance(double x) {} //TODO
	public void addBalance(double x) {} //TODO
	public double getBalance() {return 0;} //TODO
	public boolean authenticate(String cardNumber, String pin) {return false;} //TODO

		
}
 