/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aequinoxio.speeddialutility;

import Utilities.CustomPreferences;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author utente
 */
public class SpeedDialUtility extends SwingWorker<String, String> implements WorkerCallback {

    // Constants constantValues = Constants.getInstance();
    //String pattern="\\((\\\".*?-\\d+)-(.*)\\\",(.*)\\)";S
    String regexPattern = "\\((\\\"extensions.speeddial.thumbnail-)(\\d+)-(.*)\\\",(.*)\\)";
    String regexpGroupPattern = "\\((\\\"extensions.speeddial.group-)(\\d+)-(.*)\\\",(.*)\\)";

    final static String fileName = "D:\\Mozilla_profiles\\Profiles\\y10v7eee.default-1493351350206\\prefs - Copia del 20171118.js";

    Map<Integer, SpeedDialData> DB;
    Map<Integer, String> DBLABEL;
    Map<Integer, String> DBURL;

    Map<Integer, String> DBGROUPCOLUMNS;
    Map<Integer, String> DBGROUPROWS;
    Map<Integer, String> DBGROUPTITLE;
    Map<Integer, SpeedDialGroupData> DBGROUP;
    private final String SDFilename;
    private final JTextArea textArea;
    ThumbnailGenerator thumbnailGenerator;
    private final UpdateDB updateDB;
    PropertyChangeSupport pcs;

    /**
     * @param args the command line arguments
     */
//    // TODO: parametrizzare il path per il file pref.js
//    // TODO: salvare il thumbnail
//    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        SpeedDialUtility speedDialUtility = new SpeedDialUtility();
//        speedDialUtility.startImport(fileName);
////        SpeedDialUtility speedDialUtility = new SpeedDialUtility();
////        speedDialUtility.readFromFile(fileName);
////        try {
////            speedDialUtility.printDB();
////        } catch (SQLException | ClassNotFoundException ex) {
////            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
////        }
////
////        ThumbnailGenerator thumbnailGenerator;
////        try {
////            thumbnailGenerator = new ThumbnailGenerator(
////                    "C:\\PortableApps\\phantomjs-2.1.1-windows",
////                    Constants.DBPath,
////                    Constants.phantomJSViewport,
////                    "d:\\temp\\ttt.png"
////            );
////            thumbnailGenerator.grabAllThumbnail();
////            thumbnailGenerator.closeDBConnection();
////        } catch (ClassNotFoundException | SQLException | IOException | InterruptedException ex) {
////            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
////        }
//    }
    /**
     *
     * @return @throws ClassNotFoundException
     * @throws SQLException
     */
    @Override
    public String doInBackground() throws ClassNotFoundException, SQLException {
        startImport();
        return "Done!";
    }

