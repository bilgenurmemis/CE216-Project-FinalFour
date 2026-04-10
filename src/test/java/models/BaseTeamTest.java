package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaseTeamTest {
    private BaseTeam team;

    @BeforeEach
    void setUp() {
        team = new BaseTeam("Fenerbahce");
    }

    @Test
    void testAddPoints() {
        team.addPoints(3);
        team.addPoints(1);
        assertEquals(4, team.getPoints(), "Points should accumulate correctly.");
    }

    @Test
    void testAddPlayerEnforcesLimit() {
        assertTrue(team.addPlayer(new FootballPlayer("P1", 20, 80), 2));
        assertTrue(team.addPlayer(new FootballPlayer("P2", 21, 80), 2));
        assertFalse(team.addPlayer(new FootballPlayer("P3", 22, 80), 2), "Should not add player if limit is reached.");
    }

    @Test
    void testGetTeamStrengthWithNoPlayers() {
        assertEquals(0, team.getTeamStrength(), "Empty team should have 0 strength.");
    }

    @Test
    void testGetTeamStrengthCalculatesAverage() {
        team.addPlayer(new FootballPlayer("P1", 20, 100));
        team.addPlayer(new FootballPlayer("P2", 20, 40));
        assertTrue(team.getTeamStrength() > 0, "Team strength should be an average of player skills.");
    }
}