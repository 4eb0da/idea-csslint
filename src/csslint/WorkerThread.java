package csslint;

import com.thoughtworks.xstream.io.path.Path;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class WorkerThread extends Thread {
    private Path path;
    private String data;
    private String output;
    private SettingsStorage storage;
    private boolean res;

    public class ThreadException extends Exception {}

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

            File workingDir = new File(path.apply(new Path("csslint/node_modules/csslint")).toString());
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
                NotificationManager.showError("CSSLint run failure:<br>" + error);
            }

        } catch (InterruptedException ignored) {
        } catch (FileNotFoundException e) {
            NotificationManager.showError("Temp file not found");
        } catch (IOException e) {
            NotificationManager.showError("Cannot process temp file");
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public synchronized String getOutput() throws ThreadException {
        if (!res) {
            throw new ThreadException();
        }
        return output;
    }
}
