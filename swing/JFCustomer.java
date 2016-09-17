package swing;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import classes.*;
import java.awt.Color;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        permaCo.connectDatabase();

    }

    // --------------- ORDER jLIST ---------------
    private void useOrderList() {

        Order o = (Order) orderList.getSelectedValue();
        jLOref.setText(Integer.toString(o.getId()));
        jLOprice.setText(Float.toString(o.calculatePrice()) + " €");
        jLOdate.setText(Helpers.convertDateToString(o.getDate()));
        jLOstatus.setText(o.getStatusList().lastElement().toString());
        jLObilling.setText(o.getAddresses(true).toString());
        jLOdelivery.setText(o.getAddresses(false).toString());

    }

    private DefaultListModel initOrderList() {
        DefaultListModel orders = new DefaultListModel();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        orderList.removeAll();
        String query = "SELECT * FROM sb_order WHERE customer_id LIKE '" + current.getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                orders.addElement(new Order(rs.getInt("order_id"),
                        current,
                        rs.getDate("order_date"),
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

    //--------------- SearchC LIST ---------------
    private DefaultListModel empowerSearchC(Connection co) {
        DefaultListModel searchC = new DefaultListModel();
        int aIndex;
        int zIndex;
        String fName;
        String sName;
        String query = new String();
        jLiSearchC.removeAll();
        if (jComboSearchC.getSelectedIndex() == 1) {
            query = "SELECT * FROM sb_customer WHERE customer_mail LIKE '" + jTFSearchC.getText() + "%'";
        } else {
            if (!jTFSearchC.getText().contains(" ")) {
                query = "SELECT * FROM sb_customer WHERE customer_firstname LIKE '%" + jTFSearchC.getText() + "%'";
            } else {
                aIndex = jTFSearchC.getText().indexOf(" ");
                zIndex = jTFSearchC.getText().lastIndexOf(" ");
                fName = jTFSearchC.getText().substring(0, aIndex);
                sName = jTFSearchC.getText().substring(zIndex + 1).trim();
                query = "SELECT * FROM sb_customer "
                        + "WHERE (customer_firstname LIKE '%" + fName + "%' "
                        + "AND customer_surname LIKE '%" + sName + "%') "
                        + "OR (customer_firstname LIKE '%" + sName + "%' "
                        + "AND customer_surname LIKE '%" + fName + "%')";
            }

        }

        try {
            Statement stmt = co.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                searchC.addElement(new Customer(rs.getInt("customer_id"),
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

        }
        jLiSearchC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return searchC;
    }



    //--------------- ORDERLINES jTABLE ---------------
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
    
    //--------------- ADDRESSES jTABLE ---------------
    
    private DefaultTableModel initAddressesModel() {
        Vector v = new Vector();
        v.add("Adresse");
        v.add("Complément");
        v.add("CP");
        v.add("Ville");
        v.add("Pays");

        return new javax.swing.table.DefaultTableModel(initVectorAddresses(), v) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

    }

    private Vector initVectorAddresses() {
        Vector v = new Vector();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();

        String query = "SELECT * FROM sb_address WHERE customer_id LIKE '" + current.getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Address a = new Address(rs.getInt("address_id"),
                        rs.getString("address_street"),
                        rs.getString("address_other"),
                        rs.getString("address_zipcode"),
                        rs.getString("address_city"),
                        rs.getString("address_country"));
                Vector vv = new Vector();
                vv.add(a.getStreet());
                vv.add(a.getOther());                
                vv.add(a.getZipcode());
                vv.add(a.getCity());
                vv.add(a.getCountry());
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

    //--------------- STATUS HISTO ---------------
    private String initStatusC() {
        String hist = new String();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        jTAStatus.removeAll();
        String query = "SELECT * FROM sb_customerStatus WHERE customer_id LIKE '" + current.getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ItemStatus status = new ItemStatus(rs.getInt("customerStatus_id"),
                        rs.getInt("status_number"),
                        rs.getDate("status_date"));
                hist += Helpers.convertDateToString(status.getStatusDate()) + " - " + status.toString() + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());

        }
        co.closeConnectionDatabase();
        return hist;
    }

    private String initStatusO() {
        String hist = new String();
        Order selectO = (Order) orderList.getSelectedValue();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        jTAStatus.removeAll();
        String query = "SELECT * FROM sb_orderStatus WHERE order_id LIKE '" + selectO.getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ItemStatus status = new ItemStatus(rs.getInt("orderStatus_id"),
                        rs.getInt("status_number"),
                        rs.getDate("status_date"));
                hist += Helpers.convertDateToString(status.getStatusDate()) + " - " + status.toString() + "\n";
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());

        }
        co.closeConnectionDatabase();
        return hist;
    }

    //--------------- ADD CUSTOMER ---------------
    //  Status jCombo
    private DefaultComboBoxModel initModelStatus() {
        return new DefaultComboBoxModel(initVectorStatus());
    }

    private Vector initVectorStatus() {
        Vector status = new Vector();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query;
        jComboStatus.removeAllItems();

        query = "SELECT * FROM sb_status WHERE status_number > 600 AND status_number < 700";

        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                status.add(new Status(rs.getInt("status_number"),
                        rs.getString("status_name")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return status;
        }

        co.closeConnectionDatabase();
        return status;
    }

    private void newCustStatus(Customer c) {
        Status selectS = (Status) jComboStatus.getSelectedItem();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {
            String query = "INSERT INTO sb_customerStatus "
                    + "(status_number, customer_id, status_date) VALUES "
                    + "(?, ?, GETDATE())";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, selectS.getNumber());
            pstmt.setInt(2, c.getId());

            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }

        co.closeConnectionDatabase();

    }

    private void addCustomer() throws ParseException {
        String dob = jTFDBirthAC.getText() + "/" + jTFMBirthAC.getText() + "/" + jTFYBirthAC.getText();
        Customer c = new Customer(0, jTFSurnameAC.getText(), jTFFirstnameAC.getText(),
                "password", jTFMailAC.getText(), jTFCellAC.getText(), jTFLandlineAC.getText(),
                Helpers.convertStringToDate(dob));
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "INSERT INTO sb_customer(customer_surname, customer_firstname, "
                + "customer_pwd, customer_mail,customer_cell, customer_landline, customer_dob) "
                + "VALUES ('" + c.getSurname() + "', '" + c.getFirstname() + "', '" + c.getPwd() + "',"
                + "'" + c.getMail() + "', '" + c.getCell() + "', '" + c.getLandline() + "', '" + Helpers.convertUtiltoSQLDate(c.getDob()) + "')";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());

        }
        newCustStatus(c);

        co.closeConnectionDatabase();
    }

    private Customer modCustomer() throws ParseException {
        String dob = jTFDBirthAC.getText() + "/" + jTFMBirthAC.getText() + "/" + jTFYBirthAC.getText();
        Customer m = new Customer(current.getId(), jTFSurnameAC.getText(), jTFFirstnameAC.getText(),
                current.getPwd(), jTFMailAC.getText(), jTFCellAC.getText(), jTFLandlineAC.getText(),
                Helpers.convertStringToDate(dob));
        m.updateCustomer();

        return m;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogStatus = new javax.swing.JDialog();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTAStatus = new javax.swing.JTextArea();
        jDialogAddCustomer = new javax.swing.JDialog();
        jLTitleAC = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jComboStatus = new javax.swing.JComboBox();
        jTFSurnameAC = new javax.swing.JTextField();
        jTFMailAC = new javax.swing.JTextField();
        jTFFirstnameAC = new javax.swing.JTextField();
        jTFCellAC = new javax.swing.JTextField();
        jTFLandlineAC = new javax.swing.JTextField();
        jTFDBirthAC = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTFMBirthAC = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTFYBirthAC = new javax.swing.JTextField();
        MCButton = new javax.swing.JButton();
        ACButton = new javax.swing.JButton();
        addAButton = new javax.swing.JButton();
        jDialogAddAddress = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jTFCityB1 = new javax.swing.JTextField();
        jTFCodeB1 = new javax.swing.JTextField();
        jTFCountryB1 = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTAAddressB1 = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTAOtherB1 = new javax.swing.JTextArea();
        addAOK = new javax.swing.JButton();
        jDialogSearchC = new javax.swing.JDialog();
        jLabel8 = new javax.swing.JLabel();
        jComboSearchC = new javax.swing.JComboBox();
        jTFSearchC = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jLiSearchC = new javax.swing.JList();
        OKSearchC = new javax.swing.JButton();
        jDialogAddresses = new javax.swing.JDialog();
        ModAButton = new javax.swing.JToggleButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTAddresses = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jDialogAddressesOK = new javax.swing.JButton();
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
        cStatusButton = new javax.swing.JButton();
        jButtonAddC = new javax.swing.JButton();
        jButtonModC = new javax.swing.JButton();
        cAddresses = new javax.swing.JButton();
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
        oStatusButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLOdelivery = new javax.swing.JLabel();
        jLObilling = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jDialogStatus.setTitle("Historique des statuts");
        jDialogStatus.setSize(new java.awt.Dimension(256, 372));

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLabel7.setText("Statuts");

        jButton3.setText("OK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTAStatus.setEditable(false);
        jTAStatus.setColumns(20);
        jTAStatus.setRows(5);
        jScrollPane4.setViewportView(jTAStatus);

        javax.swing.GroupLayout jDialogStatusLayout = new javax.swing.GroupLayout(jDialogStatus.getContentPane());
        jDialogStatus.getContentPane().setLayout(jDialogStatusLayout);
        jDialogStatusLayout.setHorizontalGroup(
            jDialogStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogStatusLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jDialogStatusLayout.createSequentialGroup()
                .addGap(0, 19, Short.MAX_VALUE)
                .addGroup(jDialogStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jDialogStatusLayout.setVerticalGroup(
            jDialogStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(26, 26, 26))
        );

        jDialogAddCustomer.setTitle("Création client");
        jDialogAddCustomer.setResizable(false);
        jDialogAddCustomer.setSize(new java.awt.Dimension(382, 532));

        jLTitleAC.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLTitleAC.setText("Création client");

        jPanel3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel3KeyPressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel9.setText("Nom de famille*");

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel10.setText("Mail*");

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel11.setText("Date de naissance*");

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel12.setText("Prénom*");

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel13.setText("Tel. mobile*");

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel14.setText("Tel. fixe");

        jLabel15.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel15.setText("Statut*");

        jComboStatus.setModel(initModelStatus());

        jTFSurnameAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFSurnameACActionPerformed(evt);
            }
        });

        jTFCellAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCellACActionPerformed(evt);
            }
        });

        jTFLandlineAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFLandlineACActionPerformed(evt);
            }
        });

        jTFDBirthAC.setText("jj");
        jTFDBirthAC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFDBirthACMouseClicked(evt);
            }
        });
        jTFDBirthAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFDBirthACActionPerformed(evt);
            }
        });

        jLabel16.setText("/");

        jTFMBirthAC.setText("mm");
        jTFMBirthAC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFMBirthACMouseClicked(evt);
            }
        });
        jTFMBirthAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFMBirthACActionPerformed(evt);
            }
        });

        jLabel17.setText("/");

        jTFYBirthAC.setText("aaaa");
        jTFYBirthAC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFYBirthACMouseClicked(evt);
            }
        });

        MCButton.setText("Enregistrer");
        MCButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCButtonActionPerformed(evt);
            }
        });

        ACButton.setText("Ajouter client");
        ACButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ACButtonActionPerformed(evt);
            }
        });

        addAButton.setText("Ajouter une adresse");
        addAButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTFCellAC, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(10, 10, 10)
                        .addComponent(jTFDBirthAC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFMBirthAC, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTFYBirthAC, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(87, 87, 87)
                        .addComponent(jTFMailAC))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(27, 27, 27))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(80, 80, 80)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFSurnameAC, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFFirstnameAC, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ACButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(MCButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(75, 75, 75)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addAButton)
                            .addComponent(jTFLandlineAC, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTFSurnameAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTFFirstnameAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTFDBirthAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jTFMBirthAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFYBirthAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTFMailAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTFCellAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTFLandlineAC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(addAButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MCButton)
                    .addComponent(ACButton))
                .addGap(88, 88, 88))
        );

        javax.swing.GroupLayout jDialogAddCustomerLayout = new javax.swing.GroupLayout(jDialogAddCustomer.getContentPane());
        jDialogAddCustomer.getContentPane().setLayout(jDialogAddCustomerLayout);
        jDialogAddCustomerLayout.setHorizontalGroup(
            jDialogAddCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddCustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogAddCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialogAddCustomerLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jLTitleAC)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jDialogAddCustomerLayout.setVerticalGroup(
            jDialogAddCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogAddCustomerLayout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addComponent(jLTitleAC)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Nouvelle adresse"));

        jLabel28.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel28.setText("Adresse*");

        jLabel29.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel29.setText("Complément");

        jLabel30.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel30.setText("Ville*");

        jLabel31.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel31.setText("Pays*");

        jLabel32.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel32.setText("Code postal*");

        jTFCityB1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCityB1ActionPerformed(evt);
            }
        });

        jTAAddressB1.setColumns(20);
        jTAAddressB1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTAAddressB1.setLineWrap(true);
        jTAAddressB1.setRows(5);
        jScrollPane8.setViewportView(jTAAddressB1);

        jTAOtherB1.setColumns(20);
        jTAOtherB1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTAOtherB1.setLineWrap(true);
        jTAOtherB1.setRows(5);
        jScrollPane9.setViewportView(jTAOtherB1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel29)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31)
                            .addComponent(jLabel28)
                            .addComponent(jLabel30))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTFCodeB1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(jTFCityB1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTFCountryB1, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addComponent(jScrollPane8))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFCityB1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jTFCodeB1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTFCountryB1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(17, 17, 17))
        );

        addAOK.setText("Ajouter");
        addAOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogAddAddressLayout = new javax.swing.GroupLayout(jDialogAddAddress.getContentPane());
        jDialogAddAddress.getContentPane().setLayout(jDialogAddAddressLayout);
        jDialogAddAddressLayout.setHorizontalGroup(
            jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddAddressLayout.createSequentialGroup()
                .addGroup(jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogAddAddressLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialogAddAddressLayout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(addAOK)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jDialogAddAddressLayout.setVerticalGroup(
            jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddAddressLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(addAOK)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jDialogSearchC.setTitle("Recherche client");
        jDialogSearchC.setModal(true);
        jDialogSearchC.setResizable(false);
        jDialogSearchC.setSize(new java.awt.Dimension(350, 351));

        jLabel8.setText("Rechercher un client par...");

        jComboSearchC.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nom", "Mail" }));
        jComboSearchC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboSearchCActionPerformed(evt);
            }
        });

        jTFSearchC.setForeground(new java.awt.Color(153, 153, 153));
        jTFSearchC.setText("Entrer un nom...");
        jTFSearchC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTFSearchCMouseClicked(evt);
            }
        });
        jTFSearchC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFSearchCActionPerformed(evt);
            }
        });
        jTFSearchC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFSearchCKeyTyped(evt);
            }
        });

        jScrollPane3.setViewportView(jLiSearchC);

        OKSearchC.setText("OK");
        OKSearchC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKSearchCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogSearchCLayout = new javax.swing.GroupLayout(jDialogSearchC.getContentPane());
        jDialogSearchC.getContentPane().setLayout(jDialogSearchCLayout);
        jDialogSearchCLayout.setHorizontalGroup(
            jDialogSearchCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchCLayout.createSequentialGroup()
                .addGroup(jDialogSearchCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogSearchCLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jDialogSearchCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jDialogSearchCLayout.createSequentialGroup()
                                .addComponent(jComboSearchC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jDialogSearchCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                                    .addComponent(jTFSearchC)))))
                    .addGroup(jDialogSearchCLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(OKSearchC)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jDialogSearchCLayout.setVerticalGroup(
            jDialogSearchCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogSearchCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jDialogSearchCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboSearchC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFSearchC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(OKSearchC)
                .addGap(36, 36, 36))
        );

        ModAButton.setText("Modifier");
        ModAButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModAButtonActionPerformed(evt);
            }
        });

        jTAddresses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Adresse", "Complément", "CP", "Ville", "Pays"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTAddresses.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(jTAddresses);
        if (jTAddresses.getColumnModel().getColumnCount() > 0) {
            jTAddresses.getColumnModel().getColumn(0).setResizable(false);
            jTAddresses.getColumnModel().getColumn(1).setResizable(false);
            jTAddresses.getColumnModel().getColumn(2).setResizable(false);
            jTAddresses.getColumnModel().getColumn(3).setResizable(false);
            jTAddresses.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton2.setText("Ajouter");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jDialogAddressesOK.setText("OK");
        jDialogAddressesOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDialogAddressesOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogAddressesLayout = new javax.swing.GroupLayout(jDialogAddresses.getContentPane());
        jDialogAddresses.getContentPane().setLayout(jDialogAddressesLayout);
        jDialogAddressesLayout.setHorizontalGroup(
            jDialogAddressesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddressesLayout.createSequentialGroup()
                .addGroup(jDialogAddressesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogAddressesLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jDialogAddressesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jDialogAddressesLayout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(ModAButton))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jDialogAddressesLayout.createSequentialGroup()
                        .addGap(341, 341, 341)
                        .addComponent(jDialogAddressesOK)))
                .addContainerGap(89, Short.MAX_VALUE))
        );
        jDialogAddressesLayout.setVerticalGroup(
            jDialogAddressesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogAddressesLayout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .addGroup(jDialogAddressesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(ModAButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jDialogAddressesOK)
                .addGap(33, 33, 33))
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
        jLIDV.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLIDVPropertyChange(evt);
            }
        });

        jLStatusV.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLStatusV.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLStatusVPropertyChange(evt);
            }
        });

        cStatusButton.setText("...");
        cStatusButton.setToolTipText("Historique des statuts");
        cStatusButton.setEnabled(false);
        cStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cStatusButtonActionPerformed(evt);
            }
        });

        jButtonAddC.setText("+");
        jButtonAddC.setToolTipText("Ajouter un client");
        jButtonAddC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddCActionPerformed(evt);
            }
        });

        jButtonModC.setText("Modifier");
        jButtonModC.setEnabled(false);
        jButtonModC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModCActionPerformed(evt);
            }
        });

        cAddresses.setText("Adresses...");
        cAddresses.setEnabled(false);
        cAddresses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cAddressesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
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
                            .addComponent(jLLandV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLStatus)
                                .addGap(18, 18, 18)
                                .addComponent(jLStatusV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLID)
                                .addGap(18, 18, 18)
                                .addComponent(jLIDV, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                        .addComponent(cStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cAddresses)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonModC, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonAddC)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSearch)
                    .addComponent(jButtonAddC)
                    .addComponent(jButtonModC))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(jLStatusV)
                            .addComponent(cStatusButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cAddresses)
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
        orderList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                orderListPropertyChange(evt);
            }
        });
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
        jLOstatus.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jLOstatusPropertyChange(evt);
            }
        });

        oStatusButton.setText("...");
        oStatusButton.setToolTipText("Historique des statuts");
        oStatusButton.setEnabled(false);
        oStatusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oStatusButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel2.setText("Adresse de livraison :");

        jLabel18.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel18.setText("Adresse de facturation :");

        jLOdelivery.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        jLObilling.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOprice))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOref))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLOdelivery)))
                        .addGap(80, 80, 80)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOdate)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLOstatus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(oStatusButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLObilling)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
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
                    .addComponent(jLOstatus)
                    .addComponent(oStatusButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLOdelivery))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(jLObilling))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

        setBounds(0, 0, 747, 675);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        // TODO add your handling code here:
        jDialogSearchC.setModal(true);
        jDialogSearchC.setVisible(true);
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void orderListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_orderListValueChanged
        // TODO add your handling code here:
        useOrderList();
        orderTable.setModel(initOrderLinesModel());


    }//GEN-LAST:event_orderListValueChanged

    private void cStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cStatusButtonActionPerformed
        // TODO add your handling code here:
        jTAStatus.setText(initStatusC());
        jLabel7.setText("Statuts client");
        jDialogStatus.setModal(true);
        jDialogStatus.setVisible(true);


    }//GEN-LAST:event_cStatusButtonActionPerformed

    private void jLStatusVPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLStatusVPropertyChange
        // TODO add your handling code here:
        if (!jLStatusV.getText().isEmpty()) {
            cStatusButton.setEnabled(true);
            cAddresses.setEnabled(true);
        }
    }//GEN-LAST:event_jLStatusVPropertyChange

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jDialogStatus.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void oStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oStatusButtonActionPerformed
        // TODO add your handling code here:
        jTAStatus.setText(initStatusO());
        jLabel7.setText("Statuts commande");
        jDialogStatus.setModal(true);
        jDialogStatus.setVisible(true);
    }//GEN-LAST:event_oStatusButtonActionPerformed

    private void orderListPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_orderListPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_orderListPropertyChange

    private void jLOstatusPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLOstatusPropertyChange
        // TODO add your handling code here:
        if (!jLOstatus.getText().isEmpty()) {
            oStatusButton.setEnabled(true);
        }
    }//GEN-LAST:event_jLOstatusPropertyChange

    private void jTFDBirthACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFDBirthACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFDBirthACActionPerformed

    private void jTFMBirthACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFMBirthACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFMBirthACActionPerformed

    private void jTFSurnameACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSurnameACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFSurnameACActionPerformed

    private void jTFCellACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCellACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCellACActionPerformed

    private void jTFDBirthACMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFDBirthACMouseClicked
        // TODO add your handling code here:
        if (jTFDBirthAC.getText().equalsIgnoreCase("jj")) {
            jTFDBirthAC.setText("");
        }
    }//GEN-LAST:event_jTFDBirthACMouseClicked

    private void jTFMBirthACMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFMBirthACMouseClicked
        // TODO add your handling code here:
        if (jTFMBirthAC.getText().equalsIgnoreCase("mm")) {
            jTFMBirthAC.setText("");
        }
    }//GEN-LAST:event_jTFMBirthACMouseClicked

    private void jTFYBirthACMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFYBirthACMouseClicked
        // TODO add your handling code here:
        if (jTFYBirthAC.getText().equalsIgnoreCase("aaaa")) {
            jTFYBirthAC.setText("");
        }
    }//GEN-LAST:event_jTFYBirthACMouseClicked

    private void jButtonAddCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddCActionPerformed
        // TODO add your handling code here:
        MCButton.setVisible(false);
        ACButton.setVisible(true);
        jLTitleAC.setText("Création client");
        jDialogAddCustomer.setModal(true);
        jDialogAddCustomer.setVisible(true);
        jTFSurnameAC.setText("");
        jTFFirstnameAC.setText("");
        jTFMailAC.setText("");
        jTFCellAC.setText("");
        jTFLandlineAC.setText("");
        jTFDBirthAC.setText("jj");
        jTFMBirthAC.setText("mm");
        jTFYBirthAC.setText("aaaa");
    }//GEN-LAST:event_jButtonAddCActionPerformed

    private void jPanel3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel3KeyPressed

    private void jTFLandlineACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFLandlineACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFLandlineACActionPerformed

    private void jTFCityB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCityB1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCityB1ActionPerformed

    private void jButtonModCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModCActionPerformed
        // TODO add your handling code here:

        jTFSurnameAC.setText("");
        jTFFirstnameAC.setText("");
        jTFMailAC.setText("");
        jTFCellAC.setText("");
        jTFLandlineAC.setText("");
        jTFDBirthAC.setText("jj");
        jTFMBirthAC.setText("mm");
        jTFYBirthAC.setText("aaaa");

        jTFSurnameAC.setText(current.getSurname());
        jTFFirstnameAC.setText(current.getFirstname());
        jTFMailAC.setText(current.getMail());
        jTFCellAC.setText(current.getCell());
        jTFLandlineAC.setText(current.getLandline());
        String dob = Helpers.convertDateToString(current.getDob());
        jTFDBirthAC.setText(dob.substring(0, 2));
        jTFMBirthAC.setText(dob.substring(3, 5));
        jTFYBirthAC.setText(dob.substring(6, 10));

        jLTitleAC.setText("Modification client");
        MCButton.setVisible(true);
        ACButton.setVisible(false);
        jDialogAddCustomer.setModal(true);
        jDialogAddCustomer.setVisible(true);


    }//GEN-LAST:event_jButtonModCActionPerformed

    private void jLIDVPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLIDVPropertyChange
        // TODO add your handling code here:
        if (jLIDV.getText().isEmpty()) {
            jButtonModC.setEnabled(false);
        } else {
            jButtonModC.setEnabled(true);
        }
    }//GEN-LAST:event_jLIDVPropertyChange

    private void ACButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACButtonActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            addCustomer();
        } catch (ParseException ex) {
            Logger.getLogger(JFCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDialogAddCustomer.dispose();

    }//GEN-LAST:event_ACButtonActionPerformed

    private void MCButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCButtonActionPerformed

        try {
            // TODO add your handling code here:
            current = modCustomer();
        } catch (ParseException ex) {
            Logger.getLogger(JFCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }

        jDialogAddCustomer.dispose();
        jLNameV.setText(current.getFirstname() + " " + current.getSurname().toUpperCase());
        jLMailV.setText(current.getMail());
        jLBirthV.setText(Helpers.convertDateToString(current.getDob()));
        jLCellV.setText(current.getCell());
        jLLandV.setText(current.getLandline());
        jLIDV.setText(Integer.toString(current.getId()));
        jLStatusV.setText(current.getStatusList().lastElement().toString());
    }//GEN-LAST:event_MCButtonActionPerformed

    private void jTFSearchCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSearchCActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTFSearchCActionPerformed

    private void jComboSearchCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboSearchCActionPerformed
        // TODO add your handling code here:
        if (jComboSearchC.getSelectedIndex() == 0) {
            jTFSearchC.setForeground(Color.darkGray);
            jTFSearchC.setText("Entrer un nom...");
        }
        if (jComboSearchC.getSelectedIndex() == 1) {
            jTFSearchC.setForeground(Color.darkGray);
            jTFSearchC.setText("Entrer une adresse mail...");
        }
    }//GEN-LAST:event_jComboSearchCActionPerformed

    private void jTFSearchCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFSearchCMouseClicked
        // TODO add your handling code here:
        jTFSearchC.setForeground(Color.black);
        jTFSearchC.setText("");
    }//GEN-LAST:event_jTFSearchCMouseClicked

    private void jTFSearchCKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSearchCKeyTyped
        // TODO add your handling code here:
        if (jTFSearchC.getText().length() > 2) {
            jLiSearchC.setModel(empowerSearchC(permaCo.getConnexion()));
        }
    }//GEN-LAST:event_jTFSearchCKeyTyped

    private void OKSearchCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKSearchCActionPerformed
        // TODO add your handling code here:
        current = (Customer) jLiSearchC.getSelectedValue();
        jDialogSearchC.dispose();

