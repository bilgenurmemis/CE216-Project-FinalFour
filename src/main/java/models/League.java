package models;

import core.ISport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class League implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<BaseTeam> teams;
    private List<Match> fixtures;
    private ISport sport;

    public League(ISport sport) {
        this.sport = sport;
        this.teams = new ArrayList<>();
        this.fixtures = new ArrayList<>();
    }

    public void addTeam(BaseTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void generateFixtures() {
        fixtures.clear();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                BaseTeam teamA = teams.get(i);
                BaseTeam teamB = teams.get(j);
                Match firstLeg = new Match(teamA, teamB, sport, 1, "First Leg");
                Match secondLeg = new Match(teamB, teamA, sport, 2, "Second Leg");
                fixtures.add(firstLeg);
                fixtures.add(secondLeg);
            }
        }
    }

    public List<List<Match>> getWeeklyFixtures() {
        List<List<Match>> weeks = new ArrayList<>();
        int matchesPerWeek = teams.size() / 2;

        List<Match> allMatches = new ArrayList<>(fixtures);

        while (!allMatches.isEmpty()) {
            List<Match> week = new ArrayList<>();
            List<BaseTeam> usedTeams = new ArrayList<>();

            for (Match m : new ArrayList<>(allMatches)) {
                if (!usedTeams.contains(m.getHomeTeam()) &&
                        !usedTeams.contains(m.getAwayTeam())) {
                    week.add(m);
                    usedTeams.add(m.getHomeTeam());
                    usedTeams.add(m.getAwayTeam());
                }
                if (week.size() == matchesPerWeek) break;
            }
            allMatches.removeAll(week);
            weeks.add(week);
        }
        return weeks;
    }

    public void updateStandings(Match match) {
        if (!match.isPlayed()) return;
        BaseTeam homeTeam = match.getHomeTeam();
        BaseTeam awayTeam = match.getAwayTeam();
        int homeScore = match.getHomeScore();
        int awayScore = match.getAwayScore();
        int winPoint = (sport != null) ? sport.getPointForWin() : 3;
        int drawPoint = (sport != null) ? sport.getPointForDraw() : 1;
        if (homeScore > awayScore) {
            homeTeam.addPoints(winPoint);
        } else if (awayScore > homeScore) {
            awayTeam.addPoints(winPoint);
        } else {
            homeTeam.addPoints(drawPoint);
            awayTeam.addPoints(drawPoint);
        }
        homeTeam.updateStatus(homeScore, awayScore);
        awayTeam.updateStatus(awayScore, homeScore);
    }

    public List<BaseTeam> getStandings() {
        List<BaseTeam> sortedTeams = new ArrayList<>(teams);
        sortedTeams.sort((t1, t2) -> {
            int pointDiff = Integer.compare(t2.getPoints(), t1.getPoints());
            if (pointDiff != 0) return pointDiff;
            return Integer.compare(t2.getAverage(), t1.getAverage());
        });
        return sortedTeams;
    }

    public List<BaseTeam> getTeams() { return teams; }
    public List<Match> getFixtures() { return fixtures; }
    public ISport getSport() { return sport; }
    public void setSport(ISport sport) { this.sport = sport; }
}