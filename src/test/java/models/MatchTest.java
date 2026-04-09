package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void testMatchBaseInitialization() {
        // Arrange: Sadece iki takımla temel bir maç oluşturduğumuzu varsayıyoruz
        BaseTeam homeTeam = new BaseTeam("Fenerbahce");
        BaseTeam awayTeam = new BaseTeam("Galatasaray");

        // Act
        Match match = new Match(homeTeam, awayTeam);

        // Assert: Başlangıç durumlarının doğru atanıp atanmadığını doğruluyoruz
        assertEquals(homeTeam, match.getHomeTeam(), "Ev sahibi takım doğru atanmalı.");
        assertEquals(awayTeam, match.getAwayTeam(), "Deplasman takımı doğru atanmalı.");
        assertEquals(0, match.getHomeScore(), "Başlangıçta ev sahibi skoru 0 olmalı.");
        assertEquals(0, match.getAwayScore(), "Başlangıçta deplasman skoru 0 olmalı.");
        assertFalse(match.isPlayed(), "Yeni oluşturulmuş bir maçın statüsü oynanmamış (false) olmalı.");
    }

    @Test
    void testMatchComprehensiveInitialization() {
        // Arrange
        BaseTeam homeTeam = new BaseTeam("Besiktas");
        BaseTeam awayTeam = new BaseTeam("Trabzonspor");
        core.ISport mockSport = new core.FootballSport(); // ISport implementasyonlarından biri
        
        // Act: Tüm yardımcı alanların doldurulduğu geniş (kapsamlı) constructor çağrısı
        Match match = new Match(homeTeam, awayTeam, mockSport, 3, "QuarterFinal");

        // Assert: Ekstra alanlarımızın doğru yerleştiğinden emin oluyoruz
        assertEquals(mockSport, match.getSport(), "Spor arabirimi (ISport) doğru atanmalı.");
        assertEquals(3, match.getMatchWeek(), "Maç haftası (week) doğru kaydedilmeli.");
        assertEquals("QuarterFinal", match.getRound(), "Maç turu (round) doğru kaydedilmeli.");
    }

    @Test
    void testUpdateScoreLogic() {
        // Arrange
        BaseTeam homeTeam = new BaseTeam("Anadolu Efes");
        BaseTeam awayTeam = new BaseTeam("Fenerbahce Beko");
        Match match = new Match(homeTeam, awayTeam);

        // Act
        match.updateScore(85, 74);

        // Assert
        assertEquals(85, match.getHomeScore(), "Ev sahibi skoru 85 olarak güncellenmelidir.");
        assertEquals(74, match.getAwayScore(), "Deplasman skoru 74 olarak güncellenmelidir.");
    }
}
