package csslint;

import com.intellij.openapi.util.TextRange;

public class Issue {

    public enum Type {
        Error,
        Warning
    }

    public Issue(TextRange range, Type type, String message) {
        this.range = range;
        this.type = type;
        this.message = message;
    }

    public TextRange getRange() {
        return range;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return "CssLintIssue <" + type + ", location: " + range + ", " + message + ">";
    }

    private TextRange range;

    private Type type;

    private String message;
}
