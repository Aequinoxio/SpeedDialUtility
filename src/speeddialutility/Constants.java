/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speeddialutility;

/**
 *
 * @author utente
 */
public class Constants {

    public static final String DBPath = "C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db";
    public static final String phantomJSExe = "\\bin\\phantomjs.exe";
    public static final String phantomJSScript = "\\examples\\rasterize.js";
    public static final String phantomJSViewport = "1024px*678px";
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
