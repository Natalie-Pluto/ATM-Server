package ATMSystem;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;

public class DB {
	//constants for database connection
	private String db_url;
	private String username;
	private String password; 

	// If cardNumber is null, it means no card is authenticated.
	// This variable is set by the authenticate method.
	private String cardNumber;
	public DB(String cardNumber, String username, String password, String db_url) {
        this.cardNumber = cardNumber;
        setConnectionFields(username, password, db_url);
	}

    public boolean db_connection_test(String username, String password, String db_url) {
        // This method checks whether a db_url, username and password combination enables database connection.
        // True is returned if connection is successful.
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db_url, username, password);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
	
	public <T> boolean sql_update(String column, T value) {
		// Takes column name and value to update column value to.
		// The column value is updated for the row containing
		// the card number that is currently authenticated. If 
		// no card is currently authenticated, false is returned.
		if (this.cardNumber == null) return false;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(db_url, username, password);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE atmserver.\"card\" SET " + column + " = " + value.toString() + " WHERE card_number = '" + cardNumber + "';");
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Boolean sql_getBoolean(String column) {
		// Takes column name and returns the value of the column for row 
		// containing the card number currently authenticated. If no card is 
		// authenticated, then null is returned.
		if (this.cardNumber == null) return null;
		Connection conn = null;
		Boolean output = false;
		String sql = "SELECT * FROM atmserver.\"card\" WHERE card_number = '" + this.cardNumber + "';";
		try {
			conn = DriverManager.getConnection(db_url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			if (result.next()) {
				output = result.getBoolean(column);
			} else return null;
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return output;
	}

	public Double sql_getDouble(String column) {
		// Takes column name and returns the value of the column for row
		// containing the card number currently authenticated. If no card 
		// is authenticated, then null is returned.
		if (this.cardNumber == null) return null;
		Connection conn = null;
		Double output = 0.0;
		String sql = "SELECT * FROM atmserver.\"card\" WHERE card_number = '" + this.cardNumber + "';";
		try {
			conn = DriverManager.getConnection(db_url, username, password);
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			if (result.next()) {
				output = result.getDouble(column);
			} else return null;
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
	public Boolean getConfiscated() {return sql_getBoolean("confiscated");}

	// blocked methods
	public void setBlocked(boolean x) {sql_update("blocked", x);}
	public Boolean getBlocked() {
		return sql_getBoolean("blocked");
	}

	// balance methods
	public void setBalance(double x) {sql_update("balance", x);}
	public Double getBalance() {return sql_getDouble("balance");} 


	//authenticate and set 'cardNumber' field.
	public boolean authenticate(String cardNumber, String pin) {
        //This method checks if the cardNum and pin represent a row in the card table, if yes 
        //then true is returned. If cardNum or pin are invalid or the combination does not exist
        //in the card table, false is returned.

        Connection conn = null;
        String query = "SELECT * FROM atmserver.\"card\" WHERE card_number = '" + cardNumber  + "' AND pin = '" + pin + "';";

        try {
            conn = DriverManager.getConnection(db_url, username, password);
            Statement stmt = conn.createStatement(); 
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                this.cardNumber = cardNumber;
                return true;
            }
            result.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    // Search card in the database
    public boolean isCardexist(String cardNum) {
        Connection conn = null;
        String query = "SELECT * FROM atmserver.\"card\" WHERE card_number = '" + cardNum  + "';";
        try {
            conn = DriverManager.getConnection(db_url, username, password);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                return true;
            }
            result.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean isCardInfoMatch(String cardNum) throws InterruptedException {
        // Current date
        String initialCardNum = this.cardNumber;
        this.cardNumber = cardNum;
        Date currentDate = new Date();
        Connection conn = null;
        String sql = "SELECT * FROM atmserver.\"card\" WHERE card_number = '" + cardNum  + "';";
        Date issueDate = null;
        Date expiredDate = null;
        boolean isMatch = true;
        try {
            conn = DriverManager.getConnection(db_url, username, password);
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);
            if (result.next()) {
				issueDate = result.getDate("issue_date");
				expiredDate = result.getDate("exp_date");
			}
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            this.cardNumber = initialCardNum;
            return false;
        } 

        if(getBlocked()) {
            System.err.println("Sorry, this card is blocked.");
            this.cardNumber = initialCardNum;
            isMatch = false;
        }
        if(getConfiscated()) {
            System.err.println("Sorry, this card is reported stolen or lost.");
            this.cardNumber = initialCardNum;
            isMatch = false;
        }
        if (expiredDate.before(currentDate)) {
            System.err.println("Sorry, this card is expired.");
            this.cardNumber = initialCardNum;
            isMatch = false;
        }

        if (issueDate.after(currentDate)) {
            System.err.println("Sorry, this card is not issued.");
            this.cardNumber = initialCardNum;
            isMatch = false;
        }

        if(!isMatch) {
            msg();
        }

        this.cardNumber = initialCardNum;
        return isMatch;
    }

    public String getDB_Url() {return db_url;}

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public boolean setConnectionFields(String username, String password, String db_url) {
        if (db_connection_test(username, password, db_url)) {
            //The fields are only set if they are valid.
            //If they are invalid, then they must be reassigned, 
            // using this method.
            this.db_url = db_url;
            this.username = username;
            this.password = password;
            return true;
        }
        return false;
    }

    public static void msg() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("Ejecting card...\n");
        Thread.sleep(2000);
        System.out.print("Thank you for using XYZ Bank ATM!\nPlease don't forget to take your card.\nLooking forward to your next visit.\n\n");
        Thread.sleep(3000);
        System.out.println("Returning to the main page...\n");
        System.out.println("--------------------------------------------------------------------");
        System.out.print("\n");
        Thread.sleep(3000);
	}
}

 

