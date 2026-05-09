package core;

import java.io.Serializable;

public class HeadballSport implements ISport, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int getRequiredPlayers() { return 7; }

    @Override
    public int getSquadSize() { return 10; } // 7 + 3

    @Override
    public int getMatchDuration() { return 40; }

    @Override
    public int getPointForWin() { return 2; }

    @Override
    public int getPointForDraw() { return 1; }
    @Override
    public int getNumberOfPeriods() { return 4; }
}
