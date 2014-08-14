package csslint;

public class CssLintDataProvider {
    private static CssLintData instance;

    public static CssLintData getInstance () {
        if (instance == null) {
            instance = new CssLintData();
        }

        return instance;
    }
}
