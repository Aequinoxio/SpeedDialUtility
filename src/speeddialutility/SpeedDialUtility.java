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
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author utente
 */
public class SpeedDialUtility {

   // Constants constantValues = Constants.getInstance();
    //String pattern="\\((\\\".*?-\\d+)-(.*)\\\",(.*)\\)";S
    String regexPattern = "\\((\\\"extensions.speeddial.thumbnail-)(\\d+)-(.*)\\\",(.*)\\)";
    String regexpGroupPattern = "\\((\\\"extensions.speeddial.group-)(\\d+)-(.*)\\\",(.*)\\)";

    static String fileName = "D:\\Mozilla_profiles\\Profiles\\y10v7eee.default-1493351350206\\prefs - Copia del 20171118.js";

    Map<Integer, SpeedDialData> DB;
    Map<Integer, String> DBLABEL;
    Map<Integer, String> DBURL;

    Map<Integer, String> DBGROUPCOLUMNS;
    Map<Integer, String> DBGROUPROWS;
    Map<Integer, String> DBGROUPTITLE;
    Map<Integer, SpeedDialGroupData> DBGROUP;

    /**
     * @param args the command line arguments
     */
    // TODO: parametrizzare il path per il file pref.js
    // TODO: salvare il thumbnail
    public static void main(String[] args) {

        SpeedDialUtility speedDialUtility = new SpeedDialUtility();
        speedDialUtility.readFromFile(fileName);
        try {
            speedDialUtility.printDB();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

        ThumbnailGenerator thumbnailGenerator;
        try {
            thumbnailGenerator = new ThumbnailGenerator(
                    "C:\\PortableApps\\phantomjs-2.1.1-windows",
                    Constants.DBPath,
                    Constants.phantomJSViewport,
                    "d:\\temp\\ttt.png"
            );
            thumbnailGenerator.grabAllThumbnail();
            thumbnailGenerator.closeDBConnection();
        } catch (ClassNotFoundException | SQLException | IOException | InterruptedException ex) {
            Logger.getLogger(SpeedDialUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Utility class for parsing the speeddial prefs.js in Mozilla user folder
     */
    public SpeedDialUtility() {
        this.DBLABEL = new HashMap<>();
        this.DBURL = new HashMap<>();
        this.DB = new HashMap<>();

        this.DBGROUPCOLUMNS = new HashMap<>();
        this.DBGROUPROWS = new HashMap<>();
        this.DBGROUPTITLE = new HashMap<>();
        this.DBGROUP = new HashMap<>();
    }

    /**
     * Print the DB of parsed speeddial urls and groups
     */
    public void printDB() throws SQLException, ClassNotFoundException {
        SQLliteUtils sqlLiteUtils = new SQLliteUtils(Constants.DBPath);
        //sqlLiteUtils.startDBConnection("C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db");
        System.out.println("Total keys:" + DB.size());
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
            sqlLiteUtils.insertGroup(sdGD.title, sdGD.rows, sdGD.columns);

            for (int i = thumbsInGroupFrom; i < thumbsInGroupTo; i++) {
                if (DB.containsKey(i)) {
                    System.out.println(String.format("\t%d - %s -> %s - %s", i, sdGD.title, DB.get(i).label, DB.get(i).url));
                    sqlLiteUtils.insertSite(DB.get(i).url, DB.get(i).label, null, sdGD.title);
                }
            }
            thumbsInGroupFrom = thumbsInGroupTo;
        }
        sqlLiteUtils.closeDBConnection();
    }

    /**
     * Read the speeddials prefs.js
     *
     * @param fileName The filename to be read
     */
    public void readFromFile(String fileName) {
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