//infos client
        jLNameV.setText(current.getFirstname() + " " + current.getSurname().toUpperCase());
        jLMailV.setText(current.getMail());
        jLBirthV.setText(Helpers.convertDateToString(current.getDob()));
        jLCellV.setText(current.getCell());
        jLLandV.setText(current.getLandline());
        jLIDV.setText(Integer.toString(current.getId()));
        jLStatusV.setText(current.getStatusList().lastElement().toString());
//infos commande
        try {
            orderList.setModel(initOrderList());
            if (orderList.getWidth() >= 0) {
                orderList.setSelectedIndex(0);
                useOrderList();
                orderTable.setModel(initOrderLinesModel());
            }
        } catch (NoSuchElementException ex) {
            System.err.println("No order for customer " + current.getId() + " (" + ex.getMessage() + ")");
        }
    }//GEN-LAST:event_OKSearchCActionPerformed

    private void addAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAButtonActionPerformed
        // TODO add your handling code here:
        jDialogAddAddress.setVisible(true);
        jDialogAddAddress.setModal(true);
    }//GEN-LAST:event_addAButtonActionPerformed

    private void cAddressesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cAddressesActionPerformed
        // TODO add your handling code here:
        jDialogAddresses.setVisible(true);
        jDialogAddresses.setModal(true);
        jTAddresses.setModel(initAddressesModel());
    }//GEN-LAST:event_cAddressesActionPerformed

    private void ModAButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModAButtonActionPerformed
        // TODO add your handling code here:
        jTAddresses.setEditingRow(jTAddresses.getSelectedRow());
    }//GEN-LAST:event_ModAButtonActionPerformed

    private void addAOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAOKActionPerformed
        // TODO add your handling code here:
        if(!jTAAddressB1.getText().isEmpty() || !jTFCityB1.getText().isEmpty() 
               || !jTFCodeB1.getText().isEmpty() || !jTFCountryB1.getText().isEmpty()){
            Address addA = new Address(0,jTAAddressB1.getText(), jTAOtherB1.getText(),
            jTFCodeB1.getText(), jTFCityB1.getText(), jTFCountryB1.getText());
            addA.add();
        }else{
            
        }
        jTAddresses.setModel(initAddressesModel());
        jDialogAddAddress.dispose();
    }//GEN-LAST:event_addAOKActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTAAddressB1.setText("");
        jTAOtherB1.setText("");
        jTFCodeB1.setText("");
        jTFCityB1.setText("");
        jTFCountryB1.setText("");
        jDialogAddAddress.setModal(true);
        jDialogAddAddress.setVisible(true);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jDialogAddressesOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDialogAddressesOKActionPerformed
        // TODO add your handling code here:
        jDialogAddresses.dispose();
    }//GEN-LAST:event_jDialogAddressesOKActionPerformed

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
    private javax.swing.JButton ACButton;
    private javax.swing.JButton MCButton;
    private javax.swing.JToggleButton ModAButton;
    private javax.swing.JButton OKSearchC;
    private javax.swing.JButton addAButton;
    private javax.swing.JButton addAOK;
    private javax.swing.JButton cAddresses;
    private javax.swing.JButton cStatusButton;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonAddC;
    private javax.swing.JButton jButtonModC;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JComboBox jComboSearchC;
    private javax.swing.JComboBox jComboStatus;
    private javax.swing.JDialog jDialogAddAddress;
    private javax.swing.JDialog jDialogAddCustomer;
    private javax.swing.JDialog jDialogAddresses;
    private javax.swing.JButton jDialogAddressesOK;
    private javax.swing.JDialog jDialogSearchC;
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
    private javax.swing.JLabel jLObilling;
    private javax.swing.JLabel jLOdate;
    private javax.swing.JLabel jLOdelivery;
    private javax.swing.JLabel jLOprice;
    private javax.swing.JLabel jLOref;
    private javax.swing.JLabel jLOstatus;
    private javax.swing.JLabel jLStatus;
    private javax.swing.JLabel jLStatusV;
    private javax.swing.JLabel jLTitleAC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jLiSearchC;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTAAddressB1;
    private javax.swing.JTextArea jTAOtherB1;
    private javax.swing.JTextArea jTAStatus;
    private javax.swing.JTable jTAddresses;
    private javax.swing.JTextField jTFCellAC;
    private javax.swing.JTextField jTFCityB1;
    private javax.swing.JTextField jTFCodeB1;
    private javax.swing.JTextField jTFCountryB1;
    private javax.swing.JTextField jTFDBirthAC;
    private javax.swing.JTextField jTFFirstnameAC;
    private javax.swing.JTextField jTFLandlineAC;
    private javax.swing.JTextField jTFMBirthAC;
    private javax.swing.JTextField jTFMailAC;
    private javax.swing.JTextField jTFSearchC;
    private javax.swing.JTextField jTFSurnameAC;
    private javax.swing.JTextField jTFYBirthAC;
    private javax.swing.JButton oStatusButton;
    private javax.swing.JList orderList;
    private javax.swing.JTable orderTable;
    // End of variables declaration//GEN-END:variables
    private Customer current = new Customer();
    private ConnectSQLS permaCo = new ConnectSQLS();
    private int test = 0;

}
