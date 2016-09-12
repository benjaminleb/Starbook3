package classes;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

            int result = pstmt.executeUpdate();
            System.out.println("result:" + result);
            pstmt.close();

       } catch (SQLException ex) {
            System.err.println("error: sql exception: " +ex.getMessage());
        }

        co.closeConnectionDatabase();

    }

}
