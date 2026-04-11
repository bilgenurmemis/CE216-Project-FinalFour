import core.HeadballSport;
import core.MatchEngine;
import models.BaseTeam;
import models.League;
import models.Match;
import Services.DataManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   CE216 SPORTS MANAGER - MILESTONE 2");
        System.out.println("          Team: Final Four");
        System.out.println("==========================================\n");

        // Initialize core components
        HeadballSport headball = new HeadballSport();
        League league = new League(headball);
        DataManager dataManager = new DataManager();
        MatchEngine engine = new MatchEngine();

        // Load teams and players from text files
        List<BaseTeam> teams = dataManager.setupLeague(headball);
        for (BaseTeam team : teams) {
            league.addTeam(team);
        }
        System.out.println("[INFO] Successfully loaded " + teams.size() + " teams.\n");

        // Generate double round-robin fixtures
        league.generateFixtures();
        List<Match> fixtures = league.getFixtures();
        System.out.println("[INFO] Fixtures generated. Total matches: " + fixtures.size() + "\n");

        // Simulate the entire season
        System.out.println("--- MATCH RESULTS ---");
        for (Match match : fixtures) {
            engine.simulateMatch(match);
            league.updateStandings(match);

            System.out.printf("%-18s %2d - %-2d %18s\n",
                    match.getHomeTeam().getTeamName(),
                    match.getHomeScore(),
                    match.getAwayScore(),
                    match.getAwayTeam().getTeamName());
        }

        // Display final league table
        System.out.println("\n--- FINAL STANDINGS ---");
        System.out.printf("%-5s | %-22s | %-5s\n", "Rank", "Team", "Pts");
        System.out.println("---------------------------------------");

        List<BaseTeam> standings = league.getStandings();
        int rank = 1;
        for (BaseTeam team : standings) {
            System.out.printf("%-5d | %-22s | %-5d\n", rank++, team.getTeamName(), team.getPoints());
        }
        System.out.println("---------------------------------------");
        System.out.println("Simulation completed successfully.");
    }
}