package csslint;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

public class CSSLintApplicationComponent implements ApplicationComponent {
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
        return "CSSLintApplicationComponent";
    }
}
