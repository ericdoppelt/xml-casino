package GameView.NodeViews;

import GameView.NodeViews.Interfaces.NodeViewInterface;
import Utility.Formatter;
import engine.player.Player;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class AllPlayersView implements NodeViewInterface {

    private VBox myPlayers;
    private Formatter myFormatter;
    private List<PlayerView>  allPlayers;

    private static final Color backgroundColor = Color.web("3385FF");


    public AllPlayersView() {
        myPlayers = new VBox();
        myFormatter = new Formatter();
        myFormatter.formatUnFixedVBox(myPlayers);
        myFormatter.updateBackground(myPlayers, backgroundColor);
        allPlayers = new ArrayList<>();
    }

    public void addPlayer(String name, int playerID, double bankroll) {
        PlayerView addedPlayerView = new PlayerView(name, playerID, bankroll);
        myPlayers.getChildren().add(addedPlayerView.getView());
        allPlayers.add(addedPlayerView);
    }

    public void removePlayer(int playerID) {
        PlayerView removedPlayerView = getPlayerView(playerID);
        myPlayers.getChildren().remove(removedPlayerView.getView());
        allPlayers.remove(removedPlayerView);
    }

    public PlayerView getPlayerView(int playerID) {
        for (PlayerView tempPlayerView : allPlayers) {
            if (tempPlayerView.hasSameID(playerID)) return tempPlayerView;
            }
        return null;
    }

    @Override
    public VBox getView() {
        return myPlayers;
    }
}