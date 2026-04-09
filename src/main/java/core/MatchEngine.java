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

    public void simulateMatch(Match match) {
        BaseTeam team1 = match.getHomeTeam();
        BaseTeam team2 = match.getAwayTeam();
        ISport rules = match.getSport();

        if (team1.getPlayers().size() < rules.getRequiredPlayers() ||
                team2.getPlayers().size() < rules.getRequiredPlayers()) {
            throw new IllegalArgumentException("Not enough players to start the match.");
        }

        double t1Power = calculateTeamPower(team1);
        double t2Power = calculateTeamPower(team2);

        double scoreMultiplier = rules.getMatchDuration() / 45.0;
        int t1Score = (int) (random.nextInt(5) * (t1Power / 100) * scoreMultiplier);
        int t2Score = (int) (random.nextInt(5) * (t2Power / 100) * scoreMultiplier);

        applyRandomInjuries(team1);
        applyRandomInjuries(team2);

        // Sonuçları Match objesine işliyoruz ve maçı oynandı (isPlayed = true) yapıyoruz
        match.updateScore(t1Score, t2Score);
        match.markAsPlayed();

        System.out.println("Match Played: " + team1.getTeamName() + " " + t1Score + " - " + t2Score + " " + team2.getTeamName());
    }

    private double calculateTeamPower(BaseTeam team) {
        double totalFitness = 0;
        int activePlayers = 0;

        for (BasePlayer player : team.getPlayers()) {
            if (!player.isInjured()) {
                totalFitness += player.getFitness();
                activePlayers++;
            }
        }
        return activePlayers == 0 ? 1.0 : totalFitness / activePlayers;
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