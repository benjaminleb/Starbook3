
package classes;

import java.util.*; 

/*
ben
 */

public class Author {
    //p
    private String surname;
    private String firstname;
    private Date dob;
    private Date dod;
    
    //c
    public Author() {    
    }

    public Author(String surname) {
        this.surname = surname;
    }

    public Author(String surname, String firstname, Date dob, Date dod) {
        this.surname = surname;
        this.firstname = firstname;
        this.dob = dob;
        this.dod = dod;
    }
    

    //g&s
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDod() {
        return dod;
    }

    public void setDod(Date dod) {
        this.dod = dod;
    }
    
    //m

    
    
    
}
