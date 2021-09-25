package ATMSystem;

import org.checkerframework.checker.units.qual.A;
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
    public void invalidCardnum1() throws InterruptedException {
        setUpOutput();
        getInput( "ABC" + System.lineSeparator() + "ABC" + System.lineSeparator() + "ABC" + System.lineSeparator() + "ABC" + System.lineSeparator() + "ABC" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
    }

    @Test
    public void wrongPin() throws InterruptedException {
        setUpOutput();
        getInput( "10000" + System.lineSeparator() + "1" + System.lineSeparator() + "1" + System.lineSeparator() + "j" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
    }

    @Test
    public void invalidCardinfoTest() throws InterruptedException {
        setUpOutput();
        getInput( "99917" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }


/*
    @Test
    public void cardNotExist() throws InterruptedException {
        setUpOutput();
        getInput( "22222"  + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
    }

    @Test
    public void checkBalanceTest() throws InterruptedException {
        setUpOutput();
        getInput( "10485"  + "4455" + "3" + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
    }



    /* @Test
    public void invalidCardnum2() throws InterruptedException {
        setUpOutput();
        getInput( "ABC" + System.lineSeparator() + "efg" + System.lineSeparator() + "123" + System.lineSeparator() + "jjj" + System.lineSeparator() + "1" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }*/
/*
    @Test
    public void cancelTest() throws InterruptedException {
        setUpOutput();
        getInput( "10485" + System.lineSeparator() + "4455" + System.lineSeparator() + "4"  + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }

    @Test
    public void WithdrawTest() throws InterruptedException {
        setUpOutput();
        getInput( "10485" + System.lineSeparator() + "4455" + System.lineSeparator() + "1"  + System.lineSeparator() + "5" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }

    @Test
    public void WithdrawTest2() throws InterruptedException {
        setUpOutput();
        getInput( "10485" + System.lineSeparator() + "4455" + System.lineSeparator() + "1"  + System.lineSeparator() + "50000000" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }

    @Test
    public void DepositTest() throws InterruptedException {
        setUpOutput();
        getInput( "10485" + System.lineSeparator() + "4455" + System.lineSeparator() + "2"  + System.lineSeparator() + "50" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }

    @Test
    public void DepositTest2() throws InterruptedException {
        setUpOutput();
        getInput( "10485" + System.lineSeparator() + "4455" + System.lineSeparator() + "2"  + System.lineSeparator() + "5.5" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }

    @Test
    public void invalidCardinfoTest() throws InterruptedException {
        setUpOutput();
        getInput( "99902" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
    }

    @Test
    public void atmTest() throws InterruptedException {
        App app = new App(10);
        setUpOutput();
        getInput( "10485" + System.lineSeparator() + "4455" + System.lineSeparator() + "1"  + System.lineSeparator() + "500" + System.lineSeparator() + "CLOSE");
        App.main(null);
        assertNotNull(getOutput());
        reset();
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
