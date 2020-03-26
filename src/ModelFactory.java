public class ModelFactory {

    private static Model model;

    /*
     Calling the static method getModel returns an initialised Model object.
     */
    public static Model getModel() {
        if (model == null) model = new Model();
        return model;
    }
}
