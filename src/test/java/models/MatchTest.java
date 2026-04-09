package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void testMatchBaseInitialization() {
        // Arrange
        BaseTeam homeTeam = new BaseTeam("Fenerbahce");
        BaseTeam awayTeam = new BaseTeam("Galatasaray");

        // Act
        Match match = new Match(homeTeam, awayTeam);

        // Assert
        assertEquals(homeTeam, match.getHomeTeam(), "Home team should be correctly assigned.");
        assertEquals(awayTeam, match.getAwayTeam(), "Away team should be correctly assigned.");
        assertEquals(0, match.getHomeScore(), "Initial home score should be 0.");
        assertEquals(0, match.getAwayScore(), "Initial away score should be 0.");
        assertFalse(match.isPlayed(), "Initial played status should be false.");
    }

    @Test
    void testMatchComprehensiveInitialization() {
        // Arrange
        BaseTeam homeTeam = new BaseTeam("Besiktas");
        BaseTeam awayTeam = new BaseTeam("Trabzonspor");
        core.ISport mockSport = new core.FootballSport(); // ISport implementation
        
        // Act
        Match match = new Match(homeTeam, awayTeam, mockSport, 3, "QuarterFinal");

        // Assert
        assertEquals(mockSport, match.getSport(), "Sport interface should be correctly assigned.");
        assertEquals(3, match.getMatchWeek(), "Match week should be correctly assigned.");
        assertEquals("QuarterFinal", match.getRound(), "Match round should be correctly assigned.");
    }

    @Test
    void testUpdateScoreLogic() {
        // Arrange
        BaseTeam homeTeam = new BaseTeam("Anadolu Efes");
        BaseTeam awayTeam = new BaseTeam("Fenerbahce Beko");
        Match match = new Match(homeTeam, awayTeam);

        // Act
        match.updateScore(85, 74);

        // Assert
        assertEquals(85, match.getHomeScore(), "Home score should be updated to 85.");
        assertEquals(74, match.getAwayScore(), "Away score should be updated to 74.");
    }
}
