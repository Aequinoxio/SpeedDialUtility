/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speeddialutility;

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

    private static final int size = 1024;
    Connection DBConnection = null;

    /**
     *
     * @param DBPath the value of DBPath
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public SQLliteUtils(String DBPath) throws ClassNotFoundException, SQLException {
// TODO:impostare la tart db connection alla creazione della classe ed eliminare il metodo startdbconnection
        startDBConnection(DBPath);
    }

    private void startDBConnection(String DBPath) throws ClassNotFoundException, SQLException {
        if (DBConnection == null) {
            Class.forName("org.sqlite.JDBC");
            DBConnection = DriverManager.getConnection("jdbc:sqlite:" + DBPath);
        }
    }

    public void closeDBConnection() throws SQLException {
        DBConnection.close();
    }

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

    public void insertSite(String url, String label, Object thumbnail, String groupTitle) throws SQLException {
        ResultSet groupRS = readGroup(groupTitle);
        if (groupRS.next()) {
            insertSite(url, label, thumbnail, groupRS.getInt("ID"));
        } else {
            throw new SQLException("Unexistent group");
        }
    }

    public ResultSet readGroup(String group) throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from groups where title = ?";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setString(1, group);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public ResultSet readSites() throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from sites ";
        stmt = DBConnection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

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

    public ResultSet readSite(String siteUrl) throws SQLException {
        PreparedStatement stmt;
        String sql = "select * from sites where url = ?";
        stmt = DBConnection.prepareStatement(sql);
        stmt.setString(1, siteUrl);
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
        return true ;
    }

    public boolean updateImageDB(String url, String filename) throws FileNotFoundException, IOException, SQLException {
        File imageFile = new File(filename);

        if (!imageFile.isFile()) {
            return false;
        }

        // Leggo il file e lo converto in byte array
        InputStream bis = new BufferedInputStream(new FileInputStream(imageFile));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[size];
        int lenght = 0;
        while ((lenght = bis.read(buffer)) != -1) {
            baos.write(buffer, 0, lenght);
        }
        baos.flush();
        bis.close();
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

    public InputStream getImage(String url) throws SQLException {
        return (readSite(url).getBinaryStream(3));
    }
}
