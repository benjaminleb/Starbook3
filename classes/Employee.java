package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Vector;

/*
 Gab 
 */
public class Employee {

        //p
    private int id;
    private String surname;
    private String firstname;
    private String pwd;
    private String mail;
    private String phone;

        // constructeur par défaut
    public Employee() {
    }

        // constructeur avec les champs obligatoires
    public Employee(int id, String surname, String firstname, String pwd, String mail) {
        this.id = id;
        this.surname = surname;
        this.firstname = firstname;
        this.pwd = pwd;
        this.mail = mail;
    }

        // surcharge du constructeur avec l'ajout des champs non obligatoires
    public Employee(int id, String surname, String firstname, String pwd, String mail, String phone) {
        this(id, surname, firstname, pwd, mail);
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    //m
    
    
    public void insertEmployee() {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_employee VALUES ("
                    + "?,?,?,?,?)";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, surname);
            pstmt.setString(2, firstname);
            pstmt.setString(5, Log.convertPassword(pwd));
            pstmt.setString(3, mail);
            pstmt.setString(4, phone);
            pstmt.execute();
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " +ex.getMessage());
        }
        
        co.closeConnectionDatabase();
    }
    
    
    public void updateEmployee(String firstname, String surname, String email, String phone, int id) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {
            String query = "UPDATE sb_employee SET employee_firstname = ?, employee_surname = ?,"
                    + " employee_mail = ?, employee_phone = ? WHERE employee_id = " + id;
            PreparedStatement stmt = co.getConnexion().prepareStatement(query);
            stmt.setString(1, firstname);
            stmt.setString(2, surname);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops : SQL Connexion : " + ex.getMessage());
            return;
        }
    }
    
    public Vector getStatusList(){
        Vector<ItemStatus> statusList = new Vector<ItemStatus>();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_employeeStatus WHERE employee_id LIKE '" + id + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                statusList.add(new ItemStatus(rs.getInt("employee_id"),
                        rs.getInt("status_number"),
                        rs.getDate("status_date")));
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

    @Override
    public String toString() {
        return  surname + " " + firstname;
    }
    
    
    public void insertEmployeeStatus(Status status) {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_employeeStatus VALUES ("
                    + "?,?,GETDATE())";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, status.getNumber());
            pstmt.setInt(2, id);
            pstmt.execute();
            
            
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }

        co.closeConnectionDatabase();
    }
    
    
    public static void insertEmployeeStatus(int status_number, int employee_id) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_employeeStatus VALUES ("
                    + "?, ?, GETDATE())";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, status_number);
            pstmt.setInt(2, employee_id);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }
        co.closeConnectionDatabase();
    }
}
    

