/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aequinoxio.speeddialutility;

import Utilities.Constants;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author utente
 */
public class SQLliteUtils {

    //Constants constantValues = Constants.getInstance();
    private static final int SIZE = 1024;
    Connection DBConnection = null;
    private static final String DROP_TABLE_SITES  = "DROP TABLE IF EXISTS sites";
    private static final String DROP_TABLE_GROUPS = "DROP TABLE IF EXISTS groups ";
    private static final String CREATE_TABLE_SITES = "CREATE TABLE sites ( `url` TEXT, `label` TEXT, `thumbnail` BLOB, `IDGroup` INTEGER, `ID` INTEGER, `last_update` TEXT DEFAULT CURRENT_TIMESTAMP, `update_status` INTEGER, PRIMARY KEY(ID) )";
    private static final String CREATE_TABLE_GROUPS = "CREATE TABLE groups ( `ID` INTEGER, `title` TEXT UNIQUE, `columns` INTEGER, `rows` INTEGER, `size` INTEGER, PRIMARY KEY(ID) )";

    /**
     *
     * @param DBPath the value of DBPath
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public SQLliteUtils(String DBPath) throws ClassNotFoundException, SQLException {
// TODO:impostare la start db connection alla creazione della classe ed eliminare il metodo startdbconnection
        startDBConnection(DBPath);
    }

    /**
     * Avvio la connessione al DB
     *
     * @param DBPath
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void startDBConnection(String DBPath) throws ClassNotFoundException, SQLException {
        if (DBConnection == null) {
            Class.forName("org.sqlite.JDBC");
            DBConnection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
        }
    }

    /**
     * Chiudo la connessione
     *
     * @throws SQLException
     */
    public void closeDBConnection() throws SQLException {
        if (DBConnection != null) {
            DBConnection.close();
        }
    }

    public void resetDB() throws SQLException {
        PreparedStatement stmt;
        
        // Cancello tutto
        stmt = DBConnection.prepareStatement(DROP_TABLE_SITES);
        stmt.executeUpdate();
        stmt = DBConnection.prepareStatement(DROP_TABLE_GROUPS);
        stmt.executeUpdate();

        // Ricreo tutto
        stmt = DBConnection.prepareStatement(CREATE_TABLE_SITES);
        stmt.executeUpdate();
        stmt = DBConnection.prepareStatement(CREATE_TABLE_GROUPS);
        stmt.executeUpdate();
    }

