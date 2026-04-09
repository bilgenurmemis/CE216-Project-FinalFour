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
}
