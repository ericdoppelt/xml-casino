package controller.gametype;

import Utility.CardTriplet;
import Utility.Generator;
import Utility.StringPair;
import actions.individual.IndividualAction;
import controller.bundles.ControllerBundle;
import engine.adversary.Adversary;
import engine.bet.Bet;
import engine.dealer.Card;
import engine.player.Player;
import exceptions.ReflectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdversaryController extends Controller {

    private Adversary myAdversary;

    // TODO - refactor into data files (in adversary construction?)
    private static final String ACTION_TYPE = "individual";
    private static final String MINIMUM_TAG = "Minimum";
    private double ADVERSARY_MIN;

    public AdversaryController(ControllerBundle bundle, Map<String, String> params) {
        super(bundle, ACTION_TYPE);
        assignParams(params);
    }

    private void assignParams(Map<String, String> params) {
        this.ADVERSARY_MIN = Double.parseDouble(params.get(MINIMUM_TAG));
    }

    @Override
    public void startGame() {
        promptForEntryBet();
        renderAdversary();
        for (StringPair s: this.myDealerAction) {
            performDealerAction(s);
            updatePlayerHands();
            promptForActions();
            garbageCollect();
        }
        showAllAdversaryCards();
        computePayoffs();
        updateBankrolls();
        showGameViewRestart();
    }

    @Override
    protected void promptForEntryBet() {
        for (Player p: this.myTable.getPlayers()) {
            this.myGameView.setMainPlayer(p.getID());
            double min = this.myTable.getTableMin();
            double max = Math.min(this.myTable.getTableMax(), p.getBankroll());
            double wager = this.myGameView.selectWager(min, max);
            Bet b = this.myTable.placeEntryBet(p.getID(), this.myEntryBet, wager);
            this.myGameView.addBet(new ArrayList<>(), wager, p.getID(), b.getID());
            this.myGameView.setBankRoll(p.getBankroll(), p.getID());
        }
    }

    protected void renderAdversary() {
        this.myAdversary = this.myTable.createAdversary(ADVERSARY_MIN);
        List<CardTriplet> list = Generator.createTripletList(this.myAdversary.getHand());
        this.myGameView.renderAdversary(list);
        showAdversaryCard(this.myAdversary.getCard());
    }

    private void showAdversaryCard(Card c) {
        this.myGameView.showAdversaryCard(c.getID());
    }

    private void showAllAdversaryCards() {
        this.myGameView.renderAdversary(Generator.createTripletList(this.myAdversary.getHand()));
        List<Card> list = this.myAdversary.getHand().getCards();
        for (Card c: list)
            showAdversaryCard(c);
    }

    @Override
    protected void promptForActions() {
        while (this.myTable.hasActivePlayers()) {
            Player p = this.myTable.getNextPlayer();
            System.out.printf("player %s is acting\n", p.getName());
            this.myGameView.setMainPlayer(p.getID());
            cardShow(p);
            try {
                IndividualAction a = this.myFactory.createIndividualAction(this.myGameView.selectAction((ArrayList<String>) this.myPlayerActions));
                Bet b = p.getNextBet();
                a.execute(p, b, this.myTable.getDealCardMethod());
                classifyHand(b);
                addCardToPlayer(p);
                this.myGameView.setWager(b.getWager(), p.getID(), b.getID());
                this.myGameView.setBankRoll(p.getBankroll(), p.getID());
            } catch (ReflectionException e) {
                this.myGameView.displayException(e);
            }
        }
    }

    @Override
    protected void computePayoffs() {
        invokeCompetition();
        this.myHandClassifier.classifyHand(this.myAdversary.getHand());
        for (Player p: this.myTable.getPlayers()) {
            for (Bet b: p.getBets()) {
                this.myBetEvaluator.evaluateHands(b.getHand(), this.myAdversary.getHand());
                System.out.printf("%s's hand is a %s\n", p.getName(), b.getHand().getOutcome().toString());
            }
        }
    }

    private void invokeCompetition() {
        this.myAdversary.playHand(
                (card) -> this.myGameView.showAdversaryCard(card),
                (triplet) -> this.myGameView.addAdversaryCard(triplet),
                this.myTable.getDealCardMethod());
    }

}
