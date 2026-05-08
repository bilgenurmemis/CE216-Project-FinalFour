package core;

import models.BaseTeam;
import models.BasePlayer;
import models.Match;
import java.util.Random;
import java.util.List;

public class MatchEngine {

    private final Random random;

    public MatchEngine() {
        this.random = new Random();
    }

    /**
     * Simulates a match between two generic teams based on provided sports rules.
     * Preserves backwards compatibility for generic simulation calls.
     * 
     * @param team1 Home team
     * @param team2 Away team
     * @param rules Sports rules interface
     * @return Array containing [homeScore, awayScore]
     */
    public int[] simulateMatch(BaseTeam team1, BaseTeam team2, ISport rules) {
        if (team1.getPlayers().size() < rules.getRequiredPlayers() ||
                team2.getPlayers().size() < rules.getRequiredPlayers()) {
            throw new IllegalArgumentException("Not enough players to start the match.");
        }

        double t1Power = calculateTeamPower(team1);
        double t2Power = calculateTeamPower(team2);

        int t1Score = 0;
        int t2Score = 0;

        if (rules instanceof BasketballSport) {
            System.out.println("--- Basketball Match Starting ---");
            for (int quarter = 1; quarter <= 4; quarter++) {
                // Base points between 10-20 + extra points between 0-4 based on team power
                int t1QuarterScore = 10 + random.nextInt(11) + (int)((t1Power / 100.0) * random.nextInt(5));
                int t2QuarterScore = 10 + random.nextInt(11) + (int)((t2Power / 100.0) * random.nextInt(5));

                t1Score += t1QuarterScore;
                t2Score += t2QuarterScore;

                System.out.println("Quarter " + quarter + " Result: " + team1.getTeamName() + " " + t1Score + " - " + t2Score + " " + team2.getTeamName());
            }


            int overtime = 1;
            while (t1Score == t2Score) {
                System.out.println("Match is tied! Playing overtime " + overtime + "...");
                t1Score += 5 + random.nextInt(6);
                t2Score += 5 + random.nextInt(6);
                overtime++;
            }
        } else {

            double scoreMultiplier = rules.getMatchDuration() / 45.0;
            t1Score = (int) (random.nextInt(5) * (t1Power / 100) * scoreMultiplier);
            t2Score = (int) (random.nextInt(5) * (t2Power / 100) * scoreMultiplier);
        }

        applyRandomInjuries(team1);
        applyRandomInjuries(team2);

        System.out.println("Match Result: " + team1.getTeamName() + " " + t1Score + " - " + t2Score + " " + team2.getTeamName());

        return new int[]{t1Score, t2Score};
    }

    /**
     * Simulates a match strictly based on a provided Match object instance.
     * Updates the internal scores and status logically.
     *
     * @param match Match object representing the fixture to be simulated
     */
    public void simulateMatch(Match match) {
        int[] scores = simulateMatch(match.getHomeTeam(), match.getAwayTeam(), match.getSport());
        
        match.updateScore(scores[0], scores[1]);
        match.markAsPlayed();
    }

    private double calculateTeamPower(BaseTeam team) {
        double totalSkill = 0;
        int activePlayers = 0;

        for (BasePlayer player : team.getPlayers()) {
            if (!player.isInjured()) {
                totalSkill += player.getSkillLevel();
                activePlayers++;
            }
        }
        return activePlayers == 0 ? 1.0 : totalSkill / activePlayers;
    }

    private void applyRandomInjuries(BaseTeam team) {
        List<BasePlayer> players = team.getPlayers();
        // 10% chance for a random injury per team
        if (!players.isEmpty() && random.nextDouble() < 0.10) {
            int luckyIndex = random.nextInt(players.size());
            players.get(luckyIndex).setInjured(true);
        }
    }
}