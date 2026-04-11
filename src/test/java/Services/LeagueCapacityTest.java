package Services;

import core.*;
import models.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class LeagueCapacityTest {
    @Test
    void testTotalPlayerCountInLeague() {
        DataManager dm = new DataManager();
        List<BaseTeam> teams = dm.setupLeague(new FootballSport());
        int totalPlayers = 0;
        for (BaseTeam team : teams) {
            totalPlayers += team.getPlayers().size();
        }
        assertEquals(200, totalPlayers, "Total players should be 200.");
    }
}
