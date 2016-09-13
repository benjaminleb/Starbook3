package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import swing.JFAddAuthor;

/*
 ben
 */
public class Book {

    //p
    private String isbn;
    private Publisher publisher;
    private String title;
    private String subtitle;
    private Date date;
    private String picture;
    private String summary;
    private String idiom;
    private float price;
    private Tax tax;
    private int quantity;
    private String pages;
    private String print;
    private int weight;

    //c
    public Book() {
    }

    public Book(String isbn, String title, String subtitle, float price) {
        this.isbn = isbn;
        this.title = title;
        this.subtitle = subtitle;
        
        this.price = price;
    }
    
    

    public Book(String isbn, Publisher publisher, String title, Date date, float price, Tax tax, int quantity) {
        this.isbn = isbn;
        this.publisher = publisher;
        this.title = title;
        this.date = date;
        this.price = price;
        this.tax = tax;
        this.quantity = quantity;
    }

    public Book(String isbn, Publisher publisher, String title,
            String subtitle, Date date, String picture, String summary, String idiom,
            float price, Tax tax, int quantity, String pages, String print, int weight) {
        this(isbn, publisher, title, date, price, tax, quantity);
        this.subtitle = subtitle;
        this.date = date;
        this.picture = picture;
        this.summary = summary;
        this.idiom = idiom;
        this.pages = pages;
        this.print = print;
        this.weight = weight;
    }

    //g&s
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        if (Integer.valueOf(pages) > 0) {
            this.pages = pages;
        }
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    //m
    public float calculateInclTax() {
        float tPrice;
        tPrice = price + ((price) * (tax.getRate()) / 100);
        //troncate to xx,xx
        tPrice = ((float) ((int) (tPrice * 100))) / 100;
        return tPrice;
    }

    public String toString() {
        return title;
    }
    
    public void insertBook() {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_book VALUES ("
                    + "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, isbn);
            pstmt.setString(2, publisher.getCode());
            pstmt.setString(3, title);
            pstmt.setString(4, subtitle);
            pstmt.setDate(5, (java.sql.Date) date);
            pstmt.setString(6, picture);
            pstmt.setString(7, summary);
            pstmt.setString(8, idiom);
            pstmt.setFloat(9, price);
            pstmt.setFloat(10, tax.getRate());
            pstmt.setInt(11, quantity);
            pstmt.setString(12, pages);
            pstmt.setString(13, print);
            pstmt.setInt(14, weight);

            int result = pstmt.executeUpdate();
            System.out.println("result:" + result);
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }

        co.closeConnectionDatabase();

    }

    public Vector getStatusList() {
        Vector<BookStatus> statusList = new Vector<BookStatus>();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_bookStatus WHERE book_isbn LIKE '" + isbn + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                statusList.add(new BookStatus(rs.getString("book_isbn"),
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

    public void insertBookStatus(Status status) {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_bookStatus VALUES ("
                    + "?,?,?)";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, status.getNumber());
            pstmt.setString(2, isbn);
            pstmt.setDate(3, (java.sql.Date)getDate());

            int result = pstmt.executeUpdate();
            System.out.println("result:" + result);
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }

        co.closeConnectionDatabase();
    }
    
    
    
}
