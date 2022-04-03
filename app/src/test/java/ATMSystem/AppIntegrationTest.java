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

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class AppIntegrationTest {
    private final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    private final ByteArrayOutputStream testErr = new ByteArrayOutputStream();
    private final InputStream systemInput = System.in;
    private final PrintStream systemOutput = System.out;
    private final PrintStream systemErr = System.err;

    //set the testInput and testOutput before testing)

    @BeforeEach
    public void setUpOutput() {
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

    private String getErr() {
        return testErr.toString();
    }

    //reset the system input/output back
    @AfterEach
    private void reset() {
        System.setIn(systemInput);
        System.setOut(systemOutput);
        System.setErr(systemErr);
    }

    @Test
    public void invalidCardnum1() throws InterruptedException {
        App app = new App(100000);
        setUpOutput();
        getInput("123" + System.lineSeparator() + "456" + System.lineSeparator() + "789" + System.lineSeparator() + "101" + System.lineSeparator() + "123" + System.lineSeparator() + "CLOSE");
        app.illegalCardMsg();
        App.main(null);
        assertNotNull(getOutput());
    }

    @Test
    public void wrongPin() throws InterruptedException {
        setUpOutput();
        getInput("10000" + System.lineSeparator() + "1" + System.lineSeparator() + "1" + System.lineSeparator() + "j" + System.lineSeparator() + "CLOSE");
        DB db = new DB("10000", "dbmasteruser", "A>XV>D*7r-V{y_wL}}I{+U=8zEtj1*T<",
                "jdbc:postgresql://ls-d4381878930280384f33af335289e24c73224a04.c0apyqxz8x8m.ap-southeast-2.rds.amazonaws.com:5432/postgres");
        db.setBlocked(false);
        App.main(null);
        assertNotNull(getOutput());
    }

    @Test
    public void invalidCardinfoTest() throws InterruptedException {
        App app = new App(100000);
        setUpOutput();
        getInput("99917" + System.lineSeparator() + "CLOSE");
        App.main(null);
        app.printGreetings();
        assertNotNull(getOutput());
        reset();
    }

    @Test
    public void checkBalanceTest() throws InterruptedException {
        setUpOutput();
        getInput("10485" + System.lineSeparator() + "4455" + System.lineSeparator() + "3" + System.lineSeparator() + "CLOSE");
        DB db = new DB("10000", "dbmasteruser", "A>XV>D*7r-V{y_wL}}I{+U=8zEtj1*T<",
                "jdbc:postgresql://ls-d4381878930280384f33af335289e24c73224a04.c0apyqxz8x8m.ap-southeast-2.rds.amazonaws.com:5432/postgres");
        db.setBlocked(false);
        App.main(null);
        assertNotNull(getOutput());
    }

    @Test
    public void cardNotExist() throws InterruptedException {
        App app = new App(100000);
        setUpOutput();
        getInput("22222" + System.lineSeparator() + "CLOSE");
        app.cardNotexistMsg();
        App.main(null);
        assertNotNull(getErr());
    }

    @Test
    public void cardexistException() throws InterruptedException {
        DB db = new DB("22222", "dbmasteruser2", "A>XV>D*7r-V{y_wL}}I{+U=8zEtjj1*TT<",
                "jdbc:postgresql://ls-d4381878930280384f33af335289e24c73224a04.c0apyqxz8x8m.ap-southeast-2.rds.amazonaws.com:5432/postgres");
        assertFalse(db.isCardexist("22222"));
        assertFalse(db.isCardInfoMatch("22222"));
    }
}
