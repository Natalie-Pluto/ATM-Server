package ATMSystem;

import org.junit.Test;
import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
import java.sql.*;
import java.util.Date;

public class DBTests {
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

    

}
