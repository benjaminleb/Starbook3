package classes;

/*
 Gab 
 */
import java.util.Date;  // ATTENTION A GERER LA DATE ET AJUSTER LE TYPE DATE 
// OU DATETIME

public class Order {

    //p
    private Customer customer;
    private Date date;
    private String ipAddress;
    private String comments;
    private Status status;

    // constructeur par défaut
    public Order() {
    }

    // constructeur avec les champs obligatoires
    public Order(Customer customer, Date date, String ipAddress, Status status) {
        this.customer = customer;
        this.date = date;
        this.ipAddress = ipAddress;
        this.status = status;
    }

    // surcharge du constructeur avec l'ajout des champs non obligatoires
    public Order(Customer customer, Date date, String ipAddress, String comments, Status status) {
        this.customer = customer;
        this.date = date;
        this.ipAddress = ipAddress;
        this.comments = comments;
        this.status = status;
    }

    //g&s
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if ((status.getNumber() > 100) && (status.getNumber() < 200)) {
            this.status = status;
        }
    }

}
