
package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/*
ben
 */


public abstract class ItemStatusParent {
    //p
    private int statusNumber;
    //c
    public ItemStatusParent(){       
    }
    
    public ItemStatusParent(int statusNumber){
        this.statusNumber = statusNumber;
    }
    
   
    //g&s

    public int getStatusNumber() {
        return statusNumber;
    }

    public void setStatusNumber(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    //m
    @Override
    public String toString(){
        String statusName = "";
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_Status WHERE status_number LIKE '" + statusNumber + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
               statusName = rs.getString("status_name");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops:SQL:" + ex.getErrorCode() + ":" + ex.getMessage());
            return statusName;
        }

        co.closeConnectionDatabase();
        return statusName;
    }
}
