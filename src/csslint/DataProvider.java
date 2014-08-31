package csslint;

import java.io.File;
import java.nio.file.Path;

public class DataProvider {
    private static Data instance;

    public static Data getInstance () {
        if (instance == null) {
            instance = new Data();
        }

        return instance;
    }

    private static void deleteDirectory (File tempFile) {
        if (tempFile.isDirectory()) {
            File[] entries = tempFile.listFiles();
            if (entries != null) {
                for (File currentFile : entries) {
                    deleteDirectory(currentFile);
                }
            }
            tempFile.delete();
        } else {
            tempFile.delete();
        }
    }

    public static void disposeResources () {
        Path path = getInstance().getPath();
        File file = new File(path.toString());
        deleteDirectory(file);
    }
}
