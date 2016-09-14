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
        if (inputtxt.matches("(([0-9Xx][- ]*){13}|([0-9Xx][- ]*){10})")
                && checkStringIsNotBlank(inputtxt)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkPublisherISBN(String inputtxt) {
        if (inputtxt.matches("(([0-9Xx][- ]*){5,7})")
                && checkStringIsNotBlank(inputtxt)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkDateFormat(String inputtxt) {
        if (inputtxt.matches("^(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d$")
                && checkStringIsNotBlank(inputtxt)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkNumbers(String inputtxt) {
        if (inputtxt.matches("^[0-9]{3}|([0-9]*\\.[0-9]+|[0-9]+)$")
                && checkStringIsNotBlank(inputtxt)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkMail(String inputtxt) {
        if (inputtxt.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                && checkStringIsNotBlank(inputtxt)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkPhone(String inputtxt) {
        if (inputtxt.matches("^(0|\\+33)[1-9]([-. ]?[0-9]{2}){4}$") && checkStringIsNotBlank(inputtxt)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkStringIsNotBlank(String inputtxt) {
        if (StringUtils.isNotBlank(inputtxt)) {
            return true;
        } else {
            return false;
        }
    }

}
