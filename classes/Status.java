
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
    
    // Récupérer le nom d'un statut en ayant son numéro (=status number)
    public String getStatusName (int status_number) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String status_name ="";
        try {
            String query = "SELECT sb_status.status_name FROM status WHERE sb_status.status_id = " + status_number;
            PreparedStatement stmt = co.getConnexion().prepareStatement(query);
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                status_name += rs.getString("status_name");
            }
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Oops : SQL Connexion : " + ex.getMessage());
        }
        return status_name;
    }
    
    
    @Override
    public String toString() {
        return name ;
    }
}
