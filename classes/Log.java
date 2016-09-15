/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author cdi314
 */
public class Log {

    public static Boolean checkPassword(String password, String mail) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String pwd2 ="";
        String pwd = "";
        String query = "SELECT sb_employee.employee_pwd FROM sb_employee WHERE sb_employee.employee_mail = '" + mail+"'";
        try {

            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                pwd = rs.getString("employee_pwd");
            }
            rs.close();
            stmt.close();
            
            query = "SELECT HASHBYTES('MD5','" + password + "') AS pwd";
            Statement stmt2 = co.getConnexion().createStatement();
            ResultSet rs2 = stmt2.executeQuery(query);
            while (rs2.next()) {
                pwd2 = rs2.getString("pwd");
            }
            rs2.close();
            stmt2.close();
        } catch (SQLException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pwd.equals(pwd2);
    }

    public static Boolean checkEmail(String emailInput) {
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        Boolean b = false;
        String query = "SELECT * FROM sb_employee WHERE sb_employee.employee_mail = '" + emailInput+"'";
        try {

            Statement stmt = co.getConnexion().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                JOptionPane.showInputDialog(null, "Email erroné !", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                b = true;
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }    
        return b;
    }
    
    public static String convertPassword(String pwd){
        ConnectSQLS co = new ConnectSQLS();
        co.connectDatabase();
        String pwd2 = "";
        try {
            String query = "SELECT HASHBYTES('MD5','" + pwd + "') AS pwd";
            Statement stmt2 = co.getConnexion().createStatement();
            ResultSet rs2 = stmt2.executeQuery(query);
            while (rs2.next()) {
                pwd2 += rs2.getString("pwd");
            }
            rs2.close();
            stmt2.close();
        } catch (SQLException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pwd2;
    }
}
