package csslint;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Settings implements Configurable {
    private JSpinner waitLimit;
    private JCheckBox importantCheckBox;
    private JCheckBox adjoiningClassesCheckBox;
    private JCheckBox knownPropertiesCheckBox;
    private JCheckBox boxSizingCheckBox;
    private JCheckBox boxModelCheckBox;
    private JCheckBox overqualifiedElementsCheckBox;
    private JCheckBox displayPropertyGroupingCheckBox;
    private JCheckBox bulletproofFontFaceCheckBox;
    private JCheckBox compatibleVendorPrefixesCheckBox;
    private JCheckBox regexSelectorsCheckBox;
    private JCheckBox errorsCheckBox;
    private JCheckBox duplicateBackgroundImagesCheckBox;
    private JPanel rootPanel;
    private JCheckBox useWaitLimitCheckBox;
    private JCheckBox duplicatePropertiesCheckBox;
    private JCheckBox emptyRulesCheckBox;
    private JCheckBox selectorMaxApproachingCheckBox;
    private JCheckBox gradientsCheckBox;
    private JCheckBox fallbackColorsCheckBox;
    private JCheckBox fontSizesCheckBox;
    private JCheckBox fontFacesCheckBox;
    private JCheckBox floatsCheckBox;
    private JCheckBox starPropertyHackCheckBox;
    private JCheckBox outlineNoneCheckBox;
    private JCheckBox importCheckBox;
    private JCheckBox idsCheckBox;
    private JCheckBox underscorePropertyHackCheckBox;
    private JCheckBox rulesCountCheckBox;
    private JCheckBox qualifiedHeadingsCheckBox;
    private JCheckBox selectorMaxCheckBox;
    private JCheckBox shorthandCheckBox;
    private JCheckBox textIndentCheckBox;
    private JCheckBox uniqueHeadingsCheckBox;
    private JCheckBox universalSelectorCheckBox;
    private JCheckBox unqualifiedAttributesCheckBox;
    private JCheckBox vendorPrefixCheckBox;
    private JCheckBox zeroUnitsCheckBox;

    private JCheckBox[] checkBoxes;

    private SettingsStorage storage;

    public Settings (final Project project) {
        storage = project.getComponent(CSSLintProjectComponent.class).settingsStorage();

        useWaitLimitCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                waitLimit.setEnabled(useWaitLimitCheckBox.isSelected());
            }
        });
        
        useWaitLimitCheckBox.setSelected(storage.useWaitLimit());
        waitLimit.setValue(storage.waitLimit());
        waitLimit.setEnabled(useWaitLimitCheckBox.isSelected());

        checkBoxes = new JCheckBox[]{
            importantCheckBox, adjoiningClassesCheckBox, knownPropertiesCheckBox, boxSizingCheckBox, boxModelCheckBox,
            overqualifiedElementsCheckBox, displayPropertyGroupingCheckBox, bulletproofFontFaceCheckBox,
            compatibleVendorPrefixesCheckBox, regexSelectorsCheckBox, errorsCheckBox, duplicateBackgroundImagesCheckBox,
            duplicatePropertiesCheckBox, emptyRulesCheckBox, selectorMaxApproachingCheckBox, gradientsCheckBox,
            fallbackColorsCheckBox, fontSizesCheckBox, fontFacesCheckBox, floatsCheckBox, starPropertyHackCheckBox,
            outlineNoneCheckBox, importCheckBox, idsCheckBox, underscorePropertyHackCheckBox, rulesCountCheckBox,
            qualifiedHeadingsCheckBox, selectorMaxCheckBox, shorthandCheckBox, textIndentCheckBox,
            uniqueHeadingsCheckBox, universalSelectorCheckBox, unqualifiedAttributesCheckBox, vendorPrefixCheckBox,
            zeroUnitsCheckBox
        };

        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(storage.value(checkBox.getText()));
        }
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "CSSLint";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return rootPanel;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        storage.setUseWaitLimit(useWaitLimitCheckBox.isSelected());
        storage.setWaitLimit((Integer) waitLimit.getValue());

        for (JCheckBox checkBox : checkBoxes) {
            storage.setValue(checkBox.getText(), checkBox.isSelected());
        }

        storage.save();
    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }
}
