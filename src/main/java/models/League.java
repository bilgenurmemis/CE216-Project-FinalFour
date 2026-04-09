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
     * Lige yeni bir takım ekler.
     * Puanlar takımın kendi nesnesinde (BaseTeam) tutulduğu için burada sadece listeye eklenir.
     * @param team Eklenecek takım
     */
    public void addTeam(BaseTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
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
     * Oynanmış bir maçı alarak takımların kendi içindeki (BaseTeam.addPoints) puan sistemini günceller.
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
        int winPoint = (sport != null) ? sport.getPointForWin() : 3;
        int drawPoint = (sport != null) ? sport.getPointForDraw() : 1;

        if (homeScore > awayScore) {
            // Ev sahibi kazandı
            homeTeam.addPoints(winPoint);
        } else if (awayScore > homeScore) {
            // Deplasman kazandı
            awayTeam.addPoints(winPoint);
        } else {
            // Beraberlik
            homeTeam.addPoints(drawPoint);
            awayTeam.addPoints(drawPoint);
        }
    }

    // --- Getters & Setters ---

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
