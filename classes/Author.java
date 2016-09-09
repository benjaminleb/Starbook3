package classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import swing.JFAddAuthor;

/*
 ben
 */
public class Author {

    //p
    private int id;
    private String surname;
    private String firstname;
    private Date dob;
    private Date dod;

    //c
    public Author() {
    }

    public Author(int id, String surname) {
        this.id = id;
        this.surname = surname;
    }

    public Author(int id, String surname, String firstname, Date dob, Date dod) {
        this(id, surname);
        this.firstname = firstname;
        this.dob = dob;
        this.dod = dod;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDod() {
        return dod;
    }

    public void setDod(Date dod) {
        this.dod = dod;
    }

    //m
    public void updateAuthor(String surname, String firstname, Date dod, Date dob, int id) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {

            String query = "UPDATE author SET VALUES(?, ?, ?, ?) WHERE ID = " + id;
            PreparedStatement stmt = co.getConnexion().prepareStatement(query);
            stmt.setString(1, surname);
            stmt.setString(2, firstname);
            //>>>>>>>>>>>>>>>>>>>> PROBLEME DE DATE
            stmt.setDate(3, (java.sql.Date) dod);
            stmt.setDate(4, (java.sql.Date) dob);
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops : SQL Connexion : " + ex.getMessage());
            return;
        }
    }

    public void insertAuthor() {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {

            String query = "INSERT INTO starbook VALUES ("
                    + "?,"
                    + "?,"
                    + "?,"
                    + "?)";
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, surname);
            pstmt.setString(2, firstname);
            pstmt.setDate(3, (java.sql.Date) dob);
            pstmt.setDate(4, (java.sql.Date) dod);

            int result = pstmt.executeUpdate();
            System.out.println("result:" + result);
            pstmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(JFAddAuthor.class.getName()).log(Level.SEVERE, null, ex);
        }

        co.closeConnectionDatabase();
    }

}
