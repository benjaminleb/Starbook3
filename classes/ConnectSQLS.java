
package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 Seb
 */

public class ConnectSQLS {
    Connection connexion;

    public ConnectSQLS() {
    }

    
    public Connection getConnexion() {
        return connexion;
    }
    
 
    public void connectDatabase (){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Err: ClassNotFound: "+ex.getMessage());
            return;
        }
        try {
            String url = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=starbook;user=sa;password=sa";
            connexion = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            System.err.println("Err : SQLConnexion: " + ex.getMessage());
        }
    }
    
    public void closeConnectionDatabase() {
        try {
            connexion.close();
            System.out.println("Connection closed.");
        } catch (SQLException ex) {
            System.err.println("Err: SQLException: " + ex.getMessage());
        }
        
    }
}
