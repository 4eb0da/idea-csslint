package csslint;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CSSLintProjectComponent implements ProjectComponent {

    private SettingsStorage storage;

    public CSSLintProjectComponent (Project project) {
        storage = new SettingsStorage(project);
    }

    public SettingsStorage settingsStorage() {
        return storage;
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {
        DataProvider.disposeResources();
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "CSSLintProjectComponent";
    }
}
