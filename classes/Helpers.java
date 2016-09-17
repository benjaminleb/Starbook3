/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author cdi315
 */
public class Helpers {

    public static String convertDateToString(Date indate) {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateString = sdfr.format(indate);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return dateString;
    }
    
    public static float troncateTwoDecs(float f){         
        f = ((float) ((int) (f * 100))) / 100;
        return f;    
    }
    
    public static float troncateThreeDecs(float f){         
        f = ((float) ((int) (f * 1000))) / 1000;
        return f;    
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        Date date = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = df.parse(dateString);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return date ;
    }
    
  
    public static java.sql.Date convertUtiltoSQLDate (java.util.Date inDate) {
        java.sql.Date SQLDate = new java.sql.Date(inDate.getTime());
        return SQLDate;
    }
    
    public static java.util.Date convertSQLtoUtilDate (java.sql.Date inDate) {
        java.util.Date utilDate = new java.util.Date(inDate.getTime());
        return utilDate;
    }
    
    public static java.sql.Date parseDateFromTF (String rawDateTF) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date jDate = (Date) formatter.parse(rawDateTF);
        java.sql.Date sDate = new java.sql.Date(jDate.getTime());
        return sDate;
    }
    
    
    public static java.util.Date convertDateTimeToUDate (LocalDateTime indate) {
        LocalDateTime a = LocalDateTime.now();  
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());  
        return out;
    }
  
}
