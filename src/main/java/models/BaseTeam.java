package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseTeam implements Serializable {
    private static final long serialVersionUID = 1L;

    private String teamName;
    private List<BasePlayer> players;
    private List<BasePlayer> starters;
    private List<BasePlayer> substitutes;
    private int points;
    private int scoredPoints = 0;
    private int concededPoints = 0;
    private String coach;

    public BaseTeam(String teamName) {
        this.teamName = teamName;
        this.players = new ArrayList<>();
        this.starters = new ArrayList<>();
        this.substitutes = new ArrayList<>();
        this.points = 0;
    }

    public String getTeamName() { return teamName; }
    public void addPlayer(BasePlayer player) { this.players.add(player); }
    public List<BasePlayer> getPlayers() { return players; }
    public int getPoints() { return points; }
    public void addPoints(int earnedPoints) { this.points += earnedPoints; }

    public int getTeamStrength() {
        if (players.isEmpty()) return 0;
        int total = 0;
        for (BasePlayer p : players) {
            total += p.getSkillLevel();
        }
        return total / players.size();
    }

    public boolean addPlayer(BasePlayer player, int maxPlayers) {
        if (players.size() >= maxPlayers) {
            System.out.println("Team is full!");
            return false;
        }
        players.add(player);
        return true;
    }

    public void setCoach(String coach) { this.coach = coach; }
    public String getCoach() { return this.coach; }

    public int getAverage() {
        return scoredPoints - concededPoints;
    }

    public void updateStatus(int scored, int conceded) {
        this.scoredPoints += scored;
        this.concededPoints += conceded;
    }

    public void resetStatus() {
        this.points = 0;
        this.scoredPoints = 0;
        this.concededPoints = 0;
    }

    public void initSquad(int requiredPlayers) {
        starters = new ArrayList<>();
        substitutes = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (i < requiredPlayers) {
                starters.add(players.get(i));
            } else {
                substitutes.add(players.get(i));
            }
        }
    }

    public List<BasePlayer> getStarters() { return starters; }
    public List<BasePlayer> getSubstitutes() { return substitutes; }

    public void substitute(BasePlayer out, BasePlayer in) {
        int outIndex = starters.indexOf(out);
        int inIndex = substitutes.indexOf(in);
        if (outIndex != -1 && inIndex != -1) {
            starters.set(outIndex, in);
            substitutes.set(inIndex, out);
        }
    }

    @Override
    public String toString() {
        return teamName + " | Points: " + points + " | Players: " + players.size();
    }
}