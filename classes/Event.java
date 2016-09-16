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
    private String start;
    private String end;
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

    public Event(int id, String name, String start, String end, float discountRate, String picture) {
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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
    public static void updateEvent(String name, Date startdate, Date enddate, Float discountrate, int id) {
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
        co.closeConnectionDatabase();
    }
    
    
    public static void insertBookEvent2(int event_id, String book_isbn) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {
            String query = "INSERT INTO sb_bookEvent VALUES (?, ?)";
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, event_id);
            pstmt.setString(2, book_isbn);
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }
        co.closeConnectionDatabase();   
    }
    
    public static void deleteBookEvent(int event_id, String book_isbn) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {
            String query = "DELETE FROM sb_bookEvent WHERE sb_bookEvent.event_id = ? AND sb_bookEvent.book_isbn = ?";
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, event_id);
            pstmt.setString(2, book_isbn);
            pstmt.close();
        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }
        co.closeConnectionDatabase();   
    }
    
    public void insertEvent() {
        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_event VALUES ("
                    + "?,?,?,?,?)";
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, start);
            pstmt.setString(3, end);
            pstmt.setFloat(4, discountRate);
            pstmt.setString(5, picture);

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
            pstmt.setString(2, b.getIsbn());
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
