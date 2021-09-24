package ATMSystem;

import org.junit.Test;
import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
import java.sql.*;
import java.util.Date;

public class DBTests {
    //The test descriptions are contained in DBTests.txt.

    //good (valid) args
    public static String db_url = "jdbc:postgresql://localhost:5432/a12412";
    public static String username = "alien";
    public static String password = "fuck";
    public static String pin = "0000";  
    public static String cardNumber = "11111";

    //bad args
    public static String bad_db_url = "bad_url";
    public static String bad_username = "23457890";
    public static String bad_password = "123467890";
    public static String bad_cardNumber = "2345333";
    public static String bad_pin = "k33j";

    @Test
    public void initOk_A1() {
        // this test checks if a DB object can be initialised without error.
        DB x = new DB(cardNumber,username,password,db_url);
    }

    @Test
    public void initBadArgs_A2() {
        // this test checks that if bad database connection args are
        // passed into a DB object at initialisation, the bad connection 
        // args are not assigned to the fields of the instance.
        DB x = new DB(cardNumber,bad_username,bad_password,db_url);
        assertEquals(x.getDB_Url(),null);
        assertEquals(x.getUsername(),null);
        assertEquals(x.getPassword(),null);
    }

    @Test
    public void initGoodArgs_A3() {
        // this test checks that if good database connection args are
        // passed into a DB object at initialisation, the good connection 
        // args are assigned fields in the object.
        DB x = new DB(cardNumber, username, password, db_url);  
        assertEquals(x.getUsername(),username);
        assertEquals(x.getPassword(),password);
        assertEquals(x.getDB_Url(),db_url);
    }

    @Test
    public void failedAuthentication_B1() {
        DB x = new DB(cardNumber, username, password, db_url); 
        assertFalse(x.authenticate(bad_cardNumber, pin));
        assertFalse(x.authenticate(bad_cardNumber, bad_pin));
        assertFalse(x.authenticate(cardNumber, bad_pin));
    }

    @Test
    public void successfulAuthentication_B2() {
        DB x = new DB(cardNumber, username, password, db_url);
        assertEquals(x.getUsername(),username);
        assertTrue(x.authenticate(cardNumber,pin));
    }

    @Test
    public void numericTypeUpdate_C1() {
        DB x = new DB(cardNumber, username, password, db_url);
        Double initialBal = x.sql_getDouble("balance");
        x.sql_update("balance",initialBal - 1);
        assertTrue(x.sql_getDouble("balance") == initialBal - 1);
        x.sql_update("balance",initialBal);
    }

    @Test 
    public void booleanTypeUpdate_C2() {
        DB x = new DB(cardNumber, username, password, db_url);
        boolean initialConfiscatedValue = x.sql_getBoolean("confiscated");
        x.sql_update("confiscated", !initialConfiscatedValue);
        assertEquals(x.sql_getBoolean("confiscated"),!initialConfiscatedValue);
        x.sql_update("confiscated",initialConfiscatedValue);
    }
    
    @Test
    public void getBoolean_D1() {
        DB x = new DB(cardNumber, username, password, db_url);
        Boolean bool = x.sql_getBoolean("confiscated");
        assertTrue(bool != null);
    }

    @Test 
    public void getBooleanFailure_D2() {
        DB x = new DB(cardNumber, bad_username, password, bad_db_url);
        Boolean bool = x.sql_getBoolean("confiscated");
        assertEquals(bool,null);
    }

    @Test
    public void getDouble_E1() {
        DB x = new DB(cardNumber, username, password, db_url);
        Double num = x.sql_getDouble("balance");
        assertTrue(num != null);
    }

    @Test
    public void getDoubleFailure_E2() {
        DB x = new DB(cardNumber, username, password, bad_db_url);
        Double num = x.sql_getDouble("balance");
        assertTrue(num == null);
    }

    @Test
    public void getCardNumber_F1() {
        DB x = new DB(cardNumber, username, password, bad_db_url);
        assertEquals(x.getCardNumber(),cardNumber);
    }

    @Test
    public void setConfiscatedAuthenticated_G1() {
        DB x = new DB(cardNumber, username, password, db_url);
        Boolean initialConfiscatedVal = x.sql_getBoolean("confiscated");
        x.setConfiscated(!initialConfiscatedVal);
        assertEquals(x.sql_getBoolean("confiscated"),!initialConfiscatedVal);
        x.setConfiscated(initialConfiscatedVal);
    }

    @Test
    public void setConfiscatedNotAuthenticated_G2() {
        DB x = new DB(bad_cardNumber, username, password, db_url);
        x.setConfiscated(true);
    }

    @Test
    public void getConfiscatedAuthenticated() {
        DB x = new DB(cardNumber, username, password, db_url);
        Boolean confiscatedVal = x.getConfiscated();
        assertTrue(confiscatedVal != null);
    }

    @Test
    public void getConfiscatedNotAuthenticated() {
        DB x = new DB(bad_cardNumber, username, password, db_url);
        Boolean confiscatedVal = x.getConfiscated();
        assertTrue(confiscatedVal == null);
    }

    @Test
    public void getBalanceAuthenticated_H1() {
        DB x = new DB(cardNumber, username, password, db_url);
        Double bal = x.getBalance();
        assertTrue(bal != null);
    }

    @Test
    public void getBalanceNotAuthenticated_H2() {
        DB x = new DB(bad_cardNumber, username, password, db_url);
        Double bal = x.getBalance();
        System.out.print("bal: ");
        System.out.println(bal);
        assertTrue(bal == null);
    }

    @Test
    public void setBalanceNotAuthenticated_H3() {
        DB x = new DB(bad_cardNumber, username, password, db_url);
        x.setBalance(10);
    }

    @Test
    public void setBalanceAuthenticated_H4() {
        DB x = new DB(cardNumber, username, password, db_url);
        Double bal = x.sql_getDouble("balance");
        x.setBalance(bal - 1);
        assertTrue(x.getBalance() ==bal - 1);
        x.setBalance(bal);
    }

    

}
