/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package speeddialutility;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author utente
 */
public class ThumbnailGeneratorTest {

    String DBPath = "C:\\PortableApps\\SQLiteDatabaseBrowserPortable\\Data\\speeddial.db";
    SQLliteUtils instanceDB = new SQLliteUtils(null);

    public ThumbnailGeneratorTest() {
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
            instanceDB.startDBConnection(DBPath);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQLliteUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @After
    public void tearDown() {
        try {
            instanceDB.closeDBConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SQLliteUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of grabThumbnail method, of class ThumbnailGenerator.
     */
    @Test
    public void testGrabThumbnail() throws Exception {
        System.out.println("grabThumbnail");
        ThumbnailGenerator instance = new ThumbnailGenerator(null, null, null, null);
        instance.grabAllThumbnail();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}
