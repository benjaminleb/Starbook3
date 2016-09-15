package swing;

import classes.Book;
import classes.ConnectSQLS;
import classes.Event;
import classes.Helpers;
import classes.InputCheck;
import classes.Publisher;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/*
 Gab
 */
public class JFAddEvent extends javax.swing.JFrame {

    public JFAddEvent() {
        initComponents();
    }

    private DefaultListModel initbookDatabase() {

        DefaultListModel bookDatabaseModel = new DefaultListModel();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT sb_book.* FROM sb_book";

        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Book bk = new Book(rs.getString("book_isbn"), rs.getString("book_title"), rs.getString("book_subtitle"), rs.getFloat("book_price"));
                bookDatabaseModel.addElement(bk);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
        }
        bookDatabase.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        co.closeConnectionDatabase();

        return bookDatabaseModel;
    }

    private DefaultListModel bookSelectionModel() {
        DefaultListModel dm = new DefaultListModel();
        return dm;
    }

    private ListModel modBookSelectionModel(List bl, ListModel dm) {
        ListModel lm;
        DefaultListModel ndm = dm;
        for (int i = 0; i < bl.size(); i++) {
            ndm.addElement(bl.get(i));
        }
        lm = ndm;
        return lm;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        Ajouter = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        bookDatabase = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        bookSelection = new javax.swing.JList();
        AjouterLivre = new javax.swing.JButton();
        SupprimerLIvre = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(600, 550));

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 13)); // NOI18N
        jLabel1.setText("Evénement");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel2.setText("Nom:*");

        jTextField1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel3.setText("Début:");

        jTextField2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel4.setText("Fin:");

        jTextField3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setText("Taux de Réduction:*");

        jTextField4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setText("Image:");

        jTextField5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        Ajouter.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        Ajouter.setText("Ajouter");
        Ajouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Calibri", 2, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setText("* Champs obigatoires");

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel8.setText("Livre:");

        bookDatabase.setModel(initbookDatabase());
        jScrollPane1.setViewportView(bookDatabase);

        bookSelection.setModel(bookSelectionModel());
        jScrollPane2.setViewportView(bookSelection);

        AjouterLivre.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        AjouterLivre.setText("Ajouter Livre  >>>");
        AjouterLivre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AjouterLivreActionPerformed(evt);
            }
        });

        SupprimerLIvre.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        SupprimerLIvre.setText("Supprimer Livre <<<");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(308, 308, 308))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(Ajouter)
                        .addGap(299, 299, 299))))
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(109, 109, 109)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField5))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SupprimerLIvre)
                            .addComponent(AjouterLivre, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addComponent(jLabel7)
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(AjouterLivre)
                        .addGap(27, 27, 27)
                        .addComponent(SupprimerLIvre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Ajouter)
                        .addGap(31, 31, 31))))
        );

        setBounds(0, 0, 754, 549);
    }// </editor-fold>//GEN-END:initComponents

    private void AjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterActionPerformed

        Event evnt;
        //on vérifie que les champs obligatoires sont saisis et CORRECTEMENT
        if (!InputCheck.checkAlphaChar(jTextField1.getText()) || !InputCheck.checkNumbers(jTextField4.getText())) {

            JOptionPane JOp01 = new JOptionPane();
            JOp01.showMessageDialog(null, "Veuillez remplir correctement les champs obligatoires", "Erreur", JOptionPane.ERROR_MESSAGE);

        } else {//on vérifie que si les dates sont saisies, elles sont saisies au bon format
            if (!InputCheck.checkDateFormat_NotMandatory(jTextField2.getText())
                    || !InputCheck.checkDateFormat_NotMandatory(jTextField3.getText())) {
                JOptionPane JOp02 = new JOptionPane();
                JOp02.showMessageDialog(null, "Veuillez respecter le format JJ/MM/AAAA ", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {

                try {
                    evnt = new Event(0, jTextField1.getText(),
                            Helpers.convertStringToDate(jTextField2.getText()),
                            Helpers.convertStringToDate(jTextField3.getText()),
                            Float.valueOf(jTextField4.getText()),
                            jTextField5.getText());

                    evnt.insertEvent();

                } catch (ParseException ex) {
                    Logger.getLogger(JFAddEvent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_AjouterActionPerformed

    private void AjouterLivreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AjouterLivreActionPerformed

        //ajoute la selection de jlist1 dans jlist2
        List<Book> bkL = bookDatabase.getSelectedValuesList();
        bookDatabase.setModel(modBookSelectionModel(bkL, bookDatabase.getModel()));

        //bookSelection.add(bookDatabaseModel.getSelectedItem());
        //supprime la selection de jlist1
    }//GEN-LAST:event_AjouterLivreActionPerformed

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
            java.util.logging.Logger.getLogger(JFAddEvent.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFAddEvent.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFAddEvent.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFAddEvent.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFAddEvent().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Ajouter;
    private javax.swing.JButton AjouterLivre;
    private javax.swing.JButton SupprimerLIvre;
    private javax.swing.JList bookDatabase;
    private javax.swing.JList bookSelection;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
