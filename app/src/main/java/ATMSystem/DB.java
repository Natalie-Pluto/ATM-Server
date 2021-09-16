package ATMSystem;
import java.sql.*;

public class DB {
	//constants for database connection
	private String db_url;
	private String username;
	private String password; 

	// If cardNumber is null, it means no card is authenticated.
	// This variable is set by the authenticate method.
	private String cardNumber; 

	public DB(String username, String password, String db_url) {
		this.username = username;
		this.password = password;
		this.db_url = db_url;
	}
	
	private <T> boolean sql_update(String column, T value) {
		// Takes column name and value to update column value to.
		// The column value is updated for the row containing
		// the card number that is currently authenticated. If 
		// no card is currently authenticated, false is returned.
		if (this.cardNumber == null) return false;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(db_url, username, password);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE atmserver.\"Card\" SET " + column + " = " + value.toString() + " WHERE card_number = '" + cardNumber + ";"); 
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Boolean sql_getBoolean(String column) {
		// Takes column name and returns the value of the column for row 
		// containing the card number currently authenticated. If no card is 
		// authenticated, then null is returned.
		if (this.cardNumber == null) return null;
		Connection conn = null;
		Boolean output = false;
		String sql = "SELECT * FROM atmserver.\"Card\" WHERE card_number = '" + this.cardNumber;
		try {
			conn = DriverManager.getConnection(db_url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			assert (result.next()); 
			output = result.getBoolean(column);
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return output;
	}

	

	private Double sql_getDouble(String column) {
		// Takes column name and returns the value of the column for row
		// containing the card number currently authenticated. If no card 
		// is authenticated, then null is returned.
		if (this.cardNumber == null) return null;
		Connection conn = null;
		Double output = 0.0;
		String sql = "SELECT * FROM atmserver.\"Card\" WHERE card_number = '" + this.cardNumber + "';";
		try {
			conn = DriverManager.getConnection(db_url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			assert (result.next()); 
			output = result.getDouble(column);
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return output;
	}
	
	// cardNumber methods
	public String getCardNumber() {return this.cardNumber;}

	// confiscated methods
	public void setConfiscated(boolean x) {sql_update("confiscated", x);}
	public boolean getconfiscated() {return sql_getBoolean("confiscated");}

	// blocked methods
	public void setBlocked(boolean x) {sql_update("blocked", x);}
	public boolean getBlocked() {return sql_getBoolean("blocked");}

	// balance methods
	public void setBalance(double x) {sql_update("balance", x);}
	public double getBalance() {return sql_getDouble("balance");} 

	//authenticate and set 'cardNumber' field.
	public boolean authenticate(String cardNumber, String pin) {

		/*
        This method checks if the cardNum and pin represent a row in the card table, if yes 
        then true is returned. If cardNum or pin are invalid or the combination does not exist
        in the card table, false is returned.
        */
       
        Connection conn = null;
        String query = "SELECT * FROM atmserver.\"Card\" WHERE card_number = '" + cardNumber  + "' AND pin = '" + pin + "';";

        try {
        	conn = DriverManager.getConnection(db_url, username, password); 
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
			if (result.next()) this.cardNumber = cardNumber;
            result.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return false;
	} 
}
 