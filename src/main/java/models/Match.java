package models;

import core.ISport;

public class Match {
    private BaseTeam homeTeam;
    private BaseTeam awayTeam;
    private int homeScore;
    private int awayScore;
    private boolean isPlayed;

    private int matchWeek;
    private String round;
    private ISport sport;

    public Match(BaseTeam homeTeam, BaseTeam awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.isPlayed = false;
    }

    public Match(BaseTeam homeTeam, BaseTeam awayTeam, ISport sport, int matchWeek, String round) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.sport = sport;
        this.matchWeek = matchWeek;
        this.round = round;
        this.homeScore = 0;
        this.awayScore = 0;
        this.isPlayed = false;
    }

    public BaseTeam getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(BaseTeam homeTeam) {
        this.homeTeam = homeTeam;
    }

    public BaseTeam getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(BaseTeam awayTeam) {
        this.awayTeam = awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public int getMatchWeek() {
        return matchWeek;
    }

    public void setMatchWeek(int matchWeek) {
        this.matchWeek = matchWeek;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public ISport getSport() {
        return sport;
    }

    public void setSport(ISport sport) {
        this.sport = sport;
    }

    /**
     * Updates the score of the match.
     * 
     * @param homeTeamScore The score of the home team
     * @param awayTeamScore The score of the away team
     */
    public void updateScore(int homeTeamScore, int awayTeamScore) {
        this.homeScore = homeTeamScore;
        this.awayScore = awayTeamScore;
    }

    /**
     * Marks the match as completed.
     */
    public void markAsPlayed() {
        this.isPlayed = true;
    }
}
