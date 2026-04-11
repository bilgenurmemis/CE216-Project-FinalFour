package test.Services;
import Services.DataManager;
import core.*;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DataManagerTest {
    private DataManager dataManager;

    @BeforeEach
    void setUp() {
        dataManager = new DataManager();
    }

    @Test
    void testSetupLeagueCreatesExactlyTwentyTeams() {

        List<BaseTeam> teams = dataManager.setupLeague(new FootballSport());
        assertEquals(20, teams.size(), "The league should contain exactly 20 teams.");
    }

    @Test
    void testEachTeamHasExactlyTenPlayers() {

        List<BaseTeam> teams = dataManager.setupLeague(new FootballSport());
        for (BaseTeam team : teams) {
            assertEquals(10, team.getPlayers().size(),
                    "Team " + team.getTeamName() + " must have exactly 10 players.");
        }
    }

    @Test
    void testPolymorphicPlayerCreationForHeadball() {

        List<BaseTeam> teams = dataManager.setupLeague(new HeadballSport());
        BasePlayer firstPlayer = teams.get(0).getPlayers().get(0);

        assertTrue(firstPlayer instanceof HeadballPlayer,
                "Players should be instances of HeadballPlayer when HeadballSport is selected.");
    }

    @Test
    void testPlayerAttributesAreWithinValidRange() {

        List<BaseTeam> teams = dataManager.setupLeague(new FootballSport());
        BasePlayer player = teams.get(0).getPlayers().get(0);

        assertTrue(player.getFitness() >= 0 && player.getFitness() <= 100,
                "Fitness level must be between 0 and 100.");
    }

    @Test
    void testReadLinesHandlesInvalidFileGracefully() {

        List<String> results = dataManager.readLines("non_existent_file.txt");
        assertNotNull(results, "Result should not be null even if the file is missing.");
        assertTrue(results.isEmpty(), "Result should be an empty list for non-existent files.");
    }
}
