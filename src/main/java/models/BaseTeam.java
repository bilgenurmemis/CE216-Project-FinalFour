package models;

import java.util.ArrayList;
import java.util.List;

public class BaseTeam {
    private String teamName;
    private List<BasePlayer> players;
    private int points;

    public BaseTeam(String teamName) {
        this.teamName = teamName;
        this.players = new ArrayList<>();
        this.points = 0;
    }

    public String getTeamName() { return teamName; }
    public void addPlayer(BasePlayer player) { this.players.add(player); }
    public List<BasePlayer> getPlayers() { return players; }
    public int getPoints() { return points; }
    public void addPoints(int earnedPoints) { this.points += earnedPoints; }
}