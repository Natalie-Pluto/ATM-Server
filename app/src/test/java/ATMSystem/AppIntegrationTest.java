package ATMSystem;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AppIntegrationTest {
    DB db = new DB("10000", "postgres", "0000", "jdbc:postgresql://localhost:5433/atmserver");
    private final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    private final ByteArrayOutputStream testErr = new ByteArrayOutputStream();
    private final InputStream systemInput = System.in;
    private final PrintStream systemOutput = System.out;
    private final PrintStream systemErr = System.err;
    //set the testInput and testOutput before testing)

    @BeforeEach
    public void setUpOutput(){
        System.setOut(new PrintStream(testOutput));
        System.setErr(new PrintStream(testErr));

    }

    private void getInput(String data) {
        //sources from https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println/1119559#1119559
        ByteArrayInputStream testInput = new ByteArrayInputStream(data.getBytes());
        System.setIn(testInput);
    }

    private String getOutput() {
        return testOutput.toString();
    }

    //reset the system input/output back
    @AfterEach
    private void reset(){
        System.setIn(systemInput);
        System.setOut(systemOutput);
        System.setErr(systemErr);
    }

    @Test
    public void invalidCardnum() throws InterruptedException {
        setUpOutput();
        getInput("ABC");
        getInput("CLOSE");
        App.main(new String[]{});
        assertNotNull(getOutput());
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
    }


    /*@Test
    public void testRightInput() throws InterruptedException{
        setUpOutput();
        App atmIntegrationTest = new App(10000);
        String[] in = {};
        getInput("10000" + System.lineSeparator() + "1234" + System.lineSeparator() + "2" + System.lineSeparator() + "1000");
        atmIntegrationTest.main(in);
        assertNotNull(getOutput());
        assertEquals("Does not print out expected message", testOutput.toString(), "Good Evening, welcome to XYZ Bank! Please enter your card number:\n" );
        assertEquals("Does not print out expected message", testOutput.toString(), "Please enter the pin:\n");
    }

   @Test
    public void testWrongInput() throws InterruptedException{
        setUpOutput();
        App atmIntegrationTest = new App(10000);

        getInput("11234" + System.lineSeparator() + "10000" + System.lineSeparator() + "1111" + System.lineSeparator() + "1234" + System.lineSeparator() + "1000");
        atmIntegrationTest.main(null);
        assertEquals("Does not print out expected message", testErr.toString(), "Invalid card number. Please enter the card number again:\n" );
        assertEquals("Does not print out expected message", testErr.toString(), "Wrong pin. Please enter again:\n(You have 2 more attempts)\n");
        assertEquals("Does not print out expected message", testErr.toString(), "Please choose a service number list below:\n1.Withdraw    2.Deposit    3.Balance Check    4.Cancel\nIf you want to end the transaction, please choose cancel.");
        assertNotNull(getOutput());
    }

    @Test
    public void testCheckBalanceCancel() throws InterruptedException{
        setUpOutput();
        App atmIntegrationTest = new App(10000);
        getInput("10000" + System.lineSeparator() + "1234" + System.lineSeparator() + "3" + System.lineSeparator() + "4");
        atmIntegrationTest.main(null);
        DecimalFormat dollarFormat = new DecimalFormat("####,###,###.00");
        assertEquals("Balance not match.", testOutput.toString(),("\n\n--------------------------------------------------------------------\nAccount Balance : $" + dollarFormat.format(db.getBalance())));
        assertEquals("Does not print out expected message", testOutput.toString(), "End of transaction...\nEjecting card...\nThank you for using XYZ Bank ATM!Please don't forget to take your card. Looking forward to your next visit.\nReturning to the main page...\n--------------------------------------------------------------------\n");
    }

    @Test
    public void testCheckBalanceWithdraw() throws  InterruptedException{
        setUpOutput();
        App atmIntegrationTest = new App(10000);
        String[] in = {};
        getInput("10000" + System.lineSeparator() + "1234" + System.lineSeparator() + "3" + System.lineSeparator() + "1");
        atmIntegrationTest.main(in);
    }

    @Test
    public void testCheckBalanceDeposit() throws InterruptedException{
        setUpOutput();
        App atmIntegrationTest = new App(10000);
        String[] in = {};
        getInput("10000" + System.lineSeparator() + "1234" + System.lineSeparator() + "3" + System.lineSeparator() + "2");
        atmIntegrationTest.main(in);
    }*/

}
