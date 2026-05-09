import Services.DataManager;
import core.BasketballSport;
import core.MatchEngine;
import models.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class SportsManagerGUI extends Application {

    private Scene mainMenuScene;
    private Stage window;

    private DataManager dataManager;
    private League league;
    private MatchEngine matchEngine;
    private BaseTeam myTeamObj;
    private BaseTeam opponentTeamObj;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        dataManager = new DataManager();
        matchEngine = new MatchEngine();

        BasketballSport basketballRules = new BasketballSport();
        league = new League(basketballRules);

        List<BaseTeam> loadedTeams = dataManager.setupLeague(basketballRules);
        for (BaseTeam t : loadedTeams) {
            league.addTeam(t);
        }

        Label titleLabel = new Label("Sports Manager: Final Four");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button btnNewGame = new Button("New Game (Basketball)");
        Button btnLoadGame = new Button("Load Game");
        Button btnSaveGame = new Button("Save Game");
        Button btnExit = new Button("Exit");

        btnSaveGame.setOnAction(e -> {
            Services.SaveManager.saveGame(league, "savegame.dat");
            System.out.println("GUI: Game Saved!");
        });

        btnLoadGame.setOnAction(e -> {
            models.League loadedLeague = Services.SaveManager.loadGame("savegame.dat");
            if (loadedLeague != null) {
                this.league = loadedLeague;
                System.out.println("GUI: Game Loaded Successfully!");
            }
        });

        btnNewGame.setMinWidth(200);
        btnLoadGame.setMinWidth(200);
        btnSaveGame.setMinWidth(200);
        btnExit.setMinWidth(200);

        btnNewGame.setOnAction(e -> showNewGameScreen());
        btnExit.setOnAction(e -> window.close());

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, btnNewGame, btnLoadGame, btnSaveGame, btnExit);

        mainMenuScene = new Scene(layout, 500, 500);

        window.setTitle("Sports Manager");
        window.setScene(mainMenuScene);
        window.show();
    }

    private void showNewGameScreen() {
        Label label = new Label("League Setup");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label nameLabel = new Label("Select Your Team:");
        ComboBox<String> myTeamList = new ComboBox<>();
        for (BaseTeam t : league.getTeams()) {
            myTeamList.getItems().add(t.getTeamName());
        }
        myTeamList.setPromptText("Select Your Team");
        myTeamList.setMinWidth(200);

        Label selectLabel = new Label("Select Opponent Team:");
        ComboBox<String> teamList = new ComboBox<>();

        for (BaseTeam t : league.getTeams()) {
            teamList.getItems().add(t.getTeamName());
        }
        teamList.setPromptText("Select a Team");
        teamList.setMinWidth(200);

        Button btnStart = new Button("Start Season");
        btnStart.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnStart.setMinWidth(200);

        Button btnBack = new Button("Back");
        btnBack.setOnAction(e -> window.setScene(mainMenuScene));
        btnBack.setMinWidth(200);

        btnStart.setOnAction(e -> {
            String myTeamName = myTeamList.getValue();
            String opponentName = teamList.getValue();

            if (myTeamName == null || opponentName == null || myTeamName.equals(opponentName)) {
                System.out.println("Please select different teams!");
            } else {
                myTeamObj = league.getTeams().stream()
                        .filter(t -> t.getTeamName().equals(myTeamName))
                        .findFirst()
                        .orElse(null);

                opponentTeamObj = league.getTeams().stream()
                        .filter(t -> t.getTeamName().equals(opponentName))
                        .findFirst()
                        .orElse(null);

                showMatchScreen();
            }
        });

        VBox newGameLayout = new VBox(15);
        newGameLayout.setAlignment(Pos.CENTER);
        newGameLayout.getChildren().addAll(label, nameLabel, myTeamList, selectLabel, teamList, btnStart, btnBack);

        Scene newGameScene = new Scene(newGameLayout, 500, 500);
        window.setScene(newGameScene);
    }

    private void showMatchScreen() {
        Label matchTitle = new Label("Match Day");
        matchTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Label lblHome = new Label(myTeamObj.getTeamName());
        lblHome.setFont(Font.font("Arial", 18));
        Label lblScore = new Label("0 - 0");
        lblScore.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        Label lblAway = new Label(opponentTeamObj.getTeamName());
        lblAway.setFont(Font.font("Arial", 18));

        HBox scoreBoard = new HBox(30);
        scoreBoard.setAlignment(Pos.CENTER);
        scoreBoard.getChildren().addAll(lblHome, lblScore, lblAway);

        Button btnSimulate = new Button("Play Full Match");
        btnSimulate.setMinWidth(200);

        Button btnViewStandings = new Button("View Standings");
        btnViewStandings.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnViewStandings.setMinWidth(200);
        btnViewStandings.setDisable(true);

        btnSimulate.setOnAction(e -> {
            int[] scores = matchEngine.simulateMatch(myTeamObj, opponentTeamObj, league.getSport());

            lblScore.setText(scores[0] + " - " + scores[1]);

            myTeamObj.updateStatus(scores[0], scores[1]);
            opponentTeamObj.updateStatus(scores[1], scores[0]);

            int winPoint = league.getSport().getPointForWin();
            int drawPoint = league.getSport().getPointForDraw();
            if (scores[0] > scores[1]) myTeamObj.addPoints(winPoint);
            else if (scores[1] > scores[0]) opponentTeamObj.addPoints(winPoint);
            else { myTeamObj.addPoints(drawPoint); opponentTeamObj.addPoints(drawPoint); }

            btnSimulate.setDisable(true);
            btnViewStandings.setDisable(false);
        });

        btnViewStandings.setOnAction(e -> showStandingsScreen());

        VBox matchLayout = new VBox(20);
        matchLayout.setAlignment(Pos.CENTER);
        matchLayout.getChildren().addAll(matchTitle, scoreBoard, btnSimulate, btnViewStandings);

        Scene matchScene = new Scene(matchLayout, 500, 500);
        window.setScene(matchScene);
    }

    private void showStandingsScreen() {
        Label titleLabel = new Label("League Standings");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        LeagueTable tableLogic = new LeagueTable(league.getTeams());
        List<BaseTeam> sortedTeams = tableLogic.getTable();

        TableView<BaseTeam> standingsTable = new TableView<>();

        TableColumn<BaseTeam, String> teamCol = new TableColumn<>("Team Name");
        teamCol.setMinWidth(200);
        teamCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTeamName()));

        TableColumn<BaseTeam, String> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(100);
        pointsCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPoints())));

        TableColumn<BaseTeam, String> averageCol = new TableColumn<>("Average");
        averageCol.setMinWidth(100);
        averageCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAverage())));

        standingsTable.getColumns().addAll(teamCol, pointsCol, averageCol);


        ObservableList<BaseTeam> data = FXCollections.observableArrayList(league.getStandings());
        standingsTable.setItems(data);

        Button btnReset = new Button("Reset Standings");
        btnReset.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold;");
        btnReset.setMinWidth(80);
        btnReset.setOnAction(e -> {
            tableLogic.clearStatus();
            standingsTable.setItems(FXCollections.observableArrayList(tableLogic.getTable()));
                });

        standingsTable.setMaxHeight(250);
        standingsTable.setMaxWidth(350);

        Button btnMainMenu = new Button("Back to Main Menu");
        btnMainMenu.setOnAction(e -> window.setScene(mainMenuScene));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, standingsTable, btnMainMenu, btnReset);

        Scene standingsScene = new Scene(layout, 500, 500);
        window.setScene(standingsScene);


    }

    public static void main(String[] args) {
        launch(args);
    }
}