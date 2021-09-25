/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package ATMSystem;

import org.junit.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

public class AppTest {
    /**
     * test the method of App class
     */

    @Test
    public void appHasAGreeting() {
        App classUnderTest = new App(100000);
        assertNotNull("app should have a greeting", classUnderTest.greetings(10));
        assertNotNull("app should have a greeting", classUnderTest.greetings(14));
        assertNotNull("app should have a greeting", classUnderTest.greetings(18));
        assertNotNull("app should have a greeting", classUnderTest.greetings(28));
    }

    @Test
    public void testAppGetBalance(){
        App app = new App(100000);
        assertNotEquals(app.getAtmBalance(),100000);
    }

    @Test
    public void testAppSetBalance(){
        App app = new App(100000);
        app.setAtmBalance(100);
    }

    @Test
    public void testAddAmount() throws InterruptedException {
        App app = new App(100000);
        app.addAtmCash(100);
    }

    // IO stream is used to testing
    private ByteArrayInputStream testInput;

    private void provideInput(String data){
        testInput = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        System.setIn(testInput);
    }

    @Test
    public void testTimer() throws InterruptedException {
        App app = new App(100000);
        provideInput("Bank");
        assertNotNull("Set a timer of 120s for user's input",  app.timer());
    }

    @Test
    public void testReceipt() throws InterruptedException {
        App app = new App(100000);
        app.receipt("10000", 1000, "Withdraw", 12.0);
        app.receipt("10000", 1000, "Deposit", 12.0);
    }
    /**
     * Test the method of DB class
     */
    @Test
    public void testGetCardNumber(){
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        assertEquals(db.getCardNumber(),"10000");
    }

    @Test
    public void testGetBalance(){
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        assertNotEquals(db.getBalance(), 10000);
    }

    @Test
    public void testGetBlocked(){
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        assertFalse(db.getBlocked());
    }

    @Test
    public void testGetConfiscated(){
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        assertFalse(db.getConfiscated());
    }

    @Test
    public void testSetBlocked(){
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        db.setBlocked(false);
    }

    @Test
    public void testAuthenticate() throws InterruptedException {
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        App app = new App(1000);
        provideInput("System");
        assertFalse(db.authenticate("10000", app.timer()));
    }

    @Test
    public void testIsCardExist(){
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        assertTrue(db.isCardexist("10000"));
    }

    @Test
    public void testIsCardInfoMatch() throws InterruptedException {
        DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
        assertTrue(db.isCardInfoMatch("10000"));
    }

    @Test
    public void Messages() throws InterruptedException {
        App app = new App(10000);
        app.serviceMsg();
        app.depositMsg();
        app.atmMsg();
        app.cardNotexistMsg();
        app.illegalCardMsg();
        app.pinMsg(1);
        app.pinMsg(2);
        app.pinMsg(3);
        app.withdrawInsMsg("10000");
        app.balance("10000");
        app.timeOut();
        app.endTrans();
    }
}
