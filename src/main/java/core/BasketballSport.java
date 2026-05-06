package core;

import java.io.Serializable;

public class BasketballSport implements ISport, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int getRequiredPlayers() {
        return 5; // Basketbolda sahada 5 oyuncu bulunur
    }

    @Override
    public int getMatchDuration() {
        return 40; // 4 periyot x 10 dakika
    }

    @Override
    public int getPointForWin() {
        return 2; // Galibiyet için genelde 2 puan verilir
    }

    @Override
    public int getPointForDraw() {
        return 1; // Normalde basketbolda beraberlik yoktur, uzatmaya gider ama genel yapı için 1 diyebiliriz
    }
}
