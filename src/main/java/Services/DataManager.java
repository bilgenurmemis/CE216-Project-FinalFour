package Services;

import core.BasketballSport;
import core.ISport;
import core.FootballSport;
import core.HeadballSport;
import models.*;
import java.nio.file.*;
import java.util.*;
import java.io.IOException;

public class DataManager {

    public List<String> readLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<String> loadCoaches() {
        return readLines("coaches.txt");
    }

    public List<BaseTeam> setupLeague(ISport rules) {
        List<String> teamNames = readLines("teams.txt");
        List<String> playerNames = readLines("players.txt");
        List<String> coachNames = loadCoaches();
        List<BaseTeam> teams = new ArrayList<>();
        Random random = new Random();
        Collections.shuffle(playerNames);
        Collections.shuffle(coachNames);

        int maxPlayersPerTeam = rules.getSquadSize();

        for (String tName : teamNames) {
            BaseTeam team = new BaseTeam(tName);

            for (int i = 0; i < maxPlayersPerTeam; i++) {
                if (!playerNames.isEmpty()) {
                    String pName = playerNames.remove(0);
                    int age = 18 + random.nextInt(15);
                    double fitness = 70 + random.nextDouble() * 30;
                    BasePlayer player;
                    if (rules instanceof HeadballSport) {
                        player = new HeadballPlayer(pName, age, fitness);
                    } else if (rules instanceof BasketballSport) {
                        player = new BasketballPlayer(pName, age, fitness);
                    } else {
                        player = new FootballPlayer(pName, age, fitness);
                    }
                    team.addPlayer(player, maxPlayersPerTeam);
                }
            }

            if (!coachNames.isEmpty()) {
                String coachName = coachNames.remove(0);
                team.setCoach(coachName);
            }

            teams.add(team);
        }
        return teams;
    }
}