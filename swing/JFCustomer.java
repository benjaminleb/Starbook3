
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
    }

    
    // --------------- ORDER jLIST ---------------
    private void useOrderList() {

        Order o = (Order) orderList.getSelectedValue();
        jLOref.setText(Integer.toString(o.getId()));
        jLOprice.setText(Float.toString(o.calculatePrice()) + " €");
        jLOdate.setText(Helpers.convertDateToString(o.getDate()));
        jLOstatus.setText(o.getStatusList().lastElement().toString());

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

    
    //--------------- CUSTOMER SEARCH ---------------
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
    
    //--------------- STATUS HISTO ---------------
    private String initStatusC() {
        String hist = new String();
        Customer selectC = (Customer) jCBCustomerSearch.getSelectedItem();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        jTAStatus.removeAll();
        String query = "SELECT * FROM sb_customerStatus WHERE customer_id LIKE '" + selectC.getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ItemStatus status = new ItemStatus(rs.getInt("customerStatus_id"),
                        rs.getInt("status_number"),
                        rs.getDate("status_date"));
                hist += Helpers.convertDateToString(status.getStatusDate())+" - "+status.toString()+"\n";
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
                hist += Helpers.convertDateToString(status.getStatusDate())+" - "+status.toString()+"\n";
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
        jCBCustomerSearch.removeAllItems();
        
            query = "SELECT * FROM sb_status WHERE status_number > 600 AND status_number < 700";
                   
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                //public Status(int number, String name){
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
    
    private void addCustomer() throws ParseException{
        //(int id, String surname, String firstname, String pwd, String mail, String cell, String landline, Date dob)
        String dob = jTFDBirthAC.getText()+"/"+jTFMBirthAC.getText()+"/"+jTFYBirthAC.getText();
        Customer c = new Customer(0, jTFSurnameAC.getText(), jTFFirstnameAC.getText(), 
                "password", jTFCellAC.getText(), jTFLandlineAC.getText(), 
                Helpers.convertStringToDate(dob));
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "INSERT INTO sb_customer(customer_surname, customer_firstname, "
                + "customer_pwd, customer_mail,customer_cell, customer_landline, customer_dob) "
                + "VALUES ('"+c.getSurname()+"', '"+c.getFirstname()+"', '"+c.getPwd()+"',"
                + "'"+c.getMail()+"', '"+c.getCell()+"', '"+c.getLandline()+"', '"+Helpers.convertUtiltoSQLDate(c.getDob())+"')";                   
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);
       
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
           
        }

        co.closeConnectionDatabase();
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
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTAStatus = new javax.swing.JTextArea();
        buttonGroupSearch = new javax.swing.ButtonGroup();
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
        jComboBox1 = new javax.swing.JComboBox();
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
        jButton5 = new javax.swing.JButton();
        ACButton = new javax.swing.JToggleButton();
        jButton6 = new javax.swing.JButton();
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
        jPanel8 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTAAddressD1 = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTAOtherD1 = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jTFCityD1 = new javax.swing.JTextField();
        jTFCodeD1 = new javax.swing.JTextField();
        jTFCountryD1 = new javax.swing.JTextField();
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
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jDialogSearch.setTitle("Recherche de client");
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
        jTFNameSearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTFNameSearch1KeyPressed(evt);
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

        jComboBox1.setModel(initModelStatus());

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

        jButton5.setText("Adresses");

        ACButton.setText("Ajouter Client");
        ACButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ACButtonActionPerformed(evt);
            }
        });

        jButton6.setText("Enregistrer");

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
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFFirstnameAC, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(75, 75, 75)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(jTFLandlineAC)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(ACButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
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
                    .addComponent(jLTitleAC)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 21, Short.MAX_VALUE))
        );
        jDialogAddCustomerLayout.setVerticalGroup(
            jDialogAddCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddCustomerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLTitleAC)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Adresse de facturation"));

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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Adresse de livraison"));
        jPanel8.setPreferredSize(new java.awt.Dimension(290, 314));

        jTAAddressD1.setColumns(20);
        jTAAddressD1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTAAddressD1.setLineWrap(true);
        jTAAddressD1.setRows(5);
        jScrollPane10.setViewportView(jTAAddressD1);

        jTAOtherD1.setColumns(20);
        jTAOtherD1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        jTAOtherD1.setLineWrap(true);
        jTAOtherD1.setRows(5);
        jScrollPane11.setViewportView(jTAOtherD1);

        jLabel33.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel33.setText("Adresse*");

        jLabel34.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel34.setText("Complément");

        jLabel35.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel35.setText("Ville*");

        jLabel36.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel36.setText("Code postal*");

        jLabel37.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel37.setText("Pays*");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel35)
                                .addComponent(jLabel36)
                                .addComponent(jLabel37))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTFCountryD1, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                    .addComponent(jTFCodeD1))
                                .addComponent(jTFCityD1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jTFCityD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jTFCodeD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jTFCountryD1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialogAddAddressLayout = new javax.swing.GroupLayout(jDialogAddAddress.getContentPane());
        jDialogAddAddress.getContentPane().setLayout(jDialogAddAddressLayout);
        jDialogAddAddressLayout.setHorizontalGroup(
            jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 688, Short.MAX_VALUE)
            .addGroup(jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialogAddAddressLayout.createSequentialGroup()
                    .addGap(79, 79, 79)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(37, Short.MAX_VALUE)))
        );
        jDialogAddAddressLayout.setVerticalGroup(
            jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
            .addGroup(jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialogAddAddressLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jDialogAddAddressLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                                .addComponent(jLOref)))
                        .addGap(138, 138, 138)
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
                                .addGap(28, 28, 28))))))
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oStatusButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel5)
                        .addComponent(jLOprice)
                        .addComponent(jLOstatus)))
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

    private void jTFNameSearch1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFNameSearch1KeyPressed
        // TODO add your handling code here
    }//GEN-LAST:event_jTFNameSearch1KeyPressed

    private void cStatusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cStatusButtonActionPerformed
        // TODO add your handling code here:
        jTAStatus.setText(initStatusC());
        jLabel7.setText("Statuts client");
        jDialogStatus.setModal(true);
        jDialogStatus.setVisible(true);
        
        
    }//GEN-LAST:event_cStatusButtonActionPerformed

    private void jLStatusVPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLStatusVPropertyChange
        // TODO add your handling code here:
        if(!jLStatusV.getText().isEmpty()){
            cStatusButton.setEnabled(true);
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
        if(!jLOstatus.getText().isEmpty()){
            oStatusButton.setEnabled(true);
        }
    }//GEN-LAST:event_jLOstatusPropertyChange

    private void jTFDBirthACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFDBirthACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFDBirthACActionPerformed

    private void jTFMBirthACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFMBirthACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFMBirthACActionPerformed

    private void ACButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACButtonActionPerformed
        try {
            // TODO add your handling code here:
            addCustomer();
        } catch (ParseException ex) {
            Logger.getLogger(JFCustomer.class.getName()).log(Level.SEVERE, null, ex);
        }
        jDialogAddCustomer.dispose();
        
        
    }//GEN-LAST:event_ACButtonActionPerformed

    private void jTFSurnameACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSurnameACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFSurnameACActionPerformed

    private void jTFCellACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCellACActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCellACActionPerformed

    private void jTFDBirthACMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFDBirthACMouseClicked
        // TODO add your handling code here:
        jTFDBirthAC.setText("");
    }//GEN-LAST:event_jTFDBirthACMouseClicked

    private void jTFMBirthACMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFMBirthACMouseClicked
        // TODO add your handling code here:
        jTFMBirthAC.setText("");
    }//GEN-LAST:event_jTFMBirthACMouseClicked

    private void jTFYBirthACMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTFYBirthACMouseClicked
        // TODO add your handling code here:
        jTFYBirthAC.setText("");
    }//GEN-LAST:event_jTFYBirthACMouseClicked

    private void jButtonAddCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddCActionPerformed
        // TODO add your handling code here:
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
        Customer modC = (Customer) jCBCustomerSearch.getSelectedItem();
        jDialogAddCustomer.setModal(true);
        jDialogAddCustomer.setVisible(true);
        jTFSurnameAC.setText(modC.getSurname());
        jTFFirstnameAC.setText(modC.getFirstname());
        jTFMailAC.setText(modC.getMail());
        jTFCellAC.setText(modC.getCell());
        if(modC.getLandline() != null){
            jTFLandlineAC.setText(modC.getLandline());
        }else{
            jTFLandlineAC.setText("");
        }
        String dob = Helpers.convertDateToString(modC.getDob());
        jTFDBirthAC.setText(dob.substring(0, 1));
        jTFMBirthAC.setText(dob.substring(3, 4));
        jTFYBirthAC.setText(dob.substring(6, 9));
        
        
        
    }//GEN-LAST:event_jButtonModCActionPerformed

    private void jLIDVPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jLIDVPropertyChange
        // TODO add your handling code here:
        if(jLIDV.getText().isEmpty()){
            jButtonModC.setEnabled(false);
        }else{
           jButtonModC.setEnabled(true); 
        }
    }//GEN-LAST:event_jLIDVPropertyChange

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
    private javax.swing.JToggleButton ACButton;
    private javax.swing.ButtonGroup buttonGroupSearch;
    private javax.swing.JButton cStatusButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButtonAddC;
    private javax.swing.JButton jButtonModC;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JComboBox jCBCustomerSearch;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDialog jDialogAddAddress;
    private javax.swing.JDialog jDialogAddCustomer;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRBMail;
    private javax.swing.JRadioButton jRBName;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTAAddressB1;
    private javax.swing.JTextArea jTAAddressD1;
    private javax.swing.JTextArea jTAOtherB1;
    private javax.swing.JTextArea jTAOtherD1;
    private javax.swing.JTextArea jTAStatus;
    private javax.swing.JTextField jTFCellAC;
    private javax.swing.JTextField jTFCityB1;
    private javax.swing.JTextField jTFCityD1;
    private javax.swing.JTextField jTFCodeB1;
    private javax.swing.JTextField jTFCodeD1;
    private javax.swing.JTextField jTFCountryB1;
    private javax.swing.JTextField jTFCountryD1;
    private javax.swing.JTextField jTFDBirthAC;
    private javax.swing.JTextField jTFFirstnameAC;
    private javax.swing.JTextField jTFLandlineAC;
    private javax.swing.JTextField jTFMBirthAC;
    private javax.swing.JTextField jTFMailAC;
    private javax.swing.JTextField jTFMailSearch;
    private javax.swing.JTextField jTFNameSearch1;
    private javax.swing.JTextField jTFNameSearch2;
    private javax.swing.JTextField jTFSurnameAC;
    private javax.swing.JTextField jTFYBirthAC;
    private javax.swing.JButton oStatusButton;
    private javax.swing.JList orderList;
    private javax.swing.JTable orderTable;
    // End of variables declaration//GEN-END:variables
}
