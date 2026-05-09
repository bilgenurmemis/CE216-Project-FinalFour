package core;

import models.BaseTeam;
import models.BasePlayer;
import models.Match;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchEngine {

    private final Random random;
    private List<int[]> quarterScores; // her çeyreğin skorları

    public MatchEngine() {
        this.random = new Random();
        this.quarterScores = new ArrayList<>();
    }

    public List<int[]> getQuarterScores() { return quarterScores; }

    public int[] simulateMatch(BaseTeam team1, BaseTeam team2, ISport rules) {
        if (team1.getPlayers().size() < rules.getRequiredPlayers() ||
                team2.getPlayers().size() < rules.getRequiredPlayers()) {
            // Yaralıları iyileştir ve devam et
            team1.getPlayers().forEach(p -> { if (p.isInjured()) p.decreaseInjury(); });
            team2.getPlayers().forEach(p -> { if (p.isInjured()) p.decreaseInjury(); });
        }

        quarterScores = new ArrayList<>();
        double t1Power = calculateTeamPower(team1);
        double t2Power = calculateTeamPower(team2);

        int t1Score = 0;
        int t2Score = 0;
        int periods = rules.getNumberOfPeriods();

        for (int period = 1; period <= periods; period++) {
            int t1PeriodScore;
            int t2PeriodScore;

            if (rules instanceof BasketballSport) {
                t1PeriodScore = 10 + random.nextInt(11) + (int)((t1Power / 100.0) * random.nextInt(5));
                t2PeriodScore = 10 + random.nextInt(11) + (int)((t2Power / 100.0) * random.nextInt(5));
            } else if (rules instanceof FootballSport) {
                t1PeriodScore = (int)(random.nextInt(4) * (t1Power / 100));
                t2PeriodScore = (int)(random.nextInt(4) * (t2Power / 100));
            } else {
                // Headball
                t1PeriodScore = 3 + random.nextInt(8) + (int)((t1Power / 100.0) * random.nextInt(4));
                t2PeriodScore = 3 + random.nextInt(8) + (int)((t2Power / 100.0) * random.nextInt(4));
            }

            t1Score += t1PeriodScore;
            t2Score += t2PeriodScore;
            quarterScores.add(new int[]{t1Score, t2Score});

            // Oyuncu istatistiklerini dağıt
            distributeStats(team1, t1PeriodScore);
            distributeStats(team2, t2PeriodScore);
        }

        // Basketbolda beraberlik olursa uzatma
        if (rules instanceof BasketballSport) {
            while (t1Score == t2Score) {
                t1Score += 5 + random.nextInt(6);
                t2Score += 5 + random.nextInt(6);
            }
        }

        // Sakatlık uygula
        applyRandomInjuries(team1);
        applyRandomInjuries(team2);

        // Sakatları iyileştir (sayaç azalt)
        team1.getPlayers().forEach(BasePlayer::decreaseInjury);
        team2.getPlayers().forEach(BasePlayer::decreaseInjury);

        System.out.println("Match Result: " + team1.getTeamName() + " " + t1Score + " - " + t2Score + " " + team2.getTeamName());

        return new int[]{t1Score, t2Score};
    }

    public void simulateMatch(Match match) {
        int[] scores = simulateMatch(match.getHomeTeam(), match.getAwayTeam(), match.getSport());
        match.updateScore(scores[0], scores[1]);
        match.markAsPlayed();
    }

    // Çeyrek bazlı simülasyon - GUI'den çağrılır
    public int[] simulateQuarter(BaseTeam team1, BaseTeam team2, ISport rules, int quarterNum) {
        double t1Power = calculateTeamPower(team1);
        double t2Power = calculateTeamPower(team2);

        int t1Score;
        int t2Score;

        if (rules instanceof BasketballSport) {
            t1Score = 10 + random.nextInt(11) + (int)((t1Power / 100.0) * random.nextInt(5));
            t2Score = 10 + random.nextInt(11) + (int)((t2Power / 100.0) * random.nextInt(5));
        } else if (rules instanceof FootballSport) {
            t1Score = (int)(random.nextInt(4) * (t1Power / 100));
            t2Score = (int)(random.nextInt(4) * (t2Power / 100));
        } else {
            t1Score = 3 + random.nextInt(8) + (int)((t1Power / 100.0) * random.nextInt(4));
            t2Score = 3 + random.nextInt(8) + (int)((t2Power / 100.0) * random.nextInt(4));
        }

        distributeStats(team1, t1Score);
        distributeStats(team2, t2Score);

        return new int[]{t1Score, t2Score};
    }

    private void distributeStats(BaseTeam team, int totalScore) {
        List<BasePlayer> active = new ArrayList<>();
        for (BasePlayer p : team.getPlayers()) {
            if (!p.isInjured()) active.add(p);
        }
        if (active.isEmpty()) return;

        int remaining = totalScore;
        for (int i = 0; i < active.size() - 1 && remaining > 0; i++) {
            int contribution = random.nextInt(remaining + 1);
            active.get(i).addStatsScore(contribution);
            remaining -= contribution;
        }
        if (remaining > 0) {
            active.get(active.size() - 1).addStatsScore(remaining);
        }
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
        if (!players.isEmpty() && random.nextDouble() < 0.10) {
            int luckyIndex = random.nextInt(players.size());
            BasePlayer injured = players.get(luckyIndex);
            if (!injured.isInjured()) {
                injured.setInjuredGamesRemaining(1 + random.nextInt(3));
            }
        }
    }
}