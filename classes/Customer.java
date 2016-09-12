package classes;

/*
 Gab
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*; // ATTENTION A GERER LA DATE ET AJUSTER LE TYPE DATE 
// OU DATETIME

public class Customer {

        //p
    private int id;
    private String surname;
    private String firstname;
    private String pwd;
    private String mail;
    private String cell;
    private String landline;
    private Date dob;

        // constructeur par défaut
    public Customer() {
    }

        // constructeur  avec les champs obligatoires
    public Customer(int id, String surname, String firstname, String pwd, String mail, String cell, Date dob) {
        this.id = id;
        this.surname = surname;
        this.firstname = firstname;
        this.pwd = pwd;
        this.mail = mail;
        this.cell = cell;
        this.dob = dob;
    }

        // surcharge du constructeur avec l'ajout des champs non obligatoires
        // ajout de landline
    public Customer(int id, String surname, String firstname, String pwd, String mail, String cell, String landline, Date dob) {
        this(id, surname, firstname, pwd, mail, cell, dob);
        this.landline = landline;

    }
    
    //g&s
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
       //AJOUTER CONTRAINTE DOB > 1900 
        this.dob = dob;
    }
    
    //m
    @Override
    public String toString() {
        String info = firstname+" "+surname.toUpperCase()+" - Ref "+id;
        return info;
    }
    
    
    //  METHODES EN CHANTIER/MEDITATION /!\
    /* 
    public ArrayList getStatusList(){
        ArrayList statusList = new ArrayList();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_Status WHERE status_number LIKE '" + id + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                statusList.add(new Customer(rs.getInt("customer_id"),
                        rs.getString("customer_surname"),
                        rs.getString("customer_firstname"),
                        rs.getString("customer_pwd"),
                        rs.getString("customer_email"),
                        rs.getString("customer_cell"),
                        rs.getString("customer_landline"),
                        rs.getDate("customer_dob")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return statusList;
        }

        co.closeConnectionDatabase();
        return statusList;
    }
    
    public String getCurrentStatus(){
        String currentStatus = "Statut inconnu";
        int statusNumber = getStatusNumber();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_Status WHERE status_number LIKE '" + statusNumber + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
               currentStatus = rs.getString("status_name");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return currentStatus;
        }

        co.closeConnectionDatabase();
        return currentStatus;
    }
    
    public int getStatusNumber (){
        
        int statusNumber = 601;
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_CustomerStatus WHERE customer_id LIKE '" + id + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
               statusNumber = rs.getInt("status_number");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return statusNumber;
        }

        co.closeConnectionDatabase();
        
        
        return statusNumber;
    }
*/

}
