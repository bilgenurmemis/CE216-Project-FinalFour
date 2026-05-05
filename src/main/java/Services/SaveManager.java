package Services;

import models.League;
import java.io.*;

public class SaveManager {

    public static void saveGame(League league, String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(league);
            System.out.println("Game saved successfully to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public static League loadGame(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            League league = (League) in.readObject();
            System.out.println("Game loaded successfully from: " + filePath);
            return league;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }
}