    @Override
    protected void done() {
        super.done(); //To change body of generated methods, choose Tools | Templates.
//        try {
//            thumbnailGenerator.closeDBConnection();
//        } catch (SQLException ex) {
//            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     *
     * @param chunks
     */
    @Override
    protected void process(List<String> chunks) {
        for (String chunk : chunks) {
            textArea.append(chunk + "\n");
        }
    }

    private void startImport() throws ClassNotFoundException, SQLException {
        setProgress(0);
        initializeDB();
        readFromFile(SDFilename);
        updateSpeedDialDB();
        
        updateDB.setUI(); // Schifezza. Meglio sarebbe usare un messaggio. COME FARE?
        
       // pcs.firePropertyChange("setStopButton", new Boolean(true),new Boolean(false));
     

        try {
            thumbnailGenerator = new ThumbnailGenerator(
                    this,
                    null, //CustomPreferences.getInstance().getPhantomJSPath(), //"C:\\PortableApps\\phantomjs-2.1.1-windows",
                    null, //Constants.DBPath,
                    null, //Constants.phantomJSViewport,
                    null //"d:\\temp\\ttt.png"
            );
            thumbnailGenerator.grabAllThumbnail();
            //thumbnailGenerator.closeDBConnection();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Utility class for parsing the speeddial prefs.js in Mozilla user folder
     *
     * @param SDFilename
     * @param textArea Textarea pre contenere il log delle attività svolte
     */
    public SpeedDialUtility(String SDFilename, JTextArea textArea, UpdateDB updateDB) {
        this.DBLABEL = new HashMap<>();
        this.DBURL = new HashMap<>();
        this.DB = new HashMap<>();

        this.DBGROUPCOLUMNS = new HashMap<>();
        this.DBGROUPROWS = new HashMap<>();
        this.DBGROUPTITLE = new HashMap<>();
        this.DBGROUP = new HashMap<>();
        this.SDFilename = SDFilename;
        this.textArea = textArea;
        this.updateDB = updateDB;
        PropertyChangeSupport pcs = new PropertyChangeSupport(updateDB);
    }

    /**
     * Update the DB of parsed speeddial urls and groups.
     *
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    private void updateSpeedDialDB() {
        SQLliteUtils sqlLiteUtils = null;
        try {
            sqlLiteUtils = new SQLliteUtils(CustomPreferences.getInstance().getDBPath());

            //sqlLiteUtils.startDBConnection("C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db");
            System.out.println("Total keys:" + DB.size());
            publish("Total keys:" + DB.size());
//        for (Integer key : DB.keySet()) {
//            System.out.println(key);
//            SpeedDialData sdu = DB.get(key);
//            System.out.println("\t" + sdu.label + " -> " + sdu.url);
//        }

//        Integer[] thumbsDBKeys;
//        thumbsDBKeys = DB.keySet().toArray(new Integer[0]);
            int thumbsInGroupFrom = 1;
            int thumbsInGroupTo = 1;
            for (Integer key : DBGROUP.keySet()) {
                //System.out.println(key);
                SpeedDialGroupData sdGD = DBGROUP.get(key);
                thumbsInGroupTo += sdGD.rows * sdGD.columns;
                System.out.println(String.format(
                        "%s -> (%d,%d)", sdGD.title, sdGD.rows, sdGD.columns)
                );
                publish(String.format("%s -> (%d,%d)", sdGD.title, sdGD.rows, sdGD.columns));

                sqlLiteUtils.insertGroup(sdGD.title, sdGD.rows, sdGD.columns);

                for (int i = thumbsInGroupFrom; i < thumbsInGroupTo; i++) {
                    if (DB.containsKey(i)) {
                        System.out.println(String.format("\t%d - %s -> %s - %s", i, sdGD.title, DB.get(i).label, DB.get(i).url));
                        publish(String.format("\t%d - %s -> %s - %s", i, sdGD.title, DB.get(i).label, DB.get(i).url));

                        sqlLiteUtils.insertSite(DB.get(i).url, DB.get(i).label, null, sdGD.title);
                    }
                }
                thumbsInGroupFrom = thumbsInGroupTo;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (sqlLiteUtils != null) {
                try {
                    sqlLiteUtils.closeDBConnection();
                } catch (SQLException ex) {
                    Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Reset the SpeedDial Database recreating the empty tables
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void initializeDB() throws ClassNotFoundException, SQLException {
        SQLliteUtils sqlLiteUtils = new SQLliteUtils(CustomPreferences.getInstance().getDBPath());
        sqlLiteUtils.resetDB();
        sqlLiteUtils.closeDBConnection();
    }

    /**
     * Read the speeddials prefs.js and generate a local DB that be imported
     * with updateSpeedDialDB method
     *
     * @param fileName The filename of the SpeedDial data to be read
     */
    private void readFromFile(String fileName) {
        Pattern p = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
        Pattern pGroup = Pattern.compile(regexpGroupPattern, Pattern.CASE_INSENSITIVE);
        Matcher m;
        Matcher mGroup;

        int counter = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                        new File(fileName)), StandardCharsets.UTF_8))) {
            String linea;
            String linea_temp;
            while ((linea_temp = br.readLine()) != null) {

                m = p.matcher(linea_temp);
                if (m.find()) {
                    linea = linea_temp.replaceAll("\"", "");
                    if (linea.contains("-url")) {
                        //   System.out.println("\t* " + linea);
                        counter++;
                    }
                    Integer key = Integer.valueOf(m.group(2).trim());
                    String value = m.group(4).trim();
                    if (m.group(3).equals("label")) {
                        DBLABEL.put(key, value.replaceAll("\"", ""));
                        continue;
                    }

                    if (m.group(3).equals("url")) {
                        DBURL.put(key, value.replaceAll("\"", ""));
                        continue;
                    }
                    //System.out.println(m.group(1) + " - " + m.group(2) + " - " + m.group(3));
                }
                mGroup = pGroup.matcher(linea_temp);
                if (mGroup.find()) {

//                    if (linea.contains("-title")) {
//                     //   System.out.println("\t* " + linea);
//
//                    }
                    Integer key = Integer.valueOf(mGroup.group(2).trim());
                    String value = mGroup.group(4).trim();
                    if (mGroup.group(3).equals("title")) {
                        DBGROUPTITLE.put(key, value.replaceAll("\"", ""));
                        continue;
                    }

                    if (mGroup.group(3).equals("rows")) {
                        DBGROUPROWS.put(key, value);
                        continue;
                    }

                    if (mGroup.group(3).equals("columns")) {
                        DBGROUPCOLUMNS.put(key, value);
                        continue;
                    }

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        DBLABEL.keySet().forEach((key) -> {
            String valueLBL = DBLABEL.get(key);
            String valueURL = DBURL.get(key);
            SpeedDialData sdu = new SpeedDialData(valueLBL, valueURL);

            DB.put(key, sdu);
        });

        int thumbsInGroupTot = 0;
        for (Integer key : DBGROUPTITLE.keySet()) {
            String groupTitleLBL = DBGROUPTITLE.get(key);
            Integer groupRowsLBL = Integer.valueOf(DBGROUPROWS.get(key));
            Integer groupColumnsLBL = Integer.valueOf(DBGROUPCOLUMNS.get(key));
            thumbsInGroupTot += groupRowsLBL * groupColumnsLBL;
            SpeedDialGroupData sdGD = new SpeedDialGroupData(groupTitleLBL, groupRowsLBL, groupColumnsLBL);

            DBGROUP.put(key, sdGD);
        }

        System.out.println(
                String.format("Parsed: %d entries, %d thumbs in groups", counter, thumbsInGroupTot)
        );
        publish(String.format("Parsed: %d entries, %d thumbs in groups", counter, thumbsInGroupTot));
    }

    @Override
    public void updateWorker(String updateValues, int progress) {
        publish(updateValues);
        setProgress((int) (1.0 * progress / getMaxEntries() * 100.0));
    }

    public int getMaxEntries() {
        return (DB.size());
    }

    public void shouldExit() {
        thumbnailGenerator.shouldExit();
        //this.cancel(true);
    }

    private static class SpeedDialData {
        SpeedDialData(String label, String url) {
            this.label = label;
            this.url = url;
        }
        protected String label;
        protected String url;
    }

    private static class SpeedDialGroupData {

        SpeedDialGroupData(String title, Integer rows, Integer columns) {
            this.title = title;
            this.rows = rows;
            this.columns = columns;
        }
        protected String title;
        protected Integer rows;
        protected Integer columns;
    }
}
