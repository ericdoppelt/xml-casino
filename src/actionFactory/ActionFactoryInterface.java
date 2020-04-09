package actionFactory;

public interface ActionFactoryInterface {

    /**
     * Uses reflection to create the desired action
     * @param action is the string name denoting the appropriate action to create
     * @return the appropriate action (specified by String input
     */
    Action createAction(String action);
}