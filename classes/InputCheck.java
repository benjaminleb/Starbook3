/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author cdi314
 */
public class InputCheck {

    public static Boolean checkBookISBN(String inputtxt) {
        return inputtxt.matches("(([0-9Xx][- ]*){13}|([0-9Xx][- ]*){10})")
                && checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkBookISBN_NotMandatory(String inputtxt) {
        return inputtxt.matches("(([0-9Xx][- ]*){13}|([0-9Xx][- ]*){10})")|| !checkStringIsNotBlank(inputtxt);
    }
    

    public static Boolean checkPublisherISBN(String inputtxt) {
        return inputtxt.matches("(([0-9Xx][- ]*){5,7})")
                && checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkPublisherISBN_NotMandatory(String inputtxt) {
        return inputtxt.matches("(([0-9Xx][- ]*){5,7})")|| !checkStringIsNotBlank(inputtxt);
    }

    public static Boolean checkDateFormat(String inputtxt) {
        return inputtxt.matches("^(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d$")
                && checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkDateFormat_NotMandatory(String inputtxt) {
        return inputtxt.matches("^(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d$")|| !checkStringIsNotBlank(inputtxt);
    }
    

    public static Boolean checkNumbers(String inputtxt) {
        return inputtxt.matches("^[0-9]{3}|([0-9]*\\.[0-9]+|[0-9]+)$")
                && checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkNumbers_NotMandatory(String inputtxt) {
        return inputtxt.matches("^[0-9]{3}|([0-9]*\\.[0-9]+|[0-9]+)$")|| !checkStringIsNotBlank(inputtxt);
    }

    public static Boolean checkMail(String inputtxt) {
        return inputtxt.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkMail_NotMandatory(String inputtxt) {
        return inputtxt.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")|| !checkStringIsNotBlank(inputtxt);
    }

    public static Boolean checkPhone(String inputtxt) {
        return inputtxt.matches("^(0|\\+33)[1-9]([-. ]?[0-9]{2}){4}$") && checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkPhone_NotMandatory(String inputtxt) {
        return inputtxt.matches("^(0|\\+33)[1-9]([-. ]?[0-9]{2}){4}$")|| !checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkAlphaChar(String inputtxt){
        return inputtxt.matches("^[ A-Za-z]+$") && checkStringIsNotBlank(inputtxt);
    }
    
    public static Boolean checkAlphaChar_NotMandatory(String inputtxt){
        return inputtxt.matches("^[ A-Za-z]+$")|| !checkStringIsNotBlank(inputtxt);
    }

    public static Boolean checkStringIsNotBlank(String inputtxt) {
        return StringUtils.isNotBlank(inputtxt);
    }

}
