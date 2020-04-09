package controller;

import data.xmlreader.Pair;
import engine.bet.Bet;
import engine.player.Player;
import engine.table.Table;

import java.util.List;

public class Controller implements ControllerInterface {

    private Table myTable;
    private final String myEntryBet;
    private final List<String> myPlayerActions;
    private final Pair myDealerAction;

    // TODO - construct controller with a view object
    public Controller(Table table, String entryBet, List<String> playerActions, Pair dealerAction) {
        this.myTable = table;
        this.myEntryBet = entryBet;
        this.myPlayerActions = playerActions;
        this.myDealerAction = dealerAction;
    }

    // TODO - place entry bet and perform player action inside of the view, register inside the model
    public void startGame() {
        this.myTable.placeEntryBet(this.myEntryBet, (bet) -> this.acceptBet(bet));
        this.myTable.performDealerAction(this.myDealerAction);
        loopPlayerActions();

    }

    private void loopPlayerActions() {
        for (int i = 0; i < this.myTable.totalPlayers(); i ++) {
            // TODO - prompt action to be performed on front end
            // this.myTable.performPlayerAction(this.myPlayerActions, (action) -> this.acceptAction(action));
        }
    }

    @Override
    public void acceptBet(Bet bet) {

    }

    @Override
    public void acceptAction(String action) {

    }


    /** Max's team code for reflection example within their execution (shows how to invoke a method)
     *
     *     private List<String> executeCommand(Command command) {
     *         try {
     *             Class superclazz = command.getClass().getSuperclass();
     *             String name = EXECUTE + superclazz.getSimpleName();
     *             Method method = this.getClass().getDeclaredMethod(name, superclazz); //Command.class
     *             Object o = method.invoke(this, command);
     *             return (List<String>) o;
     *         } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | NullPointerException e) {
     *             throw new ReflectionException("Unable to apply Reflection in parser");
     *         }
     *     }
     */


}