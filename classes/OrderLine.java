
package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/*
ben
 */
public class OrderLine {
    //p
    private int id;
    private String isbnBook;
    private int itemQty;
    private float unitPrice;
    private float taxRate;
    private float discountRate;
    
    //c
    public OrderLine() {
    }

    public OrderLine(int id, String isbnBook, int itemQty, float unitPrice, float taxRate, float discountRate) {
        this.id = id;
        this.isbnBook = isbnBook;
        this.itemQty = itemQty;
        this.unitPrice = unitPrice;
        this.taxRate = taxRate;
        this.discountRate = discountRate;
    }
    //g&s

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbnBook() {
        return isbnBook;
    }

    public void setIsbnBook(String isbnBook) {
        this.isbnBook = isbnBook;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(float taxRate) {
        this.taxRate = taxRate;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    
    //m
    
    public float taxUnitPrice(){
        float uPrice;
        uPrice = unitPrice+((unitPrice*taxRate)/100);
        //troncate to xx,xx
        uPrice = ((float) ((int) (uPrice * 100))) / 100;
        return uPrice;
    }
    
    public float calculateExclTax(){
        float lPrice;
        lPrice = itemQty * unitPrice;
        //troncate to xx,xx
        lPrice = ((float) ((int) (lPrice * 100))) / 100;
        return lPrice;
    }
    
    public float calculateInclTax(){
        float lPrice;
        lPrice = calculateExclTax()+((calculateExclTax()*taxRate)/100);
        //troncate to xx,xx
        lPrice = ((float) ((int) (lPrice * 100))) / 100;
        return lPrice;
    }
    
    public float calculateLinePrice(){
        float lPrice;
        lPrice = calculateInclTax() + (calculateInclTax() * discountRate);
        //troncate to xx,xx
        lPrice = ((float) ((int) (lPrice * 100))) / 100;
        return lPrice;
    }
    
   
    
    
    
    public Vector getStatusList(){
        Vector<ItemStatus> statusList = new Vector<ItemStatus>();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_orderLineStatus WHERE orderLine_id LIKE '" + id + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                statusList.add(new ItemStatus(rs.getInt("orderLine_id"),
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
    
    public String getBookName(){
        String info = "";
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_book WHERE book_isbn LIKE '" + isbnBook + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                info = rs.getString("book_title");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return info;
        }

        co.closeConnectionDatabase();
        return info;
    }

}
