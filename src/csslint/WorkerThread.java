package csslint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;

public class WorkerThread extends Thread {
    private Path path;
    private String data;
    private String output;
    private SettingsStorage storage;
    private boolean res;

    public class CssLintThreadException extends Exception {}

    public WorkerThread(Path path, String data, SettingsStorage storage) {
        this.path = path;
        this.data = data;
        this.storage = storage;
        res = false;
    }

    @Override
    public void run() {
        try {
            File temp = File.createTempFile("csslint_", ".css");
            PrintStream stream = new PrintStream(temp);
            stream.print(data);
            stream.close();

            File workingDir = new File(path.resolve("csslint/node_modules/csslint").toString());
            String ignore = storage.ignoreList();
            if (!ignore.isEmpty()) {
                ignore = "--ignore=" + ignore + " ";
            }
            String command = "node cli.js" + " --quiet --format=compact " + ignore + temp.toString();

            Runtime rt = Runtime.getRuntime();

            Process pr = rt.exec(command, null, workingDir);

            output = convertStreamToString(pr.getInputStream());
            String error = convertStreamToString(pr.getErrorStream());

            pr.waitFor();

            if (error.equals("")) {
                res = true;
            } else {
                System.out.println("CssLint error: " + error);
            }

        } catch (InterruptedException ignored) {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public synchronized String getOutput() throws CssLintThreadException {
        if (!res) {
            throw new CssLintThreadException();
        }
        return output;
    }
}
