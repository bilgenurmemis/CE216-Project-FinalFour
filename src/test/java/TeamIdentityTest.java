package test.Services;

import core.FootballSport;
import models.BaseTeam;
import Services.DataManager;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TeamIdentityTest {
    @Test
    void testFirstTeamName() {
        DataManager dm = new DataManager();
        List<BaseTeam> teams = dm.setupLeague(new FootballSport());

        if (teams != null && !teams.isEmpty()) {
            String name = teams.get(0).getTeamName();
            assertNotNull(name, "The team name should not be null.");
        }
    }
}
