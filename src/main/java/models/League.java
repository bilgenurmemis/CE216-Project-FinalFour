package models;

import core.ISport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class League {
    private List<BaseTeam> teams;
    private List<Match> fixtures;
    private Map<BaseTeam, Integer> standings;
    private ISport sport;

    public League(ISport sport) {
        this.sport = sport;
        this.teams = new ArrayList<>();
        this.fixtures = new ArrayList<>();
        this.standings = new HashMap<>(); // Puan durumunu Takım -> Puan şeklinde tutuyoruz
    }

    // --- Getters & Setters ---

    public List<BaseTeam> getTeams() {
        return teams;
    }

    public List<Match> getFixtures() {
        return fixtures;
    }

    public Map<BaseTeam, Integer> getStandings() {
        return standings;
    }

    public ISport getSport() {
        return sport;
    }

    public void setSport(ISport sport) {
        this.sport = sport;
    }
}
