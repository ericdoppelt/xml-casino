package ooga;

import UI.GameView.GameView;
import Utility.StringPair;
import controller.*;
import engine.dealer.Dealer;
import engine.dealer.Deck;
import engine.evaluator.bet.BetEvaluator;
import engine.evaluator.handclassifier.HandClassifier;
import engine.evaluator.handevaluator.HandEvaluator;
import engine.player.Player;
import engine.player.PlayerList;
import engine.table.Table;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import xmlreader.readers.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GameConstructor {

    private final File deckFile;
    private final File gameFile;
    private final File playerFile;
    private final File handFile ;
    private final File viewFile;

    private static final String DECK_KEY = "deck";
    private static final String GAME_KEY = "game";
    private static final String PLAYER_KEY = "player";
    private static final String HAND_KEY = "hand";
    private static final String VIEW_KEY = "view";

    public GameConstructor(Map<String, File> myFiles) {
        deckFile = myFiles.get(DECK_KEY);
        gameFile = myFiles.get(GAME_KEY);
        playerFile = myFiles.get(PLAYER_KEY);
        handFile = myFiles.get(HAND_KEY);
        viewFile = myFiles.get(VIEW_KEY);
        createGame();
    }

    private void createGame() {
        try {
            GameReader gameReader = new GameReader(gameFile);
            HandReader handReader = new HandReader(handFile);
            PlayerReader playerReader = new PlayerReader(playerFile);
            ViewReader viewReader = new ViewReader(viewFile);
            DeckReader deckReader = new DeckReader(deckFile);

            Table myTable = constructTable(gameReader, playerReader, deckReader);
            GameView myGameView = constructGameView(viewReader);
            // TODO - this is view related, worked in main because that had a primary stage but now we don't
            // Maybe pass primary stage in?
//        primaryStage.setScene(new Scene(myGameView.getView(), viewReader.getScreenWidth(), viewReader.getScreenWidth()));
//        primaryStage.show();

            Controller myController = constructController(gameReader, handReader, myTable, myGameView);
            myController.startGame();
        } catch (SAXException | ParserConfigurationException |  IOException e) {
            System.out.println("sorry, could not create game at this time");
        }

    }

    private static Collection<Player> createPlayerList(PlayerReader playerReader) {
        Collection<Utility.Pair> playerCollection = playerReader.getPlayers();
        PlayerList myPlayers = new PlayerList(playerCollection);
        return myPlayers.getPlayers();
    }

    private static Table constructTable(GameReader gameReader, PlayerReader playerReader, DeckReader deckReader) {
        Collection<Player> playerList = createPlayerList(playerReader);
        List<StringPair> deckList = deckReader.getDeck();
        double tableMin = gameReader.getTableMin();
        double tableMax = gameReader.getTableMax();
        Deck myDeck = new Deck(deckList);
        Dealer myDealer = new Dealer(myDeck);
        Table myTable = new Table(playerList, myDealer, tableMin, tableMax);

        return myTable;
    }

    private static Controller constructController(GameReader gameReader, HandReader handReader, Table myTable, GameView myGameView) {

        Collection<String> myPlayerActions = gameReader.getPlayerAction();
        StringPair myDealerAction = gameReader.getDealerAction();
        Collection<String> myWinningHands = handReader.getWinningHands();
        Collection<String> myLosingHands = handReader.getLosingHands();
        HandClassifier myHandClassifier = new HandClassifier(myWinningHands, myLosingHands);
        HandEvaluator myHandEvaluator = new HandEvaluator();
        BetEvaluator myBetEvaluator = new BetEvaluator(myHandEvaluator);

        // TODO - add validation to enum constants
        EntryBet myEntryBet = EntryBet.valueOf(gameReader.getEntryBet().toUpperCase());
        Competition myCompetition = Competition.valueOf(gameReader.getCompetition().toUpperCase());
        Cardshow myCardShow = Cardshow.valueOf(gameReader.getCardShow().toUpperCase());
        Goal myGoal = Goal.valueOf(gameReader.getGoal().toUpperCase());

        return new Controller(myTable, myGameView, myEntryBet, myPlayerActions, myDealerAction,
                myHandClassifier, myBetEvaluator,
                myCompetition, myCardShow, myGoal);
    }

    // TODO - give game view parameters form the XML file
    private static GameView constructGameView(ViewReader viewReader) {
        GameView gameView = new GameView();
        Stage newGameStage = new Stage();
        newGameStage.setScene(new Scene(gameView.getView(), viewReader.getScreenWidth(), viewReader.getScreenWidth()));
        gameView.renderTable("StandardBJTable.jpeg");
        newGameStage.show();
        return gameView;
    }

}