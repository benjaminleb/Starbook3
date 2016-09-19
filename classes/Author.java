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
    public void updateAuthor(String surname, String firstname, Date dob, Date dod, int id) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {

            //String query = "UPDATE sb_author SET VALUES(?, ?, ?, ?) WHERE ID = " + id;
            String query = "UPDATE sb_author SET author_surname = ?, author_surname = ?, author_dob = ?, author_dod = ? WHERE author_id = " + id;
            PreparedStatement stmt = co.getConnexion().prepareStatement(query);
            stmt.setString(1, surname);
            stmt.setString(2, firstname);
            stmt.setDate(3, Helpers.convertUtiltoSQLDate(dob));
            stmt.setDate(4, Helpers.convertUtiltoSQLDate(dob));
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

            String query = "INSERT INTO sb_author VALUES ("
                    + "?,?,?,?)";
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, surname);
            pstmt.setString(2, firstname);
            pstmt.setDate(3, (java.sql.Date) dob);
            pstmt.setDate(4, (java.sql.Date) dod);
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " +ex.getMessage());
        }

        co.closeConnectionDatabase();
    }


    @Override
    public String toString() {
        return  surname + " " + firstname ;
    }

}
