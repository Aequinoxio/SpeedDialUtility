/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aequinoxio.speeddialutility;

import Utilities.CustomPreferences;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author utente
 */
public class ManageConfiguration extends javax.swing.JFrame {

    /**
     * Creates new form ShowConfiguration
     */
    public ManageConfiguration() {
        initComponents();
        lblDBPath.setText(CustomPreferences.getInstance().getDBPath());
        lblPhantomJSPath.setText(CustomPreferences.getInstance().getPhantomJSPath());
        lblScriptPath.setText(CustomPreferences.getInstance().getPhantomJSScriptPath());
        lblViewport.setText(CustomPreferences.getInstance().getViewPort());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblDBPath = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblPhantomJSPath = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblScriptPath = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblViewport = new javax.swing.JLabel();
        btnChangePJSPath = new javax.swing.JButton();
        btnChangeDBPath = new javax.swing.JButton();
        btnChangePJSScriptPath = new javax.swing.JButton();
        btnChangeViewPort = new javax.swing.JButton();
        btnInitDB = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Database path");

        lblDBPath.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDBPath.setText("    ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("PhantomJS path");

        lblPhantomJSPath.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPhantomJSPath.setText("    ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("PhantomJS Script");

        lblScriptPath.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblScriptPath.setText("    ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("ViewPort");

        lblViewport.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblViewport.setText("    ");

        btnChangePJSPath.setText("Cambia");
        btnChangePJSPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePJSPathActionPerformed(evt);
            }
        });

        btnChangeDBPath.setText("Cambia");
        btnChangeDBPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeDBPathActionPerformed(evt);
            }
        });

        btnChangePJSScriptPath.setText("Cambia");
        btnChangePJSScriptPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePJSScriptPathActionPerformed(evt);
            }
        });

        btnChangeViewPort.setText("Cambia");
        btnChangeViewPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeViewPortActionPerformed(evt);
            }
        });

        btnInitDB.setText("Inizializza e rigenera Database");
        btnInitDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInitDBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblViewport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblScriptPath, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblPhantomJSPath, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                            .addComponent(lblDBPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnChangePJSPath)
                    .addComponent(btnChangeDBPath)
                    .addComponent(btnChangePJSScriptPath)
                    .addComponent(btnChangeViewPort))
                .addGap(24, 24, 24))
            .addGroup(layout.createSequentialGroup()
                .addGap(339, 339, 339)
                .addComponent(btnInitDB)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDBPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChangeDBPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPhantomJSPath)
                    .addComponent(btnChangePJSPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblScriptPath)
                        .addComponent(btnChangePJSScriptPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnChangeViewPort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblViewport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInitDB)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChangeDBPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeDBPathActionPerformed
        final JFileChooser fc = new JFileChooser();
        if (fc.showDialog(this,"Scegli il file con il DB") == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //todo metterla nelle prefs e ricaricare il db
            CustomPreferences.getInstance().setDBPath(file.getAbsolutePath());
        }
    }//GEN-LAST:event_btnChangeDBPathActionPerformed

    private void btnChangePJSPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePJSPathActionPerformed
        JOptionPane.showMessageDialog(this, "Non implementato");
    }//GEN-LAST:event_btnChangePJSPathActionPerformed

    private void btnInitDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInitDBActionPerformed
        if (JOptionPane.showConfirmDialog(this, "Ripristino tutto?\nQuesto azzererà il DB corrente ed avvierà il parsing del file prefs.js\ned il downlo adelle immagini. Il processo può essere molto lungo.")
                == JOptionPane.OK_OPTION) {
            // TODO: ricaricare il DB
            final JFileChooser fc = new JFileChooser();

            if (fc.showDialog(this, "Apri il file prefs.js") == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //todo metterla nelle prefs e ricaricare il db
                file.getAbsolutePath();
                SpeedDialUtility sdu = new SpeedDialUtility();
                try {
                    sdu.startImport(file.getAbsolutePath());
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(ManageConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(this, "Annullato, nessuna modifica apportata al DB");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Annullato, nessuna modifica apportata al DB");
        }
    }//GEN-LAST:event_btnInitDBActionPerformed

    private void btnChangePJSScriptPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePJSScriptPathActionPerformed
        JOptionPane.showMessageDialog(this, "Non implementato");
    }//GEN-LAST:event_btnChangePJSScriptPathActionPerformed

    private void btnChangeViewPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeViewPortActionPerformed
        JOptionPane.showMessageDialog(this, "Non implementato");
    }//GEN-LAST:event_btnChangeViewPortActionPerformed

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
            java.util.logging.Logger.getLogger(ManageConfiguration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManageConfiguration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManageConfiguration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManageConfiguration.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManageConfiguration().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangeDBPath;
    private javax.swing.JButton btnChangePJSPath;
    private javax.swing.JButton btnChangePJSScriptPath;
    private javax.swing.JButton btnChangeViewPort;
    private javax.swing.JButton btnInitDB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblDBPath;
    private javax.swing.JLabel lblPhantomJSPath;
    private javax.swing.JLabel lblScriptPath;
    private javax.swing.JLabel lblViewport;
    // End of variables declaration//GEN-END:variables
}
