
package classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

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
            stmt.setDate(2, startdate);
            stmt.setDate(3, enddate);
            stmt.setFloat(4, discountrate);
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops : SQL Connexion : " + ex.getMessage());
            return;
        }
    }
    
}
