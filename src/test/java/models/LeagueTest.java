package models;

import core.ISport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeagueTest {

    private League league;
    private ISport mockSport;
    private BaseTeam t1, t2, t3, t4;

    @BeforeEach
    void setUp() {
        // Prepare the test environment
        mockSport = new ISport() {
            public int getRequiredPlayers() { return 11; }
            public int getMatchDuration() { return 90; }
            public int getPointForWin() { return 3; }
            public int getPointForDraw() { return 1; }
        };
        league = new League(mockSport);
        
        t1 = new BaseTeam("Fenerbahce");
        t2 = new BaseTeam("Galatasaray");
        t3 = new BaseTeam("Besiktas");
        t4 = new BaseTeam("Trabzonspor");
    }

    @Test
    void testGenerateFixturesTotalMatchCount() {
        league.addTeam(t1);
        league.addTeam(t2);
        league.addTeam(t3);
        league.addTeam(t4);
        
        league.generateFixtures();
        
        // For a generic double round-robin format with N teams, total matches should be N * (N - 1)
        // Expected outcome for 4 teams: 4 * 3 = 12
        assertEquals(12, league.getFixtures().size(), "Total match count should equal N*(N-1).");
    }

    @Test
    void testNoSelfPlayInFixtures() {
        league.addTeam(t1);
        league.addTeam(t2);
        league.addTeam(t3);
        league.generateFixtures();
        
        for (Match match : league.getFixtures()) {
            assertNotEquals(match.getHomeTeam(), match.getAwayTeam(), "A team cannot play against itself.");
        }
    }

    @Test
    void testUpdateStandingsHomeWin() {
        league.addTeam(t1);
        league.addTeam(t2);
        
        Match match = new Match(t1, t2, mockSport, 1, "Round 1");
        match.updateScore(3, 1); 
        match.markAsPlayed();
        
        league.updateStandings(match);
        
        assertEquals(3, t1.getPoints(), "Winning home team should receive 3 points.");
        assertEquals(0, t2.getPoints(), "Losing away team should receive 0 points.");
    }

    @Test
    void testStandingsSortingOrder() {
        league.addTeam(t1); // 0 points
        league.addTeam(t2); // 6 points
        league.addTeam(t3); // 3 points
        
        t2.addPoints(6); 
        t3.addPoints(3);
        
        List<BaseTeam> table = league.getStandings();
        
        assertEquals(t2, table.get(0), "Team with the highest points should be at index 0.");
        assertEquals(t3, table.get(1), "Team with the second highest points should be at index 1.");
        assertEquals(t1, table.get(2), "Team with the lowest points should be at the bottom.");
    }

    @Test
    void testUpdateStandingsDraw() {
        league.addTeam(t1);
        league.addTeam(t2);
        
        Match match = new Match(t1, t2, mockSport, 1, "R1");
        match.updateScore(2, 2); 
        match.markAsPlayed();
        
        league.updateStandings(match);
        
        assertEquals(1, t1.getPoints(), "Home team should receive 1 point for a draw.");
        assertEquals(1, t2.getPoints(), "Away team should receive 1 point for a draw.");
    }

    @Test
    void testNoDuplicateFixtures() {
        league.addTeam(t1);
        league.addTeam(t2);
        league.addTeam(t3);
        league.generateFixtures();
        
        List<Match> fixtures = league.getFixtures();
        for (int i = 0; i < fixtures.size(); i++) {
            for (int j = i + 1; j < fixtures.size(); j++) {
                Match m1 = fixtures.get(i);
                Match m2 = fixtures.get(j);
                
                boolean isDuplicate = m1.getHomeTeam().equals(m2.getHomeTeam()) && m1.getAwayTeam().equals(m2.getAwayTeam());
                assertFalse(isDuplicate, "Fixture list should not contain identical matchups.");
            }
        }
    }
}
