package core;


    public class HeadballSport implements ISport {
        @Override
        public int getRequiredPlayers() { return 7; }

        @Override
        public int getMatchDuration() { return 40; }

        @Override
        public int getPointForWin() { return 2; }

        @Override
        public int getPointForDraw() { return 1; }
    }
