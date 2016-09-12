
package classes;

import java.util.*;

/*
ben
 */

//crée des objets historiques de status
//fonctionne avec tous les types de status sauf avec les books (-> pas d'id int mais isbn String)
public class ItemStatus extends ItemStatusParent {
    //p
    private int idItem;
    private Date statusDate;
    
    //c
    public ItemStatus(){
        
    }
    
    public ItemStatus(int idItem, int statusNumber){
        super(statusNumber);
        this.idItem = idItem;
    }
    
    public ItemStatus(int idItem, int statusNumber, Date statusDate){
        this(idItem, statusNumber);
        this.statusDate = statusDate;
        
    }
    //g&s
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
    
    //m
    
}
