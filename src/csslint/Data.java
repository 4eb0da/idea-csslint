package csslint;

import org.apache.sanselan.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Data {

    private static Path csslintPath;

    private static final String[] files = {
            "csslint/node_modules/csslint/cli.js",
            "csslint/node_modules/csslint/lib/csslint-node.js",
            "csslint/node_modules/csslint/node_modules/parserlib/package.json",
            "csslint/node_modules/csslint/node_modules/parserlib/lib/node-parserlib.js"
    };

    public Data() {
        try {
            csslintPath = Files.createTempDirectory(Bundle.message("datadir.prefix"));

            for (String file : files) {
                String dirName = Paths.get(csslintPath.toString(), file).getParent().toString();
                File fileDir = new File(dirName);
                if (!fileDir.exists()) {
                    boolean res = fileDir.mkdirs();
                    if (!res) {
                        NotificationManager.showError("Cannot create folder: " + dirName);
                        return;
                    }
                }
                InputStream stream = getClass().getClassLoader().getResourceAsStream(file);
                if (stream == null) {
                    NotificationManager.showError("Cannot extract file: " + file);
                    return;
                }
                FileOutputStream output = new FileOutputStream(csslintPath.toString() + "/" + file);
                IOUtils.copyStreamToStream(stream, output);
            }
        } catch (IOException e) {
            NotificationManager.showError("Resources error: " + e.getMessage());
        }
    }

    public Path getPath() {
        return csslintPath;
    }
}
