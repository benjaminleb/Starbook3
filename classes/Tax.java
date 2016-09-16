package classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import swing.JFMain;

/*
 ben
 */
public class Tax {

    //p
    private int id;
    private String name;
    private float rate;

    //c   
    public Tax() {
    }

    public Tax(int id, String name, float rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    //g&s

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        if ((rate >= 0) && (rate <= 100)) {
            this.rate = rate;
            
            
        }
    }

    public static int getIdFromRate(float rate) {
        int tax_id = -1;
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
      
        String query = "SELECT sb_tax.tax_id "
                + "FROM sb_tax "
                + "WHERE sb_tax.tax_rate = "+rate;
        
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                tax_id = rs.getInt("tax_id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JFMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        co.closeConnectionDatabase();        
        return tax_id;
    }
}
