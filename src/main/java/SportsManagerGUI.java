import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SportsManagerGUI extends Application {

    private Scene mainMenuScene;

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Sports Manager: Final Four");
        titleLabel.setFont(new Font("Arial", 24));

        Button btnNewGame = new Button("New Game (Basketball)");
        Button btnLoadGame = new Button("Load Game");
        Button btnSaveGame = new Button("Save Game");
        Button btnExit = new Button("Exit");

        btnNewGame.setMinWidth(200);
        btnLoadGame.setMinWidth(200);
        btnSaveGame.setMinWidth(200);
        btnExit.setMinWidth(200);

        btnNewGame.setOnAction(e -> showNewGameScreen(primaryStage));
        btnExit.setOnAction(e -> primaryStage.close());

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, btnNewGame, btnLoadGame, btnSaveGame, btnExit);

        mainMenuScene = new Scene(layout, 400, 450);

        primaryStage.setTitle("Sports Manager");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private void showNewGameScreen(Stage stage) {
        Label label = new Label("League Setup");
        label.setFont(new Font("Arial", 20));

        Label nameLabel = new Label("Enter Your Team Name:");
        TextField teamNameField = new TextField();
        teamNameField.setMaxWidth(200);

        // Team selection
        Label selectLabel = new Label("Select Opponent Teams:");
        ComboBox<String> teamList = new ComboBox<>();
        teamList.getItems().addAll("AI Team 1", "AI Team 2", "AI Team 3", "AI Team 4");
        teamList.setPromptText("Select a Team");
        teamList.setMinWidth(200);

        Button btnStart = new Button("Start Season");
        btnStart.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnStart.setMinWidth(200);

        Button btnBack = new Button("Back");
        btnBack.setOnAction(e -> stage.setScene(mainMenuScene));
        btnBack.setMinWidth(200);

        btnStart.setOnAction(e -> {
            String myTeam = teamNameField.getText();
            if(myTeam.isEmpty()) {
                System.out.println("Please enter a team name!");
            } else {
                System.out.println("Starting season with: " + myTeam);

            }
        });

        VBox newGameLayout = new VBox(15);
        newGameLayout.setAlignment(Pos.CENTER);
        newGameLayout.getChildren().addAll(label, nameLabel, teamNameField, selectLabel, teamList, btnStart, btnBack);

        Scene newGameScene = new Scene(newGameLayout, 400, 450);
        stage.setScene(newGameScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}