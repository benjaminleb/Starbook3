/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swing;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import classes.*;

/*
ben
 */
public class JFCustomer extends javax.swing.JFrame {

    /**
     * Creates new form JFCustomer
     */
    public JFCustomer() {
        initComponents();
    }
    private DefaultComboBoxModel initModelCustomerResults() {
        return new DefaultComboBoxModel(initVectorCustomerResults());
    }

    private Vector initVectorCustomerResults() {
        Vector customerResults = new Vector();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query;

        if(jRBMail.isSelected()){
            query = "SELECT * FROM sb_Customer WHERE customer_mail LIKE '"+jTFMailSearch.getText()+"%'";
        }
        else{
            query = "SELECT * FROM sb_Customer "
                    + "WHERE customer_surname LIKE '%"+jTFNameSearch1.getText()+"%'"
                    + "AND customer_firstname LIKE '%"+jTFNameSearch2.getText()+"%'";
        }
        
        
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                customerResults.add(new Customer(rs.getString("Pays"),
                        rs.getString("A2"),
                        rs.getString("A3"),
                        rs.getInt("Number")));
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return customerResults;
        }

        try {
            connexion.close();
        } catch (SQLException ex) {
            System.err.println("Oops:Close:" + ex.getErrorCode() + ":" + ex.getMessage());
            return customerResults;
        }

        System.out.println("Done!");
        return customerResults;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogSearch = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        jRBName = new javax.swing.JRadioButton();
        jRBMail = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jCBCustomerSearch = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jTFMailSearch = new javax.swing.JTextField();
        jTFNameSearch1 = new javax.swing.JTextField();
        jTFNameSearch2 = new javax.swing.JTextField();
        jDialogStatus = new javax.swing.JDialog();
        buttonGroupSearch = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jButtonSearch = new javax.swing.JButton();
        jLName = new javax.swing.JLabel();
        jLMail = new javax.swing.JLabel();
        jLStatus = new javax.swing.JLabel();
        jLLand = new javax.swing.JLabel();
        jLBirth = new javax.swing.JLabel();
        jLCell = new javax.swing.JLabel();
        jButtonStatus = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jDialogSearch.setPreferredSize(new java.awt.Dimension(470, 335));
        jDialogSearch.setSize(new java.awt.Dimension(470, 335));
        jDialogSearch.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                jDialogSearchWindowOpened(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel2.setText("Rechercher un client par");

        buttonGroupSearch.add(jRBName);
        jRBName.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRBName.setSelected(true);
        jRBName.setText("Nom");
        jRBName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBNameActionPerformed(evt);
            }
        });

        buttonGroupSearch.add(jRBMail);
        jRBMail.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRBMail.setText("Mail");
        jRBMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBMailActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel3.setText("Sélectionner un client");

        jButton1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton1.setText("OK");

        jCBCustomerSearch.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jCBCustomerSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCBCustomerSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBCustomerSearchActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton2.setText("Sélectionner");

        jTFMailSearch.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTFMailSearch.setText("Mail");
        jTFMailSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTFMailSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFMailSearchActionPerformed(evt);
            }
        });

        jTFNameSearch1.setText("Prénom");
        jTFNameSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNameSearch1ActionPerformed(evt);
            }
        });

        jTFNameSearch2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTFNameSearch2.setText("Nom");
        jTFNameSearch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNameSearch2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogSearchLayout = new javax.swing.GroupLayout(jDialogSearch.getContentPane());
        jDialogSearch.getContentPane().setLayout(jDialogSearchLayout);
        jDialogSearchLayout.setHorizontalGroup(
            jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchLayout.createSequentialGroup()
                .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(jRBName, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jRBMail, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFNameSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFMailSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addComponent(jTFNameSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jCBCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        jDialogSearchLayout.setVerticalGroup(
            jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jRBName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRBMail, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTFNameSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jTFMailSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jTFNameSearch1))
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCBCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialogStatusLayout = new javax.swing.GroupLayout(jDialogStatus.getContentPane());
        jDialogStatus.getContentPane().setLayout(jDialogStatusLayout);
        jDialogStatusLayout.setHorizontalGroup(
            jDialogStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialogStatusLayout.setVerticalGroup(
            jDialogStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonSearch.setText("Rechercher");
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jLName.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLName.setText("Prénom NOM");

        jLMail.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLMail.setText("mail@mail.net");

        jLStatus.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLStatus.setText("Statut");

        jLLand.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLLand.setText("Tel02");

        jLBirth.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLBirth.setText("01/02/1903");

        jLCell.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLCell.setText("Tel06");

        jButtonStatus.setText("...");
        jButtonStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonSearch)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLMail)
                        .addComponent(jLName)
                        .addComponent(jLCell)
                        .addComponent(jLLand)
                        .addComponent(jLBirth)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(jLStatus)
                .addGap(103, 103, 103)
                .addComponent(jButtonStatus)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButtonSearch)
                .addGap(22, 22, 22)
                .addComponent(jLName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLMail)
                    .addComponent(jLStatus)
                    .addComponent(jButtonStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLBirth)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLCell)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLLand)
                .addGap(58, 58, 58))
        );

        jLabel1.setText("Historique");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(273, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 440, 582);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonStatusActionPerformed

    private void jRBMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBMailActionPerformed
        // TODO add your handling code here:
        jTFMailSearch.setVisible(true);
        jTFNameSearch1.setVisible(false);
        jTFNameSearch2.setVisible(false);
    }//GEN-LAST:event_jRBMailActionPerformed

    private void jTFNameSearch2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNameSearch2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNameSearch2ActionPerformed

    private void jTFNameSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNameSearch1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTFNameSearch1ActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        jDialogSearch.setVisible(true);
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jRBNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBNameActionPerformed
        // TODO add your handling code here:
        jTFMailSearch.setVisible(false);
        jTFNameSearch1.setVisible(true);
        jTFNameSearch2.setVisible(true);
    }//GEN-LAST:event_jRBNameActionPerformed

    private void jTFMailSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFMailSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFMailSearchActionPerformed

    private void jDialogSearchWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogSearchWindowOpened
        // TODO add your handling code here:
        jTFMailSearch.setVisible(false);
        jTFNameSearch1.setVisible(true);
        jTFNameSearch2.setVisible(true);
    }//GEN-LAST:event_jDialogSearchWindowOpened

    private void jCBCustomerSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBCustomerSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBCustomerSearchActionPerformed

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
            java.util.logging.Logger.getLogger(JFCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFCustomer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFCustomer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupSearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonStatus;
    private javax.swing.JComboBox jCBCustomerSearch;
    private javax.swing.JDialog jDialogSearch;
    private javax.swing.JDialog jDialogStatus;
    private javax.swing.JLabel jLBirth;
    private javax.swing.JLabel jLCell;
    private javax.swing.JLabel jLLand;
    private javax.swing.JLabel jLMail;
    private javax.swing.JLabel jLName;
    private javax.swing.JLabel jLStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRBMail;
    private javax.swing.JRadioButton jRBName;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTFMailSearch;
    private javax.swing.JTextField jTFNameSearch1;
    private javax.swing.JTextField jTFNameSearch2;
    // End of variables declaration//GEN-END:variables
}
