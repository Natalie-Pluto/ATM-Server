package ATMSystem;

import org.junit.Test;
import static org.junit.Assert.*;

public class DBTests {
    @Test
    public void initOk() {
        //todo
        DB x = new DB("11111", "a_username", "a_password","a_url");
    }

    @Test
    public void authenticateFailure() {
        //todo

    }
}
