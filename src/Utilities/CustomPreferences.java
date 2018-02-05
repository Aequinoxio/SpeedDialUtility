/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.util.prefs.Preferences;

/**
 *
 * @author utente
 */
public class CustomPreferences {

    private static Preferences prefs;
    //static final String PREF_DB_NAME = "SpeedDialUtilitySQLDB"; // Preference key of DBname
    private static final String PREF_DB_PATH = "SpeedDialUtilitySQLDBPath"; // Preference key of DB PATH
    private static final String PREF_FIRST_RUN = "SpeedDialUtilityFirstRun"; // Preference key name
    private static final String PREF_PHANTOMJS_PATH = "PHANTOMJS_PATH";
    private static final String PREF_PHANTOMJS_SCRIPT = "PHANTOMJS_SCRIPT";
    private static final String PREF_PHANTOMJS_VIEWPORT = "PHANTOMJS_VIEWPORT";
    private static final String PREF_TEMP_FILE = "PREF_TEMP_FILE";
    private static final String PREF_TEMP_PATH = "PREF_TEMP_PATH";
    private static final String LATEST_DB_PATH  ="LATEST_DB_PATH";

    private CustomPreferences() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        setDefaultPreferences(false);
    }

    public static CustomPreferences getInstance() {
        return CustomPreferencesHolder.INSTANCE;
    }

    private static class CustomPreferencesHolder {

        private static final CustomPreferences INSTANCE = new CustomPreferences();
    }

    public void resetPreferencesToDefault() {
        CustomPreferences.CustomPreferencesHolder.INSTANCE.setDefaultPreferences(true);
    }

    /**
     * Set the default preferences on first run or reset to the default
     *
     */
    private void setDefaultPreferences(boolean reset) {
        String fr = CustomPreferences.CustomPreferencesHolder.INSTANCE.prefs.get(PREF_FIRST_RUN, "true");
        if (fr.equals("true") || reset) {
            CustomPreferences.CustomPreferencesHolder.INSTANCE.prefs.put(PREF_FIRST_RUN, "false");
            CustomPreferences.prefs.put(PREF_DB_PATH,Constants.DBPath);
            CustomPreferences.prefs.put(PREF_PHANTOMJS_PATH, Constants.phantomJSPath);
            CustomPreferences.prefs.put(PREF_PHANTOMJS_SCRIPT, Constants.phantomJSScriptPath);
            CustomPreferences.prefs.put(PREF_PHANTOMJS_VIEWPORT, Constants.phantomJSViewport);
            CustomPreferences.prefs.put(PREF_TEMP_FILE, Constants.tempFile);
            CustomPreferences.prefs.put(PREF_TEMP_PATH, Constants.tempPath);
        }
    }

    private void setPreference(String key, String value) {
        //setDefaultPreferences();
        prefs.put(key, value);
    }

    public void setDBPath(String DBPath) {
        setPreference(PREF_DB_PATH, DBPath);
    }

    public String getDBPath() {
        //setDefaultPreferences();
        return prefs.get(PREF_DB_PATH, "");
    }

    public String getViewPort() {
        return prefs.get(PREF_PHANTOMJS_VIEWPORT, "");
    }

    public String getPhantomJSPath() {
        return prefs.get(PREF_PHANTOMJS_PATH, "");
    }

    public String getPhantomJSScriptPath() {
        return prefs.get(PREF_PHANTOMJS_SCRIPT, "");
    }
    
    public String getTempFileAbsolutePath() {
        return (prefs.get(PREF_TEMP_PATH, "")+prefs.get(PREF_TEMP_FILE, ""));
    }
    
    public void setLatestFilePath(String filePath){
        setPreference(LATEST_DB_PATH,filePath);
    }
    
    public String getLatestFilePath(){
        return prefs.get(LATEST_DB_PATH,null);
    }
}
