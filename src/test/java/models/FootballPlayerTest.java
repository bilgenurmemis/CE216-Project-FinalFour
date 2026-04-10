package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FootballPlayerTest {
    private FootballPlayer player;

    @BeforeEach
    void setUp(){
        player = new FootballPlayer("Arda Guler", 19, 80.0);
    }

    @Test
    void testTrainIncreasesFitness(){
        double initialFitness = player.getFitness();
        player.train();
        assertTrue(player.getFitness() > initialFitness || player.getFitness() == 100, "Training should increase fitness.");
    }

    @Test
    void testInjuredPlayerCannotTrain(){
        player.setInjured(true);
        double initialFitness = player.getFitness();
        player.train();
        assertEquals(initialFitness, player.getFitness(), "Injured player fitness should not change during training.");
    }

    @Test
    void testRecoverIncreasesFitness(){
        player = new FootballPlayer("Semih", 19, 40.0);
        player.recover();
        assertEquals(55.0, player.getFitness(),"Recovering should add 15 fitness points.");
    }

    @Test
    void testRecoveryHealsInjuryIfFitnessHigh(){
        player.setInjured(true);
        player.recover();
        assertFalse(player.isInjured(), "Player should heal if fitness goes above 60.");
    }

    @Test
    void testGetSkillLevelDropsWhenInjured(){
        int healthySkill = player.getSkillLevel();
        player.setInjured(true);
        int injuredSkill = player.getSkillLevel();
        assertTrue(injuredSkill < healthySkill, "Skill level should drop significantly when injured.");
    }
}
