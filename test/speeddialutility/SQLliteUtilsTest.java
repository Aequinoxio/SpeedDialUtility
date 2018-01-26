/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speeddialutility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author utente
 */
public class SQLliteUtilsTest {

    String DBPath = "C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db";
    String groupTitle = "testGroupTitle";
    String siteUrl = "https://www.test.com";
    String siteLabel = "siteLabel";
    int groupRows = 10;
    int groupColumns = 20;
    SQLliteUtils instance = new SQLliteUtils(null);

    public SQLliteUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            instance.startDBConnection(DBPath);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQLliteUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @After
    public void tearDown() {
        try {
            instance.closeDBConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SQLliteUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of insertGroup method, of class SQLliteUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testInsertGroup() throws Exception {
        System.out.println("insertGroup");

        SQLliteUtils instance = new SQLliteUtils(null);

        try {
            instance.startDBConnection(DBPath);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQLliteUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        instance.insertGroup(groupTitle, groupRows, groupColumns);
        instance.closeDBConnection();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    @Test
    public void testInsertSite() throws Exception {
        System.out.println("insertSite");
        String url = "test";
        String label = "testLabel";

        SQLliteUtils instance = new SQLliteUtils(null);

        try {
            instance.startDBConnection(DBPath);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQLliteUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        ResultSet rs = instance.readGroup(groupTitle);
        int IDGroup = rs.getInt("ID");
        instance.insertSite(url, label, null, IDGroup);
        instance.closeDBConnection();

    }

    @Test
    @Ignore
    public void testStartDBConnection() throws SQLException {
        System.out.println("read");
        SQLliteUtils instance = new SQLliteUtils(null);
        instance.closeDBConnection();
        try {
            instance.startDBConnection(DBPath);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQLliteUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of closeDBConnection method, of class SQLliteUtils.
     */
    @Test
    @Ignore
    public void testCloseDBConnection() throws Exception {
        System.out.println("closeDBConnection");
        SQLliteUtils instance = new SQLliteUtils(null);
        instance.closeDBConnection();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readGroup method, of class SQLliteUtils.
     */
    @Test
    public void testReadGroup() throws Exception {
        System.out.println("readGroup");
       
        //SQLliteUtils instance = new SQLliteUtils();        
        String expResult="";
        ResultSet result = instance.readGroup(groupTitle);
        if (result.next()){
            expResult=result.getString("title");
        System.out.println(String.format("%d , %s ,%d, %d", 
                result.getInt("ID"),result.getString("title"),result.getInt("rows"),result.getInt("columns")
        ));
        }
        assertEquals(groupTitle, expResult);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }


    /**
     * Test of insertSite method, of class SQLliteUtils.
     */
    @Test
    public void testInsertSite_4args_2() throws Exception {
        System.out.println("insertSite groupTitle as param");
                
        instance.insertSite(siteUrl, siteLabel, null, groupTitle);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of insertSite method, of class SQLliteUtils.
     */
    @Test
    public void testInsertSite_4args_1() throws Exception {
        System.out.println("insertSite");
        String url = "";
        String label = "";
        Object thumbnail = null;
        int IDGroup = 0;
        SQLliteUtils instance = new SQLliteUtils(null);
        instance.insertSite(url, label, thumbnail, IDGroup);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readSites method, of class SQLliteUtils.
     */
    @Test
    public void testReadSites() throws Exception {
        System.out.println("readSites");
        ResultSet expResult = null;
        ResultSet result = instance.readSites();
        while (result.next()){
            System.out.println(result.getString(1));
        }
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of readSite method, of class SQLliteUtils.
     */
    @Test
    public void testReadSite() throws Exception {
        System.out.println("readSite");
        String siteUrl = "";
        SQLliteUtils instance = new SQLliteUtils(null);
        ResultSet expResult = null;
        ResultSet result = instance.readSite(siteUrl);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
