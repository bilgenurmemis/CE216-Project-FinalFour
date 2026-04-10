package models;

import core.ISport;

import java.util.ArrayList;
import java.util.List;

public class League {
    private List<BaseTeam> teams;
    private List<Match> fixtures;
    private ISport sport;

    public League(ISport sport) {
        this.sport = sport;
        this.teams = new ArrayList<>();
        this.fixtures = new ArrayList<>();
    }

    /**
     * Adds a new team to the league.
     * 
     * @param team The team to be added
     */
    public void addTeam(BaseTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    /**
     * Generates a double round-robin fixture for all teams in the league.
     * Each team plays every other team exactly twice, once home and once away.
     */
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

    /**
     * Updates the league standings based on the outcome of a played match.
     * Points are dynamically distributed according to the assigned ISport rules.
     * 
     * @param match The match that was played
     */
    public void updateStandings(Match match) {
        if (!match.isPlayed()) {
            return;
        }

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

    /**
     * Retrieves the current league standings sorted by total points in descending order.
     * 
     * @return List of teams sorted by points
     */
    public List<BaseTeam> getStandings() {
        List<BaseTeam> sortedTeams = new ArrayList<>(teams);
        sortedTeams.sort((t1, t2) -> Integer.compare(t2.getPoints(), t1.getPoints()));
        return sortedTeams;
    }

    public List<BaseTeam> getTeams() {
        return teams;
    }

    public List<Match> getFixtures() {
        return fixtures;
    }

    public ISport getSport() {
        return sport;
    }

    public void setSport(ISport sport) {
        this.sport = sport;
    }
}
