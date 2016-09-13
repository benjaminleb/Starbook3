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
import java.awt.Color;
import javax.swing.table.*;

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

    
    //orderList
    private void useOrderList() {

        Order o = (Order) orderList.getSelectedValue();
        jLOref.setText(Integer.toString(o.getId()));
        jLOprice.setText(Float.toString(o.calculatePrice()) + " €");
        jLOdate.setText(Helpers.convertDateToString(o.getDate()));
//        jLOstatus.setText(o.getStatusList().lastElement().toString());

    }

    private DefaultListModel initOrderList() {
        DefaultListModel orders = new DefaultListModel();
        Customer selectC = (Customer) jCBCustomerSearch.getSelectedItem();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        orderList.removeAll();
        String query = "SELECT * FROM sb_order WHERE customer_id LIKE '" + selectC.getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                orders.addElement(new Order(rs.getInt("order_id"),
                        selectC,
                        rs.getDate("order_Date"),
                        rs.getString("order_ipAddress")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());

        }
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        co.closeConnectionDatabase();
        return orders;
    }
    
    
    
    //orderLines table
    private DefaultTableModel initOrderLinesModel() {
        Vector v = new Vector();
        v.add("Titre");
        v.add("ISBN");
        v.add("Prix TTC");
        v.add("Quantité");
        v.add("Total TTC");
        
        
        
        return new javax.swing.table.DefaultTableModel(initVectorOrderLines(), v) {
            boolean[] canEdit = new boolean[]{
                        false, false, false, false, false
                    };
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
        };

    }

    private Vector initVectorOrderLines() {
        Vector v = new Vector();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        Order select = (Order) orderList.getSelectedValue();

        String query = "SELECT * FROM sb_orderLine WHERE order_id LIKE '" + select.getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                //(int id, String isbnBook, int itemQty, float unitPrice, float taxRate, float discountRate)
                OrderLine ol = new OrderLine(rs.getInt("orderLine_id"),
                        rs.getString("book_isbn"), 
                        rs.getInt("order_itemQty"), 
                        rs.getFloat("order_unitPrice"), 
                        rs.getFloat("order_taxRate"), 
                        rs.getFloat("order_discountRate"));
                Vector vv = new Vector();
                vv.add(ol.getBookName());
                vv.add(ol.getIsbnBook());
                vv.add(ol.taxUnitPrice());
                vv.add(ol.getItemQty());
                vv.add(ol.calculateLinePrice());
                v.add(vv);
            }

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return v;
        }

        co.closeConnectionDatabase();

        return v;
    }

    
    //recherche customers
    private DefaultComboBoxModel initModelCustomerResults() {
        return new DefaultComboBoxModel(initVectorCustomerResults());
    }

    private Vector initVectorCustomerResults() {
        Vector customerResults = new Vector();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query;
        jCBCustomerSearch.removeAllItems();
        if (jRBMail.isSelected()) {
            //customer_email -> customer_mail !
            query = "SELECT * FROM sb_Customer WHERE customer_mail LIKE '" + jTFMailSearch.getText() + "%'";
        } else {
            query = "SELECT * FROM sb_Customer "
                    + "WHERE customer_firstname LIKE '%" + jTFNameSearch1.getText() + "%'"
                    + "AND customer_surname LIKE '%" + jTFNameSearch2.getText() + "%'";
        }

        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                customerResults.add(new Customer(rs.getInt("customer_id"),
                        rs.getString("customer_surname"),
                        rs.getString("customer_firstname"),
                        rs.getString("customer_pwd"),
                        rs.getString("customer_mail"),
                        rs.getString("customer_cell"),
                        rs.getString("customer_landline"),
                        rs.getDate("customer_dob")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return customerResults;
        }

        co.closeConnectionDatabase();
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
        jButton1 = new javax.swing.JButton();
        jCBCustomerSearch = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jTFNameSearch2 = new javax.swing.JTextField();
        jTFNameSearch1 = new javax.swing.JTextField();
        jTFMailSearch = new javax.swing.JTextField();
        jRBMail = new javax.swing.JRadioButton();
        jRBName = new javax.swing.JRadioButton();
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
        jLID = new javax.swing.JLabel();
        jLNameV = new javax.swing.JLabel();
        jLMailV = new javax.swing.JLabel();
        jLBirthV = new javax.swing.JLabel();
        jLCellV = new javax.swing.JLabel();
        jLLandV = new javax.swing.JLabel();
        jLIDV = new javax.swing.JLabel();
        jLStatusV = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderList = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLOref = new javax.swing.JLabel();
        jLOprice = new javax.swing.JLabel();
        jLOdate = new javax.swing.JLabel();
        jLOstatus = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jDialogSearch.setSize(new java.awt.Dimension(470, 335));
        jDialogSearch.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                jDialogSearchWindowOpened(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLabel2.setText("Rechercher un client par");

        jButton1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCBCustomerSearch.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jCBCustomerSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sélectionner un client" }));
        jCBCustomerSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBCustomerSearchActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton2.setText("Sélectionner");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTFNameSearch2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTFNameSearch2.setText("Nom");
        jTFNameSearch2.setToolTipText("Nom");
        jTFNameSearch2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFNameSearch2MouseClicked(evt);
            }
        });
        jTFNameSearch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNameSearch2ActionPerformed(evt);
            }
        });

        jTFNameSearch1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTFNameSearch1.setText("Prénom");
        jTFNameSearch1.setToolTipText("Prénom");
        jTFNameSearch1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFNameSearch1MouseClicked(evt);
            }
        });
        jTFNameSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNameSearch1ActionPerformed(evt);
            }
        });

        jTFMailSearch.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTFMailSearch.setText("Mail");
        jTFMailSearch.setToolTipText("Mail");
        jTFMailSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTFMailSearch.setEnabled(false);
        jTFMailSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFMailSearchMouseClicked(evt);
            }
        });
        jTFMailSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFMailSearchActionPerformed(evt);
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

        buttonGroupSearch.add(jRBName);
        jRBName.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRBName.setSelected(true);
        jRBName.setText("Nom");
        jRBName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogSearchLayout = new javax.swing.GroupLayout(jDialogSearch.getContentPane());
        jDialogSearch.getContentPane().setLayout(jDialogSearchLayout);
        jDialogSearchLayout.setHorizontalGroup(
            jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogSearchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(185, 185, 185))
            .addGroup(jDialogSearchLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialogSearchLayout.createSequentialGroup()
                        .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jRBName, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRBMail, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialogSearchLayout.createSequentialGroup()
                                .addComponent(jTFNameSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTFNameSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTFMailSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jDialogSearchLayout.setVerticalGroup(
            jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRBName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFNameSearch1)
                    .addComponent(jTFNameSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jDialogSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRBMail, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFMailSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCBCustomerSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
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
        jLName.setText("Nom :");

        jLMail.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLMail.setText("Mail :");

        jLStatus.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLStatus.setText("Statut :");

        jLLand.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLLand.setText("Tel. Fixe :");

        jLBirth.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLBirth.setText("Date de naissance :");

        jLCell.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLCell.setText("Tel. Mobile :");

        jLID.setText("Ref :");

        jLNameV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLMailV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLBirthV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLCellV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLLandV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLIDV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLStatusV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonSearch)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLMail)
                            .addGap(18, 18, 18)
                            .addComponent(jLMailV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLName)
                            .addGap(18, 18, 18)
                            .addComponent(jLNameV, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLBirth)
                            .addGap(18, 18, 18)
                            .addComponent(jLBirthV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLCell)
                                .addComponent(jLLand))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLCellV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLLandV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLStatus)
                        .addGap(18, 18, 18)
                        .addComponent(jLStatusV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLID)
                        .addGap(18, 18, 18)
                        .addComponent(jLIDV, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButtonSearch)
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLMail)
                            .addComponent(jLMailV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLBirth)
                            .addComponent(jLBirthV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLCell)
                            .addComponent(jLCellV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLLand)
                            .addComponent(jLLandV))
                        .addGap(58, 58, 58))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLNameV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLID)
                            .addComponent(jLIDV))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLStatus)
                            .addComponent(jLStatusV))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLabel1.setText("Historique");

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Titre", "ISBN", "Prix unitaire TTC", "Quantité", "Prix total TTC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(orderTable);

        orderList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        orderList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                orderListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(orderList);

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel3.setText("Ref commande :");

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel4.setText("Date :");

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setText("Statut :");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setText("Prix TTC :");

        jLOref.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLOprice.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLOdate.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLOstatus.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOprice))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOref)))
                        .addGap(138, 138, 138)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOstatus))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOdate)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLOref)
                    .addComponent(jLOdate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLOprice)
                    .addComponent(jLOstatus))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        setBounds(0, 0, 747, 620);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        jDialogSearch.setModal(true);
        jDialogSearch.setVisible(true);
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jDialogSearchWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialogSearchWindowOpened
        // TODO add your handling code here
    }//GEN-LAST:event_jDialogSearchWindowOpened

    private void jRBNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBNameActionPerformed
        // TODO add your handling code here:
        jTFMailSearch.setEnabled(false);
        jTFMailSearch.setText("Mail");
        jTFNameSearch1.setEnabled(true);
        jTFNameSearch2.setEnabled(true);
    }//GEN-LAST:event_jRBNameActionPerformed

    private void jRBMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBMailActionPerformed
        // TODO add your handling code here:
        jTFMailSearch.setEnabled(true);
        jTFNameSearch1.setEnabled(false);
        jTFNameSearch1.setText("Prénom");
        jTFNameSearch2.setEnabled(false);
        jTFNameSearch2.setText("Nom");
    }//GEN-LAST:event_jRBMailActionPerformed

    private void jTFMailSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFMailSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFMailSearchActionPerformed

    private void jTFMailSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFMailSearchMouseClicked
        // TODO add your handling code here:
        jTFMailSearch.setText("");
        jTFMailSearch.setForeground(Color.black);
    }//GEN-LAST:event_jTFMailSearchMouseClicked

    private void jTFNameSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNameSearch1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNameSearch1ActionPerformed

    private void jTFNameSearch1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFNameSearch1MouseClicked
        // TODO add your handling code here:
        jTFNameSearch1.setText("");
        jTFNameSearch1.setForeground(Color.black);
    }//GEN-LAST:event_jTFNameSearch1MouseClicked

    private void jTFNameSearch2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNameSearch2ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTFNameSearch2ActionPerformed

    private void jTFNameSearch2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFNameSearch2MouseClicked
        // TODO add your handling code here:
        jTFNameSearch2.setText("");
        jTFNameSearch2.setForeground(Color.black);
    }//GEN-LAST:event_jTFNameSearch2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Customer selectC = (Customer) jCBCustomerSearch.getSelectedItem();
        jDialogSearch.dispose();

