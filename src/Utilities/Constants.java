/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author utente
 */
public class Constants {
    // Variabili accedibili solo dal package
    static final String PrefPath="it.aequinoxio.speeddialutility";
    static final String DBPath = "C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db";    
    static final String phantomJSDir = "C:\\PortableApps\\phantomjs-2.1.1-windows";
    static final String phantomJSExe = "phantomjs.exe";
    static final String phantomJSPath = phantomJSDir+"\\bin\\"+phantomJSExe;
    static final String phantomJSScriptPath = phantomJSDir+"\\examples\\rasterize.js";
    static final String phantomJSViewport = "1024px*678px";
    static final String tempPath = "d:\\temp"; // File temporaneo per salvare le immagini
    static final String tempFile = "\\speddial_thumbnail.png"; // File temporaneo per salvare le immagini
    
    
    // Variabili accedibili da tutti
    public static final int PROCESSWAITSECODS = 60;
    public static final int ERROR = 1;
    public static final int OK = 0;

//    public int getProcessWaitSeconds() {
//        return PROCESSWAITSECODS;
//    }
//    
//    private Constants() {
//    }
//    
//    public static Constants getInstance() {
//        return ConstantsHolder.INSTANCE;
//    }
//
//    public String getDBPath() {
//        return DBPath;
//    }
//
//    public String getPhantomJSExe() {
//        return phantomJSExe;
//    }
//
//    public String getPhantomJSScript() {
//        return phantomJSScript;
//    }
//
//    public String getPhantomJSViewport() {
//        return phantomJSViewport;
//    }
    private static class ConstantsHolder {

        private static final Constants INSTANCE = new Constants();
    }
}
