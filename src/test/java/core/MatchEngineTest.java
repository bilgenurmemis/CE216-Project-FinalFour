package core;

import models.BasePlayer;
import models.BaseTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchEngineTest {

    private MatchEngine engine;
    private ISport dummySport;

    @BeforeEach
    void setUp() {
        engine = new MatchEngine();
        dummySport = new ISport() {
            @Override
            public int getRequiredPlayers() {
                return 2;
            }
            @Override
            public int getSquadSize() { return 5; }

            @Override
            public int getMatchDuration() {
                return 90;
            }

            @Override
            public int getPointForWin() {
                return 3;
            }

            @Override
            public int getPointForDraw() {
                return 1;
            }
        };
    }

    @Test
    void testSimulateMatchWithEnoughPlayers() {
        BaseTeam teamA = new BaseTeam("HomeTeam");
        BaseTeam teamB = new BaseTeam("AwayTeam");

        for (int i = 0; i < 3; i++) {
            teamA.addPlayer(new BasePlayer("PlayerA" + i, 20, 85.0) {
                public void train() {
                }

                public void recover() {
                }

                public int getSkillLevel() {
                    return 70;
                }
            });
            teamB.addPlayer(new BasePlayer("PlayerB" + i, 20, 85.0) {
                public void train() {
                }

                public void recover() {
                }

                public int getSkillLevel() {
                    return 70;
                }
            });
        }

        int[] result = assertDoesNotThrow(() -> engine.simulateMatch(teamA, teamB, dummySport));
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test
    void testSimulateMatchThrowsExceptionWhenNotEnoughPlayers() {
        BaseTeam teamA = new BaseTeam("EmptyTeamA");
        BaseTeam teamB = new BaseTeam("EmptyTeamB");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engine.simulateMatch(teamA, teamB, dummySport);
        });

        assertTrue(exception.getMessage().contains("Not enough players"));
    }
}
