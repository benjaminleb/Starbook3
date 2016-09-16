package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 ben
 */
public class Publisher {

    //p
    private String code;
    private String name;

    //c
    public Publisher() {
    }

    public Publisher(String code, String name) {
        this.code = code;
        this.name = name;
    }

    //g&s
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //m

    @Override
    public String toString() {
        return  code +" "+  name;
    }
    
    
    
    public void insertPublisher() {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_publisher VALUES ("
                    + "?,?)";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setString(1, code);
            pstmt.setString(2, name);
            pstmt.close();

       } catch (SQLException ex) {
            System.err.println("error: sql exception: " +ex.getMessage());
        }

        co.closeConnectionDatabase();

    }
    public Vector getStatusList(){
        Vector<BookStatus> statusList = new Vector<BookStatus>();
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String query = "SELECT * FROM sb_publisherStatus WHERE publisher_isbn LIKE '" + code + "'";
        try {
            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                statusList.add(new BookStatus(rs.getString("publisher_isbn"),
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




    public void insertPublisherStatus(Status status) {

        ConnectSQLS co = new ConnectSQLS();

        co.connectDatabase();

        try {
            String query = "INSERT INTO sb_publisherStatus VALUES ("
                    + "?,?,GETDATE())";

            PreparedStatement pstmt = co.getConnexion().prepareStatement(query);
            pstmt.setInt(1, status.getNumber());
            pstmt.setString(2, code);
            
           
            
            pstmt.close();

        } catch (SQLException ex) {
            System.err.println("error: sql exception: " + ex.getMessage());
        }

        co.closeConnectionDatabase();
    }
}
