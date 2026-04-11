package test.Services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class DataValidationTest {
    @Test
    void testFileExistence() {
        assertTrue(new File("teams.txt").exists());
        assertTrue(new File("players.txt").exists());
    }
}
