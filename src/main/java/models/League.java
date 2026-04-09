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
     * Ligdeki takımlar arasında çift maç usulü (rövanşlı) fikstür oluşturur.
     * Her takım diğer takımlarla biri içeride, biri dışarıda olmak üzere tam iki kez eşleşir.
     */
    public void generateFixtures() {
        fixtures.clear();
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                BaseTeam teamA = teams.get(i);
                BaseTeam teamB = teams.get(j);
                
                // İlk Ayak: Team A ev sahibi (Home)
                Match firstLeg = new Match(teamA, teamB, sport, 1, "First Leg");
                
                // İkinci Ayak (Rövanş): Team B ev sahibi (Home)
                Match secondLeg = new Match(teamB, teamA, sport, 2, "Second Leg");
                
                fixtures.add(firstLeg);
                fixtures.add(secondLeg);
            }
        }
    }

    /**
     * Oynanmış bir maçı alarak puan tablosunu (standings) günceller.
     * Görevlendirilen sporun (ISport) kurallarına göre puanları dinamik olarak atar.
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

        // ISport interface'inden dinamik olarak puan kurallarını çekiyoruz
        // sport referansı null olma ihtimaline karşı varsayılan 3-1 koruması ekliyorum
        int winPoint = (sport != null) ? sport.getPointForWin() : 3;
        int drawPoint = (sport != null) ? sport.getPointForDraw() : 1;

        if (homeScore > awayScore) {
            // Ev sahibi kazandı
            standings.put(homeTeam, standings.getOrDefault(homeTeam, 0) + winPoint);
        } else if (awayScore > homeScore) {
            // Deplasman kazandı
            standings.put(awayTeam, standings.getOrDefault(awayTeam, 0) + winPoint);
        } else {
            // Beraberlik
            standings.put(homeTeam, standings.getOrDefault(homeTeam, 0) + drawPoint);
            standings.put(awayTeam, standings.getOrDefault(awayTeam, 0) + drawPoint);
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
