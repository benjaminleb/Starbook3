package classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            pstmt.setString(3, pwd);
            pstmt.setString(4, mail);
            pstmt.setString(5, phone);
            

            int result = pstmt.executeUpdate();
            System.out.println("result:" + result);
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " +ex.getMessage());
        }
        
        co.closeConnectionDatabase();

    }
}



