
package classes;

import java.util.Date;

/*
ben
 */
public class BookStatus extends ItemStatusParent {
    //p
    private String isbn;
    private Date statusDate;
    
    //c
    public BookStatus(){
        
    }
    
    public BookStatus(String isbn, int statusNumber){
        super(statusNumber);
        this.isbn = isbn;
    }
    
    public BookStatus(String isbn, int statusNumber, Date statusDate){
        this(isbn, statusNumber);
        this.statusDate = statusDate;        
    }
    
    //g&s

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
    
    //m
}
