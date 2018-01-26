/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speeddialutility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author utente
 */
public class ThumbnailGenerator {
   // Constants constantValues = Constants.getInstance();
    
    SQLliteUtils sqlLiteUtils ;
    // Per creare il processo. Sono solo dei placeholder e vanno correttamente inizializzati nel costruttore
    static final List<String> LAUNCH_LIST = Arrays.asList(
            "phantomjs_EXECUTABLE",
            "rasterize_JAVASCRIPT_TO_BE_LAUCHED",
            "URL_TO_BE_DOWLOADED",
            "OUTPUT_FILE.png",
            "VIEWPORT_1024px*768px"
    );
 
    /**
     *
     * @param phantomJSDir the value of phantomJSDir
     * @param speddialDBPath the value of speddialDBPath
     * @param rasterizeImageParam the value of rasterizeImageParam
     * @param tempFilePath the value of tempFilePath
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ThumbnailGenerator(String phantomJSDir, String speddialDBPath, String rasterizeImageParam, String tempFilePath) throws ClassNotFoundException, SQLException {
        //sqlLiteUtils.startDBConnection("C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db");
        sqlLiteUtils = new SQLliteUtils(Constants.DBPath);
        //sqlLiteUtils.startDBConnection(speddialDBPath);
        
        LAUNCH_LIST.set(0,phantomJSDir+Constants.phantomJSExe);
        LAUNCH_LIST.set(1,phantomJSDir+Constants.phantomJSScript);
        LAUNCH_LIST.set(3,tempFilePath);
        LAUNCH_LIST.set(4,rasterizeImageParam);
    }
    
    /**
     *
     * @throws SQLException
     */
    public void closeDBConnection() throws SQLException{
        sqlLiteUtils.closeDBConnection();
    }

    /**
     *
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    public void grabAllThumbnail() throws SQLException, IOException, InterruptedException {
        ResultSet rs = sqlLiteUtils.readSites();

        int counter = 0;
        while (rs.next()) {
            counter++;
            String url = rs.getString(1);
            LAUNCH_LIST.set(2, url);
            ProcessBuilder pb = new ProcessBuilder(LAUNCH_LIST).redirectErrorStream(true);
            Process proc = pb.start();
            if (!proc.waitFor(Constants.PROCESSWAITSECODS, TimeUnit.SECONDS)) {
                proc.destroy();
            }

            // TODO: sostituire le print con chiamate call back per isolare il modulo
            System.out.print(String.format("%d - Url: %s - ", counter, url));

            if (proc.exitValue() != 0) {
                System.out.println("ERROR");
                sqlLiteUtils.updateUpdateStatusDB(url, Constants.ERROR);
                // Stampo il messaggio di errore
                InputStream is = proc.getInputStream();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));) {
                    // Prendo l'output del processo
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println("\t" + line);
                    }
                }
            } else {
                // Aggiorno il DB
                sqlLiteUtils.updateImageDB(url, LAUNCH_LIST.get(3));
                File file_temp = new File(LAUNCH_LIST.get(3));
                file_temp.delete();
                System.out.println("DONE");
            }

        }
    }

}
