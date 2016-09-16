
package classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
Gab
   

*/
public class Address {
    
    //p
    private int id;
    private String street; 
    private String other;
    private String zipcode;
    private String city; 
    private String country; 
    
    // constructeur par défaut

    public Address() {
    }
    
    
    // constructeur avec les champs obligatoires

    public Address(int id, String zipcode, String city, String country) {
        this.id = id;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }
    
    // surcharge du constructeur avec l'ajout des champs non obligatoires

    
    public Address(int id, String street, String other, String zipcode, String city, String country) {
        this(id, zipcode, city, country);
        this.street = street;
        this.other = other;
    }
    
    //g&s

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    //m
    @Override
    public String toString(){
        String info = street + " - " + zipcode + " " + city + " ("+ country +")";
        return info;
    }

    public void add(){
       
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {
            String query = "INSERT INTO sb_address VALUES "
                    + "address_street = ?,"
                    + "address_other = ?,"
                    + "address_zipcode = ?,"
                    + "address_city = ?,"
                    + "address_country = ?,";
            
            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, "'"+street+"'");
            pstmt.setString(2, "'"+other+"'");
            pstmt.setString(3, "'"+zipcode+"'");
            pstmt.setString(4, "'"+city+"'");
            pstmt.setString(5, "'"+country+"'");

            
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }

        co.closeConnectionDatabase();
    }
    
    public void update(){
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        try {
            String query = "UPDATE sb_customer SET"
                    + "address_street = ?,"
                    + "address_other = ?,"
                    + "address_zipcode = ?,"
                    + "address_city = ?,"
                    + "address_country = ?"
                    + " WHERE address_id = ?";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, "'"+street+"'");
            pstmt.setString(2, "'"+other+"'");
            pstmt.setString(3, "'"+zipcode+"'");
            pstmt.setString(4, "'"+city+"'");
            pstmt.setString(5, "'"+country+"'");
            pstmt.setInt(6, id);

            
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }

        co.closeConnectionDatabase();
    }
    
    
}
