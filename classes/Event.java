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
public class Event {

    //p
    private int id;
    private String name;
    private Date start;
    private Date end;
    private float discountRate;
    private String picture;

    //c
    public Event() {
    }

    public Event(int id, String name, float discountRate) {
        this.id = id;
        this.name = name;
        this.discountRate = discountRate;
    }

    public Event(int id, String name, Date start, Date end, float discountRate, String picture) {
        this(id, name, discountRate);
        this.start = start;
        this.end = end;
        this.picture = picture;
    }

    //g&s
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    //m
    public void updateEvent(String name, Date startdate, Date enddate, Float discountrate, int id) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {

            String query = "UPDATE event SET VALUES(?, ?, ?, ?) WHERE ID = " + id;
            PreparedStatement stmt = co.getConnexion().prepareStatement(query);
            stmt.setString(1, name);
            stmt.setDate(2, (java.sql.Date) startdate);
            stmt.setDate(3, (java.sql.Date) enddate);
            stmt.setFloat(4, discountrate);
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops : SQL Connexion : " + ex.getMessage());
            return;
        }
    }

    public void insertEvent() {
        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_event VALUES ("
                    + "?,?,?,?,?)";
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setDate(2, (java.sql.Date) start);
            pstmt.setDate(3, (java.sql.Date) end);
            pstmt.setFloat(4, discountRate);
            pstmt.setString(5, picture);

            int result = pstmt.executeUpdate();
            System.out.println("result:" + result);
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }
        co.closeConnectionDatabase();

    }

    public void insertBookEvent(Book b) {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_bookEvent VALUES ("
                    + "?,?)";
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.setString(2, isbn);

            int result = pstmt.executeUpdate();
            System.out.println("result:" + result);
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }
        co.closeConnectionDatabase();

    }

    @Override
    public String toString() {
        return name;
    }

}