//infos client
        jLNameV.setText(selectC.getFirstname() + " " + selectC.getSurname().toUpperCase());
        jLMailV.setText(selectC.getMail());
        jLBirthV.setText(Helpers.convertDateToString(selectC.getDob()));
        jLCellV.setText(selectC.getCell());
        jLLandV.setText(selectC.getLandline());
        jLIDV.setText(Integer.toString(selectC.getId()));
        jLStatusV.setText(selectC.getStatusList().lastElement().toString());
//infos commande
        orderList.setModel(initOrderList());
        if (orderList.getWidth() >= 0) {
            orderList.setSelectedIndex(0);
            useOrderList();
            orderTable.setModel(initOrderLinesModel());
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCBCustomerSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBCustomerSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBCustomerSearchActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jCBCustomerSearch.setModel(initModelCustomerResults());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void orderListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_orderListValueChanged
        // TODO add your handling code here:
        useOrderList();
        orderTable.setModel(initOrderLinesModel());
         

    }//GEN-LAST:event_orderListValueChanged

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
    private javax.swing.JComboBox jCBCustomerSearch;
    private javax.swing.JDialog jDialogSearch;
    private javax.swing.JDialog jDialogStatus;
    private javax.swing.JLabel jLBirth;
    private javax.swing.JLabel jLBirthV;
    private javax.swing.JLabel jLCell;
    private javax.swing.JLabel jLCellV;
    private javax.swing.JLabel jLID;
    private javax.swing.JLabel jLIDV;
    private javax.swing.JLabel jLLand;
    private javax.swing.JLabel jLLandV;
    private javax.swing.JLabel jLMail;
    private javax.swing.JLabel jLMailV;
    private javax.swing.JLabel jLName;
    private javax.swing.JLabel jLNameV;
    private javax.swing.JLabel jLOdate;
    private javax.swing.JLabel jLOprice;
    private javax.swing.JLabel jLOref;
    private javax.swing.JLabel jLOstatus;
    private javax.swing.JLabel jLStatus;
    private javax.swing.JLabel jLStatusV;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRBMail;
    private javax.swing.JRadioButton jRBName;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTFMailSearch;
    private javax.swing.JTextField jTFNameSearch1;
    private javax.swing.JTextField jTFNameSearch2;
    private javax.swing.JList orderList;
    private javax.swing.JTable orderTable;
    // End of variables declaration//GEN-END:variables
}
