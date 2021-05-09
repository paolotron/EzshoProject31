package it.polito.ezshop.internalTests;

import it.polito.ezshop.model.JsonRead;
import it.polito.ezshop.model.JsonWrite;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

public class PersistenceTests {
    @Test
    void initialTest() throws FileNotFoundException {
        JsonRead j = new JsonRead();
        j.test();
    }
}
