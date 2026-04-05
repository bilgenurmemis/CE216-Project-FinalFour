
    package core;

    public class FootballSport implements ISport {
        @Override
        public int getRequiredPlayers() { return 11; }

        @Override
        public int getMatchDuration() { return 90; }

        @Override
        public int getPointForWin() { return 3; }

        @Override
        public int getPointForDraw() { return 1 ; }
    }

