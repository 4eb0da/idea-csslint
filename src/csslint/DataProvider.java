package csslint;

public class DataProvider {
    private static Data instance;

    public static Data getInstance () {
        if (instance == null) {
            instance = new Data();
        }

        return instance;
    }
}
