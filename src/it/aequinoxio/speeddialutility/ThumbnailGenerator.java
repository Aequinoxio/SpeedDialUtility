/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aequinoxio.speeddialutility;

import Utilities.CustomPreferences;
import Utilities.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author utente
 */
public class ThumbnailGenerator {
    // Constants constantValues = Constants.getInstance();

    SQLliteUtils sqlLiteUtils;
    boolean shouldExit = false;
    // Per creare il processo. Sono solo dei placeholder e vanno correttamente inizializzati nel costruttore
    static final List<String> LAUNCH_LIST = Arrays.asList(
            "phantomjs_EXECUTABLE",
            "rasterize_JAVASCRIPT_TO_BE_LAUCHED",
            "URL_TO_BE_DOWLOADED",
            "OUTPUT_FILE.png",
            "VIEWPORT_1024px*768px"
    );
    private final WorkerCallback workerCallback;

    /**
     *
     * @param workerCallback
     * @param phantomJSPath
     * @param phantomJSDir the value of phantomJSDir
     * @param speddialDBPath the value of speddialDBPath
     * @param rasterizeImageParam the value of rasterizeImageParam
     * @param tempFilePath the value of tempFilePath
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ThumbnailGenerator(WorkerCallback workerCallback, String phantomJSPath, String speddialDBPath, String rasterizeImageParam, String tempFilePath) throws ClassNotFoundException, SQLException {
        // TODO: Eliminare i parametri visto che utilizzo le preferences per la configurazione della classe
//sqlLiteUtils.startDBConnection("C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db");
        CustomPreferences customPreferences = CustomPreferences.getInstance();
        sqlLiteUtils = null;

        LAUNCH_LIST.set(0, customPreferences.getPhantomJSPath());
        LAUNCH_LIST.set(1, customPreferences.getPhantomJSScriptPath());
        LAUNCH_LIST.set(3, customPreferences.getTempFileAbsolutePath());
        LAUNCH_LIST.set(4, customPreferences.getViewPort());
        this.workerCallback = workerCallback;
    }

    /**
     *
     * @throws SQLException
     */
    private void closeDBConnection() throws SQLException {
        sqlLiteUtils.closeDBConnection();
    }

    public void shouldExit() {
        shouldExit = true;
    }

    /**
     * Recupera tutte le url dal file pref.js e scarica le immagini.
     *
     * @throws SQLException
     * @throws IOException
     * @throws java.lang.ClassNotFoundException
     */
    public void grabAllThumbnail() throws SQLException, IOException, ClassNotFoundException {

        if (sqlLiteUtils == null) {
            sqlLiteUtils = new SQLliteUtils(CustomPreferences.getInstance().getDBPath());
        }

        ResultSet rs = sqlLiteUtils.readSites();

        int counter = 0;
        while (rs.next() && !shouldExit) {
            counter++;
            String url = rs.getString(1);
            LAUNCH_LIST.set(2, url);
            ProcessBuilder pb = new ProcessBuilder(LAUNCH_LIST).redirectErrorStream(true);
            Process proc = pb.start();
            try {
                if (!proc.waitFor(Constants.PROCESSWAITSECODS, TimeUnit.SECONDS)) {
                    proc.destroy();
                }
                // TODO: sostituire le print con chiamate call back per isolare il modulo
                System.out.print(String.format("%d - Url: %s - ", counter, url));
                workerCallback.updateWorker(String.format("%d - Url: %s - ", counter, url), counter);

                if (proc.exitValue() != 0) {
                    System.out.println("ERROR");
                    workerCallback.updateWorker("ERROR", counter);

                    sqlLiteUtils.updateUpdateStatusDB(url, Constants.ERROR);
                    // Stampo il messaggio di errore
                    InputStream is = proc.getInputStream();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));) {
                        // Prendo l'output del processo
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println("\t" + line);
                            workerCallback.updateWorker("\t" + line, counter);
                        }
                    }
                } else {
                    // Aggiorno il DB
                    sqlLiteUtils.updateImageDB(url, LAUNCH_LIST.get(3));
                    File file_temp = new File(LAUNCH_LIST.get(3));
                    if (file_temp.delete()) {
                        System.out.println("DONE");
                        workerCallback.updateWorker("DONE", counter);
                    } else {
                        System.out.println("ERROR");
                        workerCallback.updateWorker("ERROR", counter);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ThumbnailGenerator.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print(String.format("Process: %s - terminated with status: %d", pb.toString(), proc.exitValue()));
                workerCallback.updateWorker(String.format("Process: %s - terminated with status: %d", pb.toString(), proc.exitValue()), counter);
            }
        }

        closeDBConnection();
    }

    public void grabSiteThumbnail(String siteUrl) {
        try {
            if (sqlLiteUtils == null) {
                sqlLiteUtils = new SQLliteUtils(CustomPreferences.getInstance().getDBPath());
            }

            LAUNCH_LIST.set(2, siteUrl);
            ProcessBuilder pb = new ProcessBuilder(LAUNCH_LIST).redirectErrorStream(true);
            Process proc = pb.start();
            try {
                if (!proc.waitFor(Constants.PROCESSWAITSECODS, TimeUnit.SECONDS)) {
                    proc.destroy();
                }
                // TODO: sostituire le print con chiamate call back per isolare il modulo
                System.out.print(String.format("%d - Url: %s - ", 0, siteUrl));
                workerCallback.updateWorker(String.format("%d - Url: %s - ", 0, siteUrl), 0);

                if (proc.exitValue() != 0) {
                    System.out.println("ERROR");
                    workerCallback.updateWorker("ERROR", 0);

                    sqlLiteUtils.updateUpdateStatusDB(siteUrl, Constants.ERROR);
                    // Stampo il messaggio di errore
                    InputStream is = proc.getInputStream();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));) {
                        // Prendo l'output del processo
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println("\t" + line);
                            workerCallback.updateWorker("\t" + line, 0);
                        }
                    }
                } else {
                    // Aggiorno il DB
                    sqlLiteUtils.updateImageDB(siteUrl, LAUNCH_LIST.get(3));
                    File file_temp = new File(LAUNCH_LIST.get(3));
                    if (file_temp.delete()) {
                        System.out.println("DONE");
                        workerCallback.updateWorker("DONE", 0);
                    } else {
                        System.out.println("ERROR");
                        workerCallback.updateWorker("ERROR", 0);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ThumbnailGenerator.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print(String.format("Process: %s - terminated with status: %d", pb.toString(), proc.exitValue()));
                workerCallback.updateWorker(String.format("Process: %s - terminated with status: %d", pb.toString(), proc.exitValue()), 0);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ThumbnailGenerator.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print("Some error occurred in reading temp file");
                workerCallback.updateWorker("Some error occurred in reading temp file", 0);
            } catch (SQLException ex) {
                Logger.getLogger(ThumbnailGenerator.class.getName()).log(Level.SEVERE, null, ex);
                System.out.print("Some error occurred in SQL");
                workerCallback.updateWorker("Some error occurred in SQL", 0);

            }

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ThumbnailGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                closeDBConnection();
            } catch (SQLException ex) {
                Logger.getLogger(ThumbnailGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
