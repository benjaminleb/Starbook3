package classes;

/*
 Gab 
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;  // ATTENTION A GERER LA DATE ET AJUSTER LE TYPE DATE 
import java.util.Vector;
// OU DATETIME

public class Order {

    //p
    private int id;
    private Customer customer;
    private Date date;
    private String ipAddress;
    private String comments;

    // constructeur par défaut
    public Order() {
    }

    // constructeur avec les champs obligatoires
    public Order(int id, Customer customer, Date date, String ipAddress) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.ipAddress = ipAddress;
    }

    // surcharge du constructeur avec l'ajout des champs non obligatoires
    public Order(int id, Customer customer, Date date, String ipAddress, String comments) {
        this(id, customer, date, ipAddress);
        this.comments = comments;
    }

    //g&s

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    //m
    @Override
    public String toString(){
      //limite les traces de bronzage
        String info = "Ref "+id+" "+Helpers.convertDateToString(date);
        return info;
    }
    

    public Vector getStatusList(){
        //génère un Vector de l'historique des status de la commande
        //      -> le dernier est le plus récent
        Vector<ItemStatus> statusList = new Vector<ItemStatus>();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_orderStatus WHERE order_id LIKE '" + id + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                statusList.add(new ItemStatus(rs.getInt("order_id"),
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
    
    public Vector getOrderLines(){
        //génère un Vector<OrderLines> regroupant toutes les order lines de la commande
        Vector lines = new Vector();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_orderLine WHERE order_id LIKE '" + id + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                lines.add(new OrderLine(rs.getInt("orderLine_id"),
                        rs.getString("book_isbn"),
                        rs.getInt("order_itemQty"),
                        rs.getFloat("order_unitPrice"),
                        rs.getFloat("order_taxRate"),
                        rs.getFloat("order_discountRate")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return lines;
        }

        co.closeConnectionDatabase();
        return lines;
    }
    
    public float calculatePrice(){
        //calcule le prix global TTC de la commande
        Vector<OrderLine> lines = getOrderLines();
        float globalPrice = 0f;
        for (OrderLine line : lines) {
            globalPrice += line.calculateLinePrice();
        }
        globalPrice = ((float) ((int) (globalPrice * 100))) / 100;
        return globalPrice;                
    }
    
    public Address getAddresses(boolean type) {
        //type true -> billing address
        //type false -> delivery address
        Address a = new Address();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String idAddress = "";
        String addressType;
        if (type) {
            addressType = "orderBill";
        } else {
            addressType = "orderDelivery";
        }

        String query = "SELECT address_id FROM sb_" + addressType + " WHERE order_id LIKE '" + getId() + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                idAddress = rs.getString("address_id");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());

        }

        query = "SELECT * FROM sb_address WHERE address_id LIKE '" + idAddress + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
               
                a = new Address(rs.getInt("address_id"),
                        rs.getString("address_street"),
                        rs.getString("address_other"),
                        rs.getString("address_zipcode"),
                        rs.getString("address_city"),
                        rs.getString("address_country"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());

        }
        co.closeConnectionDatabase();
        return a;
    }

}
