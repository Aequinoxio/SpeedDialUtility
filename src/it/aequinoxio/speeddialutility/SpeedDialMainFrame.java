/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aequinoxio.speeddialutility;

import DUMMY_TODEL.MainFrame;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import Utilities.Constants;
import Utilities.CustomPreferences;
import it.aequinoxio.speeddialutility.SQLliteUtils;
import it.aequinoxio.speeddialutility.ManageConfiguration;
import java.awt.Dialog;

/**
 *
 * @author utente
 */
public class SpeedDialMainFrame extends javax.swing.JFrame {

    SQLliteUtils sqlu;
    ArrayList<String> siteUrls;
    CustomPreferences prefs = CustomPreferences.getInstance();

//GridLayout groupImages = new GridLayout(0,3); // tre colonne e infinite righe
    /**
     * Creates new form DummyFrameTest
     *
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public SpeedDialMainFrame() throws SQLException, ClassNotFoundException {
        initComponents();

        initTree();
    }

    private void initTree()throws SQLException, ClassNotFoundException {
        //CustomPreferences.getInstance().resetPreferencesToDefault(); // Inizializzo le preferences. TODO: Trovare un modo migliore per lanciarlo indipendentemente dalla classe frame che mostrerà la finestra

        this.sqlu = new SQLliteUtils(prefs.getDBPath());
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("SpeedDial urls");
        
        createNodes(top);
        
        treeSites.setModel(new DefaultTreeModel(top, false));
        treeSites.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = treeSites.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = treeSites.getPathForLocation(e.getX(), e.getY());

                if (selRow != -1) {
                    if (e.getClickCount() == 1) {
                        // Non faccio nulla
                    } else if (e.getClickCount() == 2) { // Al doppio click apro l'url nel browser
                        Object o = selPath.getLastPathComponent();
                        Object uo;
                        if (o instanceof DefaultMutableTreeNode) {
                            uo = ((DefaultMutableTreeNode) o).getUserObject();
                            if (uo instanceof SiteInfo) {
                                int retVal = JOptionPane.showConfirmDialog(null, String.format("Apro il sito%n%s", ((SiteInfo) uo).url));
                                if (retVal == JOptionPane.OK_OPTION) {
                                    try {
                                        Desktop.getDesktop().browse(new URI(((SiteInfo) uo).url));
                                    } catch (URISyntaxException | IOException ex) {
                                        Logger.getLogger(SpeedDialMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                        
                    } else{ // click count != 2
                        
                    }
                }
            }
        };
        treeSites.addMouseListener(ml);

        treeSites.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent tse
            ) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeSites.getLastSelectedPathComponent();
                lblImage.setVisible(true);
                lblUrl.setText(" ");
                if (node == null) //Nothing is selected.     
                {
                    return;
                }

                Object nodeInfo = node.getUserObject();
                if (node.isLeaf()) {
                    SiteInfo siteInfo = (SiteInfo) nodeInfo;
                    try {
                        InputStream is = sqlu.getImage(siteInfo.ID);
                        Image image = ImageIO.read(is);
                        if (image != null) {
                            lblImage.setIcon(new ImageIcon(image));
                        } else {
                            lblImage.setIcon(null);
                        }
                        lblImage.revalidate();
                        try (ResultSet rsTemp = sqlu.readSite(siteInfo.ID)) {
                            lblUrl.setText(String.format("(%s)     %s", rsTemp.getString("last_update"), siteInfo.toString()));
                        }
                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    lblImage.setVisible(false);
                }
            }
        }
        );
    }

    private void createNodes(DefaultMutableTreeNode top) throws SQLException {
        DefaultMutableTreeNode group = null;
        DefaultMutableTreeNode site = null;
        ResultSet rsSites = null;
        ResultSet rsGroups = null;
        rsGroups = sqlu.readGroups();
        while (rsGroups.next()) {
            group = new DefaultMutableTreeNode(rsGroups.getString(2));
            top.add(group);
            rsSites = sqlu.readSitesOfGroupID(rsGroups.getInt(1));
            int counter = 0;
            while (rsSites.next()) {
                site = new DefaultMutableTreeNode(
                        new SiteInfo(rsSites.getString(2), rsSites.getString(1), rsSites.getInt("ID")));
                group.add(site);
                counter++;
            }
            group.setUserObject(String.format("%s (%d)", group.toString(), counter));
            rsSites.close();
        }
        rsGroups.close();
//        //sqlu.readGroup(group)
//        siteUrls = sqlu.readSitesUrl();
//        comboUrl.setModel(new DefaultComboBoxModel(siteUrls.toArray()));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        treeSites = new javax.swing.JTree();
        jScrollPane3 = new javax.swing.JScrollPane();
        lblImage = new javax.swing.JLabel();
        lblUrl = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuRebuildDB = new javax.swing.JMenuItem();
        mnuConfiguration = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        treeSites.setMinimumSize(new java.awt.Dimension(190, 330));
        jScrollPane2.setViewportView(treeSites);

        jSplitPane1.setLeftComponent(jScrollPane2);

        lblImage.setBackground(new java.awt.Color(51, 51, 255));
        lblImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImage.setText("    ");
        lblImage.setMaximumSize(new java.awt.Dimension(0, 0));
        lblImage.setMinimumSize(new java.awt.Dimension(80, 60));
        jScrollPane3.setViewportView(lblImage);

        jSplitPane1.setRightComponent(jScrollPane3);

        lblUrl.setText("     ");

        jMenu1.setText("File");

        mnuRebuildDB.setText("Reload SpeedDial prefs");
        mnuRebuildDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRebuildDBActionPerformed(evt);
            }
        });
        jMenu1.add(mnuRebuildDB);

        mnuConfiguration.setText("View Configuration");
        mnuConfiguration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConfigurationActionPerformed(evt);
            }
        });
        jMenu1.add(mnuConfiguration);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUrl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1318, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUrl)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuConfigurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConfigurationActionPerformed
        ManageConfiguration showConfiguration = new ManageConfiguration();
        showConfiguration.setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        showConfiguration.setVisible(true);
    }//GEN-LAST:event_mnuConfigurationActionPerformed

    private void mnuRebuildDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRebuildDBActionPerformed
        // TODO add your handling code here:
        int retVal = JOptionPane.showConfirmDialog(null, "Reload SpeedDial preferences defaults?\nThis overwrite the existing DataBase's values");
        if (retVal != JOptionPane.OK_OPTION) {
            return;
        }
        JOptionPane.showMessageDialog(null, "OK ORA RIFACCIO TUTTO IL DB _ TODEL");
        //CustomPreferences.getInstance().resetPreferencesToDefault();
        // TODO: remove all DB entries and reaload the pref.js (asking the file's location
    }//GEN-LAST:event_mnuRebuildDBActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SpeedDialMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SpeedDialMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SpeedDialMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SpeedDialMainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new SpeedDialMainFrame().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(SpeedDialMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SpeedDialMainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblUrl;
    private javax.swing.JMenuItem mnuConfiguration;
    private javax.swing.JMenuItem mnuRebuildDB;
    private javax.swing.JTree treeSites;
    // End of variables declaration//GEN-END:variables

    private class SiteInfo {

        String title;
        String url;
        int ID;

        SiteInfo(String titleString, String urlString, int DBID) {
            title = titleString;
            url = urlString;
            ID = DBID;
        }

        @Override
        public String toString() {
            return String.format("%s - %s", title, url);
        }
    }
}
