package models;

import core.ISport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LeagueTest {

    private League league;
    private ISport mockSport;
    private BaseTeam t1, t2, t3, t4;

    @BeforeEach
    void setUp() {
        // Her testten önce ortamı hazırlıyoruz
        mockSport = new ISport() {
            public int getRequiredPlayers() { return 11; }
            public int getMatchDuration() { return 90; }
            public int getPointForWin() { return 3; }
            public int getPointForDraw() { return 1; }
        };
        league = new League(mockSport);
        
        t1 = new BaseTeam("Fenerbahce");
        t2 = new BaseTeam("Galatasaray");
        t3 = new BaseTeam("Besiktas");
        t4 = new BaseTeam("Trabzonspor");
    }

    @Test
    void testGenerateFixturesTotalMatchCount() {
        league.addTeam(t1);
        league.addTeam(t2);
        league.addTeam(t3);
        league.addTeam(t4);
        
        // Fikstürü çekiyoruz
        league.generateFixtures();
        
        // Çift devreli (rövanşlı) lig usulü için; Takım Sayısı = N ise toplam maç formülü: N * (N-1)
        // Beklenen sonuç 4 * 3 = 12
        assertEquals(12, league.getFixtures().size(), "Fikstürde tam olarak matematiksel N*(N-1) adet eşleşme üretilmelidir.");
    }

    @Test
    void testNoSelfPlayInFixtures() {
        league.addTeam(t1);
        league.addTeam(t2);
        league.addTeam(t3);
        league.generateFixtures();
        
        for (Match match : league.getFixtures()) {
            assertNotEquals(match.getHomeTeam(), match.getAwayTeam(), "Üretilen maç motorunda kesinlikle hiçbir takımın kendisiyle oynadığı (Home=Away) bir maç çıkmamalıdır.");
        }
    }

    @Test
    void testUpdateStandingsHomeWin() {
        league.addTeam(t1);
        league.addTeam(t2);
        
        Match match = new Match(t1, t2, mockSport, 1, "Round 1");
        match.updateScore(3, 1); // Ev sahibi olan t1 maçı 3-1 yendi
        match.markAsPlayed();
        
        // Puan tablosunu işliyoruz
        league.updateStandings(match);
        
        assertEquals(3, t1.getPoints(), "Ev sahibi takım maçı kazandığında, ISport arabirimindeki 3 puanı (veya galibiyet puanını) tam olarak almalıdır.");
        assertEquals(0, t2.getPoints(), "Deplasmandaki takım kaybettiğinde puan hanesine hiçbir ekleme (+0) yapılmamalıdır.");
    }

    @Test
    void testStandingsSortingOrder() {
        league.addTeam(t1); // Hedef: 0 puanda kalacak
        league.addTeam(t2); // Hedef: 6 puana fırlayacak
        league.addTeam(t3); // Hedef: 3 puan alacak
        
        // Maç kazanmışlar gibi basitçe simüle edelim:
        t2.addPoints(6); 
        t3.addPoints(3);
        
        // Puan tablosunu lig'den çekiyoruz
        List<BaseTeam> table = league.getStandings();
        
        assertEquals(t2, table.get(0), "En yüksek puana sahip olan takımın her zaman listenin zirvesinde (0. Index) listelendiğinden emin oluyoruz.");
        assertEquals(t3, table.get(1), "3 Puanlı takımın ortada listelendiğinden emin oluyoruz.");
        assertEquals(t1, table.get(2), "En az (veya 0) puana sahip olan takımın listenin en dibine yerleştiğini doğruluyoruz.");
    }

    @Test
    void testUpdateStandingsDraw() {
        league.addTeam(t1);
        league.addTeam(t2);
        
        Match match = new Match(t1, t2, mockSport, 1, "R1");
        match.updateScore(2, 2); // Maç berabere bitiyor
        match.markAsPlayed();
        
        league.updateStandings(match);
        
        // Beraberlik için sistemin her iki tarafa da puan dağıtabildiğini kanıtlıyoruz
        assertEquals(1, t1.getPoints(), "Berabere kalındığında ev sahibine ISport spesifikasyonunda belirtilen beraberlik puanı (1) verilmelidir.");
        assertEquals(1, t2.getPoints(), "Berabere kalındığında deplasman takımına da puan (1) eklenilmelidir.");
    }

    @Test
    void testNoDuplicateFixtures() {
        league.addTeam(t1);
        league.addTeam(t2);
        league.addTeam(t3);
        league.generateFixtures();
        
        List<Match> fixtures = league.getFixtures();
        for (int i = 0; i < fixtures.size(); i++) {
            for (int j = i + 1; j < fixtures.size(); j++) {
                Match m1 = fixtures.get(i);
                Match m2 = fixtures.get(j);
                
                boolean isDuplicate = m1.getHomeTeam().equals(m2.getHomeTeam()) && m1.getAwayTeam().equals(m2.getAwayTeam());
                assertFalse(isDuplicate, "Oluşturulan fikstürde aynı takım eşleşmesi (aynı ev sahibi ve aynı misafir kurgusuyla) ASLA bir daha tekrar etmemelidir (Duplicate Koruması).");
            }
        }
    }
}
