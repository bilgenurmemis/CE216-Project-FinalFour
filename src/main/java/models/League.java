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
    }

    public List<BaseTeam> getStandings() {
        List<BaseTeam> sortedTeams = new ArrayList<>(teams);
        sortedTeams.sort((t1, t2) -> {
            int pointDiff = Integer.compare(t2.getPoints(), t1.getPoints());
            if (pointDiff != 0) return pointDiff;
            return t1.getTeamName().compareTo(t2.getTeamName());
        });
        return sortedTeams;
    }

    public List<BaseTeam> getTeams() { return teams; }
    public List<Match> getFixtures() { return fixtures; }
    public ISport getSport() { return sport; }
    public void setSport(ISport sport) { this.sport = sport; }
}