package Services;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyFileTest {
    @Test
    void testReadInvalidFile() {
        DataManager dm = new DataManager();

        List<String> lines = dm.readLines("missing_file.txt");
        assertNotNull(lines, "The method should return an empty list, not null.");
    }
}
