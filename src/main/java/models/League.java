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

    /**
     * Lige yeni bir takım ekler ve puanlama tablosunda (standings) 0 puanla başlatır.
     * @param team Eklenecek takım
     */
    public void addTeam(BaseTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
            standings.put(team, 0); 
        }
    }

    /**
     * Ligdeki takımlar arasında basit bir rövanşsız (tek maçlık) fikstür oluşturur.
     */
    public void generateFixtures() {
        fixtures.clear();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                BaseTeam homeTeam = teams.get(i);
                BaseTeam awayTeam = teams.get(j);
                
                Match match = new Match(homeTeam, awayTeam, sport, 1, "Normal Season");
                fixtures.add(match);
            }
        }
    }

    /**
     * Oynanmış bir maçı alarak puan tablosunu (standings) günceller.
     * Standart 3 puanlı sistemi kullanır (Galibiyet 3, Beraberlik 1).
     * @param match Oynanan ve skoru belli olan maç
     */
    public void updateStandings(Match match) {
        if (!match.isPlayed()) {
            return; // Maç henüz oynanmadıysa işlem yapmayız
        }

        BaseTeam homeTeam = match.getHomeTeam();
        BaseTeam awayTeam = match.getAwayTeam();

        int homeScore = match.getHomeScore();
        int awayScore = match.getAwayScore();

        if (homeScore > awayScore) {
            // Ev sahibi kazandı
            standings.put(homeTeam, standings.getOrDefault(homeTeam, 0) + 3);
        } else if (awayScore > homeScore) {
            // Deplasman kazandı
            standings.put(awayTeam, standings.getOrDefault(awayTeam, 0) + 3);
        } else {
            // Beraberlik
            standings.put(homeTeam, standings.getOrDefault(homeTeam, 0) + 1);
            standings.put(awayTeam, standings.getOrDefault(awayTeam, 0) + 1);
        }
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
