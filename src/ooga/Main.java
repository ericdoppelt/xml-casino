package ooga;

import GameView.NodeViews.GameView;
import controller.Controller;
import data.xmlreader.GameReader;
import data.xmlreader.Pair;
import data.xmlreader.PlayerReader;
import engine.dealer.Dealer;
import engine.dealer.Deck;
import engine.evaluator.BetEvaluator;
import engine.evaluator.HandClassifier;
import engine.evaluator.HandEvaluator;
import engine.player.Player;
import engine.player.PlayerList;
import engine.table.Table;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    private static final String gameFile = "src/data/game/blackjackGame.xml";
    private static final String playerFile = "src/data/players/players.xml";

    private static List<Player> createPlayerList() throws ParserConfigurationException, SAXException, IOException {
        PlayerReader playerReader = new PlayerReader(playerFile);
        Map<String, Double> playerMap = playerReader.getPlayers();
        PlayerList myPlayers = new PlayerList(playerMap);
        return myPlayers.getPlayers();
    }

    private static void traverseList(List<String> list) {
        for (String s: list) {
            System.out.println(s);
        }
    }

    private static Table constructTable(GameReader myReader) throws IOException, SAXException, ParserConfigurationException {
        List<Player> playerList = createPlayerList();
        List<Pair> deckList = myReader.getDeck();
        Deck myDeck = new Deck(deckList);
        Dealer myDealer = new Dealer(myDeck);
        Table myTable = new Table(playerList, myDealer);

        return myTable;
    }

    private static Controller constructController(GameReader myReader, Table myTable, GameView myGameView) {
        String myEntryBet = myReader.getEntryBet();
        List<String> myPlayerActions = myReader.getPlayerAction();
        Pair myDealerAction = myReader.getDealerAction();
        List<String> myWinningHands = myReader.getWinningHands();
        List<String> myLosingHands = myReader.getLosingHands();
        HandClassifier myHandClassifier = new HandClassifier(myWinningHands, myLosingHands);
        HandEvaluator myHandEvaluator = new HandEvaluator();
        BetEvaluator myBetEvaluator = new BetEvaluator(myHandEvaluator);
        String myCompetition = myReader.getCompetition();

        return new Controller(myTable, myGameView, myEntryBet, myPlayerActions, myDealerAction, myHandClassifier, myBetEvaluator, myCompetition);
    }

    // TODO - give game view parameters form the XML file
    private static GameView constructGameView() {
        GameView gameView = new GameView();
        gameView.renderTable("StandardBJTable.jpeg");
        return gameView;
    }

    // TODO - refactor game code into non static objects, currently running through main must be static
    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws ParserConfigurationException, SAXException, IOException {
        GameReader myReader = new GameReader(gameFile);
        Table myTable = constructTable(myReader);
        GameView myGameView = constructGameView();
        primaryStage.setScene(new Scene(myGameView.getView(), 800, 800));
        primaryStage.show();

        Controller myController = constructController(myReader, myTable, myGameView);
        myController.startGame();
    }
}
