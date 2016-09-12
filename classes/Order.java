package classes;

/*
 Gab 
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public Vector getStatusList(){
        Vector statusList = new Vector();
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

}
