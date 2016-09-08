/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cdi315
 */
public class Helpers {
    public static String convertDateToString(Date indate) {
       String dateString = null;
       SimpleDateFormat sdfr = new SimpleDateFormat("dd/MM/yyyy");
       try{
            dateString = sdfr.format(indate);
       }catch (Exception ex ){
            System.out.println(ex);
       }
       return dateString;
    }
}
