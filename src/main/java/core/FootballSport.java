
package core;

import java.io.Serializable;

public class FootballSport implements ISport, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int getRequiredPlayers() { return 11; }

    @Override
    public int getSquadSize() { return 18; } // 11 + 7 bench

    @Override
    public int getMatchDuration() { return 90; }

    @Override
    public int getPointForWin() { return 3; }

    @Override
    public int getPointForDraw() { return 1; }
}