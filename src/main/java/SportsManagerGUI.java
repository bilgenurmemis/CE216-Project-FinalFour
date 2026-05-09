import Services.DataManager;
import Services.SaveManager;
import core.BasketballSport;
import core.FootballSport;
import core.MatchEngine;
import models.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private final String bgColor = "-fx-background-color: #f4f6f9;";
    private final String mainBtnColor = "-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;";
    private final String titleColor = "-fx-text-fill: #2c3e50;";

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        dataManager = new DataManager();
        matchEngine = new MatchEngine();

        Label titleLabel = new Label("Sports Manager: Final Four");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titleLabel.setStyle(titleColor);

        Button btnNewBasketball = new Button("New Game (Basketball)");
        Button btnNewFootball = new Button("New Game (Football)");
        Button btnLoadGame = new Button("Load Game");
        Button btnSaveGame = new Button("Save Game");
        Button btnExit = new Button("Exit");

        btnNewBasketball.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnNewFootball.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnLoadGame.setStyle(mainBtnColor);
        btnSaveGame.setStyle(mainBtnColor);
        btnExit.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        btnNewBasketball.setMinWidth(220);
        btnNewFootball.setMinWidth(220);
        btnLoadGame.setMinWidth(220);
        btnSaveGame.setMinWidth(220);
        btnExit.setMinWidth(220);

        btnNewBasketball.setOnAction(e -> initNewGame(new BasketballSport()));
        btnNewFootball.setOnAction(e -> initNewGame(new FootballSport()));

        btnSaveGame.setOnAction(e -> {
            if (league == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No active game to save! Please start a new game or load first.");
                alert.showAndWait();
            } else {
                SaveManager.saveGame(league, "savegame.dat");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Game saved successfully!");
                alert.showAndWait();
            }
        });

        btnLoadGame.setOnAction(e -> {
            League loadedLeague = SaveManager.loadGame("savegame.dat");
            if (loadedLeague != null) {
                this.league = loadedLeague;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Game loaded successfully!");
                alert.showAndWait();
            }
        });

        btnExit.setOnAction(e -> window.close());

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(titleLabel, btnNewBasketball, btnNewFootball, btnLoadGame, btnSaveGame, btnExit);

        mainMenuScene = new Scene(layout, 550, 500);

        window.setTitle("Sports Manager");
        window.setScene(mainMenuScene);
        window.show();
    }


    private void initNewGame(core.ISport selectedSport) {
        league = new League(selectedSport);
        List<BaseTeam> loadedTeams = dataManager.setupLeague(selectedSport);
        for (BaseTeam t : loadedTeams) {
            league.addTeam(t);
        }
        showNewGameScreen();
    }

    private void showNewGameScreen() {
        Label label = new Label("League Setup");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        label.setStyle(titleColor);

        Label nameLabel = new Label("Select Your Team:");
        nameLabel.setFont(Font.font("Arial", 14));

        ComboBox<String> myTeamList = new ComboBox<>();
        for (BaseTeam t : league.getTeams()) {
            myTeamList.getItems().add(t.getTeamName());
        }
        myTeamList.setPromptText("Select Your Team");
        myTeamList.setMinWidth(220);
        myTeamList.setStyle("-fx-font-size: 14px;");

        Label selectLabel = new Label("Select Opponent Team:");
        selectLabel.setFont(Font.font("Arial", 14));

        ComboBox<String> opponentTeamList = new ComboBox<>();
        for (BaseTeam t : league.getTeams()) {
            opponentTeamList.getItems().add(t.getTeamName());
        }
        opponentTeamList.setPromptText("Select Opponent Team");
        opponentTeamList.setMinWidth(220);
        opponentTeamList.setStyle("-fx-font-size: 14px;");

        Button btnStart = new Button("Start Season");
        btnStart.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnStart.setMinWidth(220);

        Button btnBack = new Button("Back");
        btnBack.setStyle(mainBtnColor);
        btnBack.setOnAction(e -> window.setScene(mainMenuScene));
        btnBack.setMinWidth(220);

        btnStart.setOnAction(e -> {
            String myTeamName = myTeamList.getValue();
            String opponentName = opponentTeamList.getValue();

            if (myTeamName == null || opponentName == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null);
                alert.setContentText("Please select both teams!");
                alert.showAndWait();
            } else if (myTeamName.equals(opponentName)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Match");
                alert.setHeaderText(null);
                alert.setContentText("A team cannot play against itself! Select different teams.");
                alert.showAndWait();
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
        newGameLayout.setStyle(bgColor);
        newGameLayout.getChildren().addAll(label, nameLabel, myTeamList, selectLabel, opponentTeamList, btnStart, btnBack);

        Scene newGameScene = new Scene(newGameLayout, 550, 500);
        window.setScene(newGameScene);
    }

    private void showMatchScreen() {
        Label matchTitle = new Label("Match Day");
        matchTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        matchTitle.setStyle(titleColor);

        ImageView homeLogoView = new ImageView();
        homeLogoView.setFitWidth(60);
        homeLogoView.setFitHeight(60);
        homeLogoView.setPreserveRatio(true);
        try {
            String homePath = "/logos/" + myTeamObj.getTeamName() + ".jpg";
            java.net.URL homeRes = getClass().getResource(homePath);
            if (homeRes != null) homeLogoView.setImage(new Image(homeRes.toExternalForm()));
        } catch (Exception ignored) {}

        ImageView awayLogoView = new ImageView();
        awayLogoView.setFitWidth(60);
        awayLogoView.setFitHeight(60);
        awayLogoView.setPreserveRatio(true);
        try {
            String awayPath = "/logos/" + opponentTeamObj.getTeamName() + ".jpg";
            java.net.URL awayRes = getClass().getResource(awayPath);
            if (awayRes != null) awayLogoView.setImage(new Image(awayRes.toExternalForm()));
        } catch (Exception ignored) {}

        Label lblHome = new Label(myTeamObj.getTeamName());
        lblHome.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label lblScore = new Label("0 - 0");
        lblScore.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        lblScore.setStyle("-fx-text-fill: #e74c3c;");

        Label lblAway = new Label(opponentTeamObj.getTeamName());
        lblAway.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox scoreBoard = new HBox(15);
        scoreBoard.setAlignment(Pos.CENTER);
        scoreBoard.getChildren().addAll(homeLogoView, lblHome, lblScore, lblAway, awayLogoView);

        Button btnSimulate = new Button("Play Full Match");
        btnSimulate.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        btnSimulate.setMinWidth(220);

        Button btnViewStandings = new Button("View Standings");
        btnViewStandings.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        btnViewStandings.setMinWidth(220);
        btnViewStandings.setDisable(true);

        btnSimulate.setOnAction(e -> {
            int[] userMatchScores = matchEngine.simulateMatch(myTeamObj, opponentTeamObj, league.getSport());
            lblScore.setText(userMatchScores[0] + " - " + userMatchScores[1]);

            List<BaseTeam> teams = league.getTeams();
            int winPoint = league.getSport().getPointForWin();
            int drawPoint = league.getSport().getPointForDraw();

            for(int i = 0; i < teams.size(); i++){
                for(int j = i + 1; j < teams.size(); j++){
                    BaseTeam home = teams.get(i);
                    BaseTeam away = teams.get(j);

                    playAndRecord(home, away, winPoint, drawPoint);
                    playAndRecord(away, home, winPoint,drawPoint);
                }
            }
            btnSimulate.setDisable(true);
            btnViewStandings.setDisable(false);
        });

        btnViewStandings.setOnAction(e -> showStandingsScreen());

        VBox matchLayout = new VBox(30);
        matchLayout.setAlignment(Pos.CENTER);
        matchLayout.setStyle(bgColor);
        matchLayout.getChildren().addAll(matchTitle, scoreBoard, btnSimulate, btnViewStandings);

        Scene matchScene = new Scene(matchLayout, 650, 500);
        window.setScene(matchScene);

    }

    private void showStandingsScreen() {
        Label titleLabel = new Label("League Standings");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle(titleColor);

        LeagueTable tableLogic = new LeagueTable(league.getTeams());
        List<BaseTeam> sortedTeams = tableLogic.getTable();

        TableView<BaseTeam> standingsTable = new TableView<>();

        TableColumn<BaseTeam, String> teamCol = new TableColumn<>("Team Name");
        teamCol.setMinWidth(220);
        teamCol.setStyle("-fx-font-weight: bold;");
        teamCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTeamName()));

        TableColumn<BaseTeam, String> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(90);
        pointsCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        pointsCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPoints())));

        TableColumn<BaseTeam, String> averageCol = new TableColumn<>("Average");
        averageCol.setMinWidth(90);
        averageCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        averageCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAverage())));

        standingsTable.getColumns().addAll(teamCol, pointsCol, averageCol);

        ObservableList<BaseTeam> data = FXCollections.observableArrayList(sortedTeams);
        standingsTable.setItems(data);

        Button btnReset = new Button("Reset Standings");
        btnReset.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-weight: bold;");
        btnReset.setMinWidth(120);
        btnReset.setOnAction(e -> {
            for(BaseTeam team : league.getTeams()){
                team.resetStatus();
            }
            LeagueTable newTable = new LeagueTable(league.getTeams());
            standingsTable.setItems(FXCollections.observableArrayList(newTable.getTable()));
        });

        standingsTable.setMaxHeight(300);
        standingsTable.setMaxWidth(420);

        Button btnMainMenu = new Button("Back to Main Menu");
        btnMainMenu.setStyle(mainBtnColor);
        btnMainMenu.setOnAction(e -> window.setScene(mainMenuScene));

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnMainMenu, btnReset);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(titleLabel, standingsTable, buttonBox);

        Scene standingsScene = new Scene(layout, 550, 550);
        window.setScene(standingsScene);
    }
    private void playAndRecord(BaseTeam t1, BaseTeam t2, int winPoint, int drawPoint){
        int[] scores = matchEngine.simulateMatch(t1, t2, league.getSport());
        t1.updateStatus(scores[0], scores[1]);
        t2.updateStatus(scores[1], scores[0]);

        if(scores[0] > scores[1]){
            t1.addPoints(winPoint);
        }else if(scores[1] > scores[0]){
            t2.addPoints(winPoint);
        }else{
            t1.addPoints(drawPoint);
            t2.addPoints(drawPoint);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}