    /**
     * Inserisco il gruppo specifico nella tabella dei gruppi
     *
     * @param title
     * @param rows
     * @param columns
     * @throws SQLException
     */
    public void insertGroup(String title, int rows, int columns) throws SQLException {
        PreparedStatement stmt;
        String sql = "insert into groups (title, rows, columns, size) values (?,?,?,?)";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setString(1, title);
        stmt.setInt(2, rows);
        stmt.setInt(3, columns);
        stmt.setInt(4, rows * columns);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Inserisco il sito collegandolo al gruppo indicato nel file di SpeedDial
     *
     * @param url Url del sito da inserire
     * @param label Titolo dell'url
     * @param thumbnail Immagine del sito
     * @param IDGroup ID del gruppo come presente nel DB a cui collegare il sito
     * @throws SQLException
     */
    public void insertSite(String url, String label, Object thumbnail, int IDGroup) throws SQLException {
        PreparedStatement stmt;
        String sql = "insert into sites (url, label, thumbnail, IDGroup) values (?,?,?,?)";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setString(1, url);
        stmt.setString(2, label);
        stmt.setString(3, "");
        stmt.setInt(4, IDGroup);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Inserisco il sito collegandolo al gruppo corrispondente al "groupTitle".
     * Il "groupTitle" deve essere unico, altrimenti lo collego al primo che
     * trovo TODO: Da irrobustire l'associazione sito - gruppo
     *
     * @param url Url del sito da inserire
     * @param label itolo dell'url
     * @param thumbnail Immagine del sito
     * @param groupTitle Nome del gruppo come presente nel DB a cui collegare il
     * sito
     * @throws SQLException
     */
    public void insertSite(String url, String label, Object thumbnail, String groupTitle) throws SQLException {
        ResultSet groupRS = readGroup(groupTitle);
        if (groupRS.next()) {
            insertSite(url, label, thumbnail, groupRS.getInt("ID"));
        } else {
            throw new SQLException("Unexistent group");
        }
    }

    /**
     * Cerca nel DB tutti i gruppi corrispondenti TODO: sostituire con un metodo
     * che ritorni un array o che permetta di navigare il resultset senza
     * esporlo
     *
     * @return
     * @throws SQLException
     */
    public ResultSet readGroups() throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from groups ";
        stmt = DBConnection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /**
     * Cerca nel DB i gruppi corrispondenti al note "group" TODO: sostituire con
     * un metodo che ritorni un array o che permetta di navigare il resultset
     * senza esporlo
     *
     * @param group Nome del gruppo da cercare
     * @return Il resutset corrispondente per navigare tutte le righe trovate
     * @throws SQLException
     */
    public ResultSet readGroup(String group) throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from groups where title = ?";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setString(1, group);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    // TODO: In capsulare meglio la funzione. Il rischio è che il resultset non venga mai chiuso
    // TODO: Vale anch eper gli altri metodi che ritornano un resultset. 
    /**
     * Cerca nel DB tutti i siti TODO: sostituire con un metodo che ritorni un
     * array o che permetta di navigare il resultset senza esporlo
     *
     * @return Il resutset corrispondente per navigare tutte le righe trovate
     * @throws SQLException
     */
    public ResultSet readSites() throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from sites ";
        stmt = DBConnection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet readSitesOfGroupID(int groupID) throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from sites where IDGroup = ? ";

        stmt = DBConnection.prepareStatement(sql);
        stmt.setInt(1, groupID);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /**
     * Cerca tutti i siti e ne memorizza le url in un ArrayList
     *
     * @return L'arrayList con tutti i siti memorizzati
     */
    public ArrayList<String> readSitesUrl() {
        ArrayList<String> temp = null;
        try (ResultSet rs = readSites()) {
            temp = new ArrayList<>();
            while (rs.next()) {
                temp.add(rs.getString("url"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLliteUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return temp;
    }

    /**
     * Cerca tutti i siti corrispondenti ad un determinato URL e ritorna il
     * resultset corrispondente
     *
     * @param siteUrl Url del sito da cercare nel DB
     * @return Il result set corrispondente per navigare tra le righe
     * @throws SQLException
     */
    public ResultSet readSite(String siteUrl) throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from sites where url = ?";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setString(1, siteUrl);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /**
     * Cerca il sito corrispondente ad un determinato ID e ritorna il resultset
     * corrispondente
     *
     * @param ID L'ID (chiave) del sito da cercare nel DB
     * @return Il result set corrispondente per navigare tra le righe
     * @throws SQLException
     */
    public ResultSet readSite(int ID) throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from sites where ID = ?";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setInt(1, ID);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    /**
     *
     * @param url - Url da aggiornare
     * @param status 0 = ok, 1 = Error
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public boolean updateUpdateStatusDB(String url, int status) throws FileNotFoundException, IOException, SQLException {
        PreparedStatement stmt;
        String sql = "update sites set update_status = ?,last_update=datetime('now') where url = ?";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setInt(1, status);
        stmt.setString(2, url);
        stmt.executeUpdate();
        stmt.close();
        return true;
    }

    /**
     * Aggiorna l'immagine del sito
     *
     * @param url url da aggiornare
     * @param filename nome del file contenente l'immagine da aggiornare per
     * l'url
     * @return True se tutto è andato bene, false in caso di problemi
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public boolean updateImageDB(String url, String filename) throws FileNotFoundException, IOException, SQLException {
        File imageFile = new File(filename);

        if (!imageFile.isFile()) {
            return false;
        }

        ByteArrayOutputStream baos;
        try ( // Leggo il file e lo converto in byte array
                InputStream bis = new BufferedInputStream(new FileInputStream(imageFile))) {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[SIZE];
            int lenght = 0;
            while ((lenght = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, lenght);
            }
            baos.flush();
        }
        //baos.toByteArray();
        // Update DB
        PreparedStatement stmt;
        String sql = "update sites set thumbnail = ?,last_update=datetime('now'), update_status=? where url = ?";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setBytes(1, baos.toByteArray());
        stmt.setInt(2, Constants.OK);
        stmt.setString(3, url);
        stmt.executeUpdate();
        stmt.close();
        baos.close();
        return true;
    }

    /**
     * Cerca nel DB il sito corrispondente all'URL e ritorna un InputStream con
     * l'immagine corrispondente
     *
     * @param url Url del sito da cercare
     * @return L'inputStream binario corrispondente all'immagine recuperata dal
     * DB. Ritorna null in caso di problemi
     * @throws SQLException
     */
    public InputStream getImage(String url) throws SQLException {
        return (readSite(url).getBinaryStream(3));
    }

    /**
     * Cerca nel DB il sito corrispondente all'URL e ritorna un InputStream con
     * l'immagine corrispondente
     *
     * @param url Url del sito da cercare
     * @return L'inputStream binario corrispondente all'immagine recuperata dal
     * DB. Ritorna null in caso di problemi
     * @throws SQLException
     */
    public InputStream getImage(int ID) throws SQLException {
        return (readSite(ID).getBinaryStream(3));
    }

}
