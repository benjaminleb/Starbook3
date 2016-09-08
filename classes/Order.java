package classes;

/*
 Gab 
 */
import java.util.Date;  // ATTENTION A GERER LA DATE ET AJUSTER LE TYPE DATE 
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

    

}
