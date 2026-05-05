package core;

import java.io.Serializable;

public class HeadballSport implements ISport, Serializable {
    private static final long serialVersionUID = 1L;


        @Override
        public int getRequiredPlayers() { return 7; }

        @Override
        public int getMatchDuration() { return 450; }

        @Override
        public int getPointForWin() { return 2; }

        @Override
        public int getPointForDraw() { return 1; }
    }
