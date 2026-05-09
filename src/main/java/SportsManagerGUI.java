import Services.DataManager;
import Services.SaveManager;
import core.BasketballSport;
import core.FootballSport;
import core.HeadballSport;
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
    private List<List<Match>> weeklyFixtures;
    private int currentWeek = 0;
    private int[] cumulativeScores = {0, 0};
    private boolean[] playedPeriods;

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
        Button btnNewHeadball = new Button("New Game (Headball)");
        Button btnLoadGame = new Button("Load Game");
        Button btnSaveGame = new Button("Save Game");
        Button btnExit = new Button("Exit");

        btnNewBasketball.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnNewFootball.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnNewHeadball.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnLoadGame.setStyle(mainBtnColor);
        btnSaveGame.setStyle(mainBtnColor);
        btnExit.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        btnNewBasketball.setMinWidth(220);
        btnNewFootball.setMinWidth(220);
        btnNewHeadball.setMinWidth(220);
        btnLoadGame.setMinWidth(220);
        btnSaveGame.setMinWidth(220);
        btnExit.setMinWidth(220);

        btnNewBasketball.setOnAction(e -> initNewGame(new BasketballSport()));
        btnNewFootball.setOnAction(e -> initNewGame(new FootballSport()));
        btnNewHeadball.setOnAction(e -> initNewGame(new HeadballSport()));

        btnSaveGame.setOnAction(e -> {
            if (league == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No active game to save!");
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
        layout.getChildren().addAll(titleLabel, btnNewBasketball, btnNewFootball,
                btnNewHeadball, btnLoadGame, btnSaveGame, btnExit);

        mainMenuScene = new Scene(layout, 550, 580);
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

        Button btnStart = new Button("Start Season");
        btnStart.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnStart.setMinWidth(220);

        Button btnBack = new Button("Back");
        btnBack.setStyle(mainBtnColor);
        btnBack.setOnAction(e -> window.setScene(mainMenuScene));
        btnBack.setMinWidth(220);

        btnStart.setOnAction(e -> {
            String myTeamName = myTeamList.getValue();

            if (myTeamName == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Information");
                alert.setHeaderText(null);
                alert.setContentText("Please select your team!");
                alert.showAndWait();
            } else {
                myTeamObj = league.getTeams().stream()
                        .filter(t -> t.getTeamName().equals(myTeamName))
                        .findFirst().orElse(null);

                league.generateFixtures();
                weeklyFixtures = league.getWeeklyFixtures();
                currentWeek = 0;
                showWeekScreen();
            }
        });

        VBox newGameLayout = new VBox(15);
        newGameLayout.setAlignment(Pos.CENTER);
        newGameLayout.setStyle(bgColor);
        newGameLayout.getChildren().addAll(label, nameLabel, myTeamList, btnStart, btnBack);

        Scene newGameScene = new Scene(newGameLayout, 550, 400);
        window.setScene(newGameScene);
    }

    private void showWeekScreen() {
        if (currentWeek >= weeklyFixtures.size()) {
            showSeasonEndScreen();
            return;
        }

        List<Match> weekMatches = weeklyFixtures.get(currentWeek);

        Label weekLabel = new Label("Week " + (currentWeek + 1) + " of " + weeklyFixtures.size());
        weekLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        weekLabel.setStyle(titleColor);

        VBox matchList = new VBox(8);
        matchList.setAlignment(Pos.CENTER);

        boolean myMatchFound = false;

        for (Match match : weekMatches) {
            boolean isMyMatch = match.getHomeTeam().getTeamName().equals(myTeamObj.getTeamName()) ||
                    match.getAwayTeam().getTeamName().equals(myTeamObj.getTeamName());

            if (isMyMatch && !myMatchFound) {
                myMatchFound = true;
                Button btnPlay = new Button("▶ PLAY: " + match.getHomeTeam().getTeamName() +
                        " vs " + match.getAwayTeam().getTeamName());
                btnPlay.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
                btnPlay.setMinWidth(350);
                Match finalMatch = match;
                btnPlay.setOnAction(ev -> {
                    myTeamObj = finalMatch.getHomeTeam().getTeamName().equals(myTeamObj.getTeamName())
                            ? finalMatch.getHomeTeam() : finalMatch.getAwayTeam();
                    opponentTeamObj = finalMatch.getHomeTeam() == myTeamObj
                            ? finalMatch.getAwayTeam() : finalMatch.getHomeTeam();

                    myTeamObj.initSquad(league.getSport().getRequiredPlayers());
                    opponentTeamObj.initSquad(league.getSport().getRequiredPlayers());

                    for (Match m : weekMatches) {
                        if (m != finalMatch && !m.isPlayed()) {
                            int[] scores = matchEngine.simulateMatch(
                                    m.getHomeTeam(), m.getAwayTeam(), league.getSport());
                            m.updateScore(scores[0], scores[1]);
                            m.markAsPlayed();
                            league.updateStandings(m);
                        }
                    }
                    showMatchScreen(finalMatch);
                });
                matchList.getChildren().add(btnPlay);
            } else {
                if (!match.isPlayed()) {
                    int[] scores = matchEngine.simulateMatch(
                            match.getHomeTeam(), match.getAwayTeam(), league.getSport());
                    match.updateScore(scores[0], scores[1]);
                    match.markAsPlayed();
                    league.updateStandings(match);
                }
                Label matchLabel = new Label(match.getHomeTeam().getTeamName() +
                        " " + match.getHomeScore() + " - " + match.getAwayScore() +
                        " " + match.getAwayTeam().getTeamName());
                matchLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
                matchList.getChildren().add(matchLabel);
            }
        }

        Button btnSquad = new Button("👥 View My Squad");
        btnSquad.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSquad.setMinWidth(200);
        btnSquad.setOnAction(e -> showSquadScreen(null, 0, 0));

        Button btnStandings = new Button("View Standings");
        btnStandings.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnStandings.setMinWidth(200);
        btnStandings.setOnAction(e -> showStandingsScreen());

        VBox layout = new VBox(12);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(weekLabel, matchList, btnSquad, btnStandings);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(bgColor);

        Scene scene = new Scene(scrollPane, 650, 600);
        window.setScene(scene);
    }

    private void showMatchScreen(Match match) {
        cumulativeScores = new int[]{0, 0};
        int totalPeriods = league.getSport().getNumberOfPeriods();
        playedPeriods = new boolean[totalPeriods + 1];
        showPeriodScreen(match, 1, totalPeriods);
    }

    private void showPeriodScreen(Match match, int period, int totalPeriods) {
        Label matchTitle = new Label("Match Day - Week " + (currentWeek + 1));
        matchTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        matchTitle.setStyle(titleColor);

        Label periodLabel = new Label("Period " + period + " of " + totalPeriods);
        periodLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        periodLabel.setStyle("-fx-text-fill: #7f8c8d;");

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

        Label lblScore = new Label(cumulativeScores[0] + " - " + cumulativeScores[1]);
        lblScore.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        lblScore.setStyle("-fx-text-fill: #e74c3c;");

        Label lblAway = new Label(opponentTeamObj.getTeamName());
        lblAway.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox scoreBoard = new HBox(15);
        scoreBoard.setAlignment(Pos.CENTER);
        scoreBoard.getChildren().addAll(homeLogoView, lblHome, lblScore, lblAway, awayLogoView);

        Label resultLabel = new Label(playedPeriods[period] ? "Period " + period + " already played." : "");
        resultLabel.setFont(Font.font("Arial", 14));
        resultLabel.setStyle("-fx-text-fill: #27ae60;");

        Button btnPlayPeriod = new Button("▶ Play Period " + period);
        btnPlayPeriod.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        btnPlayPeriod.setMinWidth(220);

        Button btnViewSquad = new Button("👥 View Squad");
        btnViewSquad.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold;");
        btnViewSquad.setMinWidth(220);
        btnViewSquad.setOnAction(e -> showSquadScreen(match, period, totalPeriods));

        Button btnSub = new Button("🔄 Substitute Player");
        btnSub.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold;");
        btnSub.setMinWidth(220);
        btnSub.setOnAction(e -> showSubstitutionScreen(match, period, totalPeriods));

        Button btnNext = new Button(period < totalPeriods ? "Next Period →" : "End Match");
        btnNext.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        btnNext.setMinWidth(220);

        if (playedPeriods[period]) {
            btnPlayPeriod.setDisable(true);
            btnNext.setDisable(false);
        } else {
            btnNext.setDisable(true);
        }

        btnPlayPeriod.setOnAction(e -> {
            int[] periodScores = matchEngine.simulateQuarter(
                    myTeamObj, opponentTeamObj, league.getSport(), period);
            cumulativeScores[0] += periodScores[0];
            cumulativeScores[1] += periodScores[1];
            playedPeriods[period] = true;

            lblScore.setText(cumulativeScores[0] + " - " + cumulativeScores[1]);
            resultLabel.setText("Period " + period + ": +" + periodScores[0] + " - +" + periodScores[1]);

            btnPlayPeriod.setDisable(true);
            btnNext.setDisable(false);
        });

        btnNext.setOnAction(e -> {
            if (period < totalPeriods) {
                showPeriodScreen(match, period + 1, totalPeriods);
            } else {
                match.updateScore(cumulativeScores[0], cumulativeScores[1]);
                match.markAsPlayed();
                league.updateStandings(match);
                showInjuryReport();
            }
        });

        VBox matchLayout = new VBox(15);
        matchLayout.setAlignment(Pos.CENTER);
        matchLayout.setStyle(bgColor);
        matchLayout.getChildren().addAll(matchTitle, periodLabel, scoreBoard,
                resultLabel, btnPlayPeriod, btnViewSquad, btnSub, btnNext);

        Scene matchScene = new Scene(matchLayout, 650, 600);
        window.setScene(matchScene);
    }

    private void showSubstitutionScreen(Match match, int period, int totalPeriods) {
        Label title = new Label("🔄 Player Substitution");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle(titleColor);

        Label subLabel = new Label("Select Starter to Remove:");
        subLabel.setFont(Font.font("Arial", 14));

        ComboBox<String> removeList = new ComboBox<>();
        for (BasePlayer p : myTeamObj.getStarters()) {
            String status = p.isInjured() ? " 🚑 INJURED" : " ✅";
            removeList.getItems().add(p.getName() + status);
        }
        removeList.setPromptText("Select starter to sub out");
        removeList.setMinWidth(280);

        Label addLabel = new Label("Select Substitute to Add:");
        addLabel.setFont(Font.font("Arial", 14));

        ComboBox<String> addList = new ComboBox<>();
        for (BasePlayer p : myTeamObj.getSubstitutes()) {
            String status = p.isInjured() ? " 🚑 INJURED" : " ✅";
            addList.getItems().add(p.getName() + status);
        }
        addList.setPromptText("Select substitute to sub in");
        addList.setMinWidth(280);

        Label resultLabel = new Label("");
        resultLabel.setFont(Font.font("Arial", 13));

        Button btnConfirm = new Button("✅ Confirm Substitution");
        btnConfirm.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirm.setMinWidth(250);
        btnConfirm.setOnAction(e -> {
            String removeSelection = removeList.getValue();
            String addSelection = addList.getValue();

            if (removeSelection == null || addSelection == null) {
                resultLabel.setText("Please select both players!");
                resultLabel.setStyle("-fx-text-fill: #e74c3c;");
            } else {
                String removeName = removeSelection.split(" ✅| 🚑")[0].trim();
                String addName = addSelection.split(" ✅| 🚑")[0].trim();

                BasePlayer removePlayer = myTeamObj.getStarters().stream()
                        .filter(p -> p.getName().equals(removeName))
                        .findFirst().orElse(null);

                BasePlayer addPlayer = myTeamObj.getSubstitutes().stream()
                        .filter(p -> p.getName().equals(addName))
                        .findFirst().orElse(null);

                if (removePlayer != null && addPlayer != null) {
                    myTeamObj.substitute(removePlayer, addPlayer);
                    resultLabel.setText("✅ " + removeName + " → " + addName + " done!");
                    resultLabel.setStyle("-fx-text-fill: #27ae60;");

                    removeList.getItems().clear();
                    for (BasePlayer p : myTeamObj.getStarters()) {
                        String status = p.isInjured() ? " 🚑 INJURED" : " ✅";
                        removeList.getItems().add(p.getName() + status);
                    }
                    addList.getItems().clear();
                    for (BasePlayer p : myTeamObj.getSubstitutes()) {
                        String status = p.isInjured() ? " 🚑 INJURED" : " ✅";
                        addList.getItems().add(p.getName() + status);
                    }
                }
            }
        });

        Button btnBack = new Button("Back to Match");
        btnBack.setStyle(mainBtnColor);
        btnBack.setMinWidth(220);
        btnBack.setOnAction(e -> showPeriodScreen(match, period, totalPeriods));

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(title, subLabel, removeList, addLabel,
                addList, btnConfirm, resultLabel, btnBack);

        Scene scene = new Scene(layout, 550, 500);
        window.setScene(scene);
    }

    private void showSquadScreen(Match match, int period, int totalPeriods) {
        Label title = new Label("Squad - " + myTeamObj.getTeamName());
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle(titleColor);

        Label coachLabel = new Label("Coach: " + (myTeamObj.getCoach() != null ?
                myTeamObj.getCoach() : "Unknown"));
        coachLabel.setFont(Font.font("Arial", 14));
        coachLabel.setStyle("-fx-text-fill: #7f8c8d;");

        // Starters tablosu
        Label startersLabel = new Label("⚽ Starters:");
        startersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startersLabel.setStyle("-fx-text-fill: #27ae60;");

        TableView<BasePlayer> startersTable = new TableView<>();
        startersTable.setMaxHeight(200);
        addPlayerColumns(startersTable);
        if (!myTeamObj.getStarters().isEmpty()) {
            startersTable.setItems(FXCollections.observableArrayList(myTeamObj.getStarters()));
        } else {
            startersTable.setItems(FXCollections.observableArrayList(myTeamObj.getPlayers()));
        }

        // Substitutes tablosu
        Label subsLabel = new Label("🪑 Substitutes:");
        subsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        subsLabel.setStyle("-fx-text-fill: #e67e22;");

        TableView<BasePlayer> subsTable = new TableView<>();
        subsTable.setMaxHeight(150);
        addPlayerColumns(subsTable);
        if (!myTeamObj.getSubstitutes().isEmpty()) {
            subsTable.setItems(FXCollections.observableArrayList(myTeamObj.getSubstitutes()));
        }

        Button btnBack = new Button("Back to Match");
        btnBack.setStyle(mainBtnColor);
        btnBack.setMinWidth(200);
        btnBack.setOnAction(e -> {
            if (match != null) {
                showPeriodScreen(match, period, totalPeriods);
            } else {
                showWeekScreen();
            }
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(title, coachLabel, startersLabel, startersTable,
                subsLabel, subsTable, btnBack);

        Scene scene = new Scene(layout, 700, 650);
        window.setScene(scene);
    }

    private void addPlayerColumns(TableView<BasePlayer> table) {
        TableColumn<BasePlayer, String> nameCol = new TableColumn<>("Name");
        nameCol.setMinWidth(150);
        nameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        TableColumn<BasePlayer, String> ageCol = new TableColumn<>("Age");
        ageCol.setMinWidth(50);
        ageCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getAge())));

        TableColumn<BasePlayer, String> fitnessCol = new TableColumn<>("Fitness");
        fitnessCol.setMinWidth(80);
        fitnessCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.format("%.1f", data.getValue().getFitness())));

        TableColumn<BasePlayer, String> statusCol = new TableColumn<>("Status");
        statusCol.setMinWidth(180);
        statusCol.setCellValueFactory(data -> {
            BasePlayer p = data.getValue();
            if (p.isInjured()) {
                return new SimpleStringProperty(
                        "🚑 Injured (" + p.getInjuredGamesRemaining() + " games)");
            }
            return new SimpleStringProperty("✅ Fit");
        });

        TableColumn<BasePlayer, String> statsCol = new TableColumn<>("Score/Goals");
        statsCol.setMinWidth(100);
        statsCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getStatsScore())));

        table.getColumns().addAll(nameCol, ageCol, fitnessCol, statusCol, statsCol);
    }

    private void showInjuryReport() {
        Label title = new Label("Match Over!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setStyle(titleColor);

        Label scoreLabel = new Label(myTeamObj.getTeamName() + "  " +
                cumulativeScores[0] + " - " + cumulativeScores[1] +
                "  " + opponentTeamObj.getTeamName());
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scoreLabel.setStyle("-fx-text-fill: #e74c3c;");

        Label injuryTitle = new Label("🚑 Injury Report:");
        injuryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        VBox injuryList = new VBox(5);
        injuryList.setAlignment(Pos.CENTER);

        boolean anyInjury = false;
        for (BasePlayer p : myTeamObj.getPlayers()) {
            if (p.isInjured()) {
                anyInjury = true;
                Label injLabel = new Label("🚑 " + p.getName() +
                        " - injured for " + p.getInjuredGamesRemaining() + " game(s)");
                injLabel.setStyle("-fx-text-fill: #e74c3c;");
                injuryList.getChildren().add(injLabel);
            }
        }
        for (BasePlayer p : opponentTeamObj.getPlayers()) {
            if (p.isInjured()) {
                anyInjury = true;
                Label injLabel = new Label("🚑 " + p.getName() +
                        " (opponent) - " + p.getInjuredGamesRemaining() + " game(s)");
                injLabel.setStyle("-fx-text-fill: #e67e22;");
                injuryList.getChildren().add(injLabel);
            }
        }

        if (!anyInjury) {
            Label noInjury = new Label("✅ No injuries this match!");
            noInjury.setStyle("-fx-text-fill: #27ae60;");
            injuryList.getChildren().add(noInjury);
        }

        Button btnNextWeek = new Button("Next Week →");
        btnNextWeek.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 15px;");
        btnNextWeek.setMinWidth(220);
        btnNextWeek.setOnAction(e -> {
            currentWeek++;
            showWeekScreen();
        });

        Button btnStandings = new Button("View Standings");
        btnStandings.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnStandings.setMinWidth(220);
        btnStandings.setOnAction(e -> showStandingsScreen());

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(title, scoreLabel, injuryTitle, injuryList,
                btnNextWeek, btnStandings);

        Scene scene = new Scene(layout, 600, 500);
        window.setScene(scene);
    }

    private void showStandingsScreen() {
        Label titleLabel = new Label("League Standings");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle(titleColor);

        List<BaseTeam> standings = league.getStandings();

        TableView<BaseTeam> standingsTable = new TableView<>();

        TableColumn<BaseTeam, String> rankCol = new TableColumn<>("#");
        rankCol.setMinWidth(40);
        rankCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(standings.indexOf(data.getValue()) + 1)));

        TableColumn<BaseTeam, String> teamCol = new TableColumn<>("Team Name");
        teamCol.setMinWidth(200);
        teamCol.setStyle("-fx-font-weight: bold;");
        teamCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTeamName()));

        TableColumn<BaseTeam, String> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(80);
        pointsCol.setStyle("-fx-alignment: CENTER;");
        pointsCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getPoints())));

        TableColumn<BaseTeam, String> averageCol = new TableColumn<>("Average");
        averageCol.setMinWidth(80);
        averageCol.setStyle("-fx-alignment: CENTER;");
        averageCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getAverage())));

        standingsTable.getColumns().addAll(rankCol, teamCol, pointsCol, averageCol);

        ObservableList<BaseTeam> data = FXCollections.observableArrayList(standings);
        standingsTable.setItems(data);
        standingsTable.setMaxHeight(320);
        standingsTable.setMaxWidth(450);

        Label weekInfo = new Label("Week: " + currentWeek + " / " +
                (weeklyFixtures != null ? weeklyFixtures.size() : "?"));
        weekInfo.setFont(Font.font("Arial", 14));

        Button btnBack = new Button("Back");
        btnBack.setStyle(mainBtnColor);
        btnBack.setMinWidth(150);
        btnBack.setOnAction(e -> {
            if (weeklyFixtures != null && currentWeek < weeklyFixtures.size()) {
                showWeekScreen();
            } else {
                window.setScene(mainMenuScene);
            }
        });

        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold;");
        btnMainMenu.setMinWidth(150);
        btnMainMenu.setOnAction(e -> window.setScene(mainMenuScene));

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnBack, btnMainMenu);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(titleLabel, weekInfo, standingsTable, buttonBox);

        Scene standingsScene = new Scene(layout, 600, 580);
        window.setScene(standingsScene);
    }

    private void showSeasonEndScreen() {
        Label titleLabel = new Label("🏆 Season Complete!");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #f39c12;");

        List<BaseTeam> standings = league.getStandings();
        BaseTeam champion = standings.get(0);

        Label championLabel = new Label("Champion: " + champion.getTeamName());
        championLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        championLabel.setStyle("-fx-text-fill: #27ae60;");

        Label pointsLabel = new Label("Points: " + champion.getPoints() +
                " | Average: " + champion.getAverage());
        pointsLabel.setFont(Font.font("Arial", 16));

        boolean playerWon = champion.getTeamName().equals(myTeamObj.getTeamName());
        Label resultLabel = new Label(playerWon ?
                "🎉 Congratulations! You won the league!" : "Better luck next season!");
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        resultLabel.setStyle(playerWon ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");

        Button btnStandings = new Button("View Final Standings");
        btnStandings.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnStandings.setMinWidth(220);
        btnStandings.setOnAction(e -> showStandingsScreen());

        Button btnMainMenu = new Button("Back to Main Menu");
        btnMainMenu.setStyle(mainBtnColor);
        btnMainMenu.setMinWidth(220);
        btnMainMenu.setOnAction(e -> window.setScene(mainMenuScene));

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(bgColor);
        layout.getChildren().addAll(titleLabel, championLabel, pointsLabel,
                resultLabel, btnStandings, btnMainMenu);

        Scene scene = new Scene(layout, 550, 450);
        window.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}