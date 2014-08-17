package csslint;

import com.intellij.ide.DataManager;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;

import java.util.HashMap;

public class SettingsStorage {
    private static final String[] keys = {
        "important", "adjoining-classes", "known-properties", "box-sizing", "box-model", "overqualified-elements",
        "display-property-grouping", "bulletproof-font-face", "compatible-vendor-prefixes", "regex-selectors", "errors",
        "duplicate-background-images", "duplicate-properties", "empty-rules", "selector-max-approaching", "gradients",
        "fallback-colors", "font-sizes", "font-faces", "floats", "star-property-hack", "outline-none", "import", "ids",
        "underscore-property-hack", "rules-count", "qualified-headings", "selector-max", "shorthand", "text-indent",
        "unique-headings", "universal-selector", "unqualified-attributes", "vendor-prefix", "zero-units"
    };

    private HashMap<String, Boolean> values;

    private PropertiesComponent properties;

    public SettingsStorage (Project project) {
        properties = PropertiesComponent.getInstance(project);

        useWaitLimit = properties.getBoolean("csslint.useWaitLimit", true);
        waitLimit = properties.getOrInitInt("csslint.waitLimit", 5000);

        values = new HashMap<String, Boolean>();
        for (String key : keys) {
            values.put(key, properties.getBoolean("csslint.values." + key, true));
        }
    }

    public void setWaitLimit (int waitLimit) {
        this.waitLimit = waitLimit;
    }

    public void setUseWaitLimit (boolean useWaitLimit) {
        this.useWaitLimit = useWaitLimit;
    }

    public boolean useWaitLimit () {
        return useWaitLimit;
    }

    public int waitLimit () {
        return waitLimit;
    }

    public boolean value (String key) {
        if (values.containsKey(key)) {
            return values.get(key);
        }
        System.out.println("Unknown value: " + key);
        return false;
    }

    public void setValue (String key, boolean value) {
        values.put(key, value);
    }

    public void save () {
        properties.setValue("csslint.useWaitLimit", String.valueOf(useWaitLimit));
        properties.setValue("csslint.waitLimit", String.valueOf(waitLimit));

        for (String key : keys) {
            properties.setValue("csslint.values." + key, String.valueOf(values.get(key)));
        }
    }

    public synchronized String ignoreList () {
        String res = "";

        for (String key : values.keySet()) {
            if (!values.get(key)) {
                if (!res.isEmpty()) {
                    res += ",";
                }
                res += key;
            }
        }

        return res;
    }

    private boolean useWaitLimit;

    private int waitLimit;
}
