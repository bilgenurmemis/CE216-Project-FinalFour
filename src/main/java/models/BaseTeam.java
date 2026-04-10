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
    public int getTeamStrength(){
        if(players.isEmpty()){
            return 0;
        }
        int total = 0;
        for(BasePlayer p : players){
            total += p.getSkillLevel();
        }
        return total / players.size();
    }
    public boolean addPlayer(BasePlayer player, int maxPlayers){
        if(players.size() >= maxPlayers){
            System.out.println("Team is full!");
            return false;
        }
        players.add(player);
        return true;
    }
    @Override
    public String toString(){
        return teamName + " | Points: " + points + " | Players: " + players.size();
    }
}