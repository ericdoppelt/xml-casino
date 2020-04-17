package actions.group;

import engine.bet.Bet;
import engine.player.Player;

import java.util.function.Consumer;

public class BetAction extends GroupAction {

    public BetAction() {
        super();
        System.out.println("Created a bet action");
    }

    @Override
    public void execute(Player p, Bet b,
                        WagerSelector selectWager, Consumer<Double> setTableBet, Consumer<Bet> activatePlayers,
                        double tableMin, double tableMax, double currentBet) {
        double wager = selectWager.getBet(tableMin, tableMax);
        b.setWager(b.getWager() + wager);
        b.setRoundActive(false);
        activatePlayers.accept(b);
        setTableBet.accept(wager);
    }
}
