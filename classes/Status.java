
package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
ben
*/

public class Status {
    //p
    private int number;
    private String name;
    
    //c
    public Status(){
        
    }
    
    public Status(int number, String name){
        this.number = number;
        this.name = name;        
    }
    
    //g&s
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static int statusBookIndexCombo (String status_name) {
        int index = -1;
        if (status_name.equalsIgnoreCase("En stock")) {
            index = 0;
        }
        else if (status_name.equalsIgnoreCase("Rupture de stock")) {
            index = 1;
        }
        else if (status_name.equalsIgnoreCase("Discontinué")) {
            index = 2;
        }
        else if (status_name.equalsIgnoreCase("Précommande")) {
            index = 3;
        }
        return index;
    }
    
    
    public static int statusPublisherIndexCombo (String status_name) {
        int index = -1;
        if (status_name.equalsIgnoreCase("Editeur actif")) {
            index = 0;
        }
        else if (status_name.equalsIgnoreCase("Editeur inactif")) {
            index = 1;
        }
        else if (status_name.equalsIgnoreCase("Litige")) {
            index = 2;
        }
        return index;
    }
    
    
     public static int statusEmployeeIndexCombo (String status_name) {
        int index = -1;
        if (status_name.equalsIgnoreCase("Utilisateur simple")) {
            index = 0;
        }
        else if (status_name.equalsIgnoreCase("Utilisateur +")) {
            index = 1;
        }
        else if (status_name.equalsIgnoreCase("Administrateur")) {
            index = 2;
        }
        else if (status_name.equalsIgnoreCase("Master")) {
            index = 3;
        }
        return index;
    }
    
    @Override
    public String toString() {
        return name ;
    }
}
