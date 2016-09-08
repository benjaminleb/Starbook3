package classes;

/*
 Gab
 */
import java.util.*; // ATTENTION A GERER LA DATE ET AJUSTER LE TYPE DATE 
// OU DATETIME

public class Customer {

        //p
    private String surname;
    private String firstname;
    private String pwd;
    private String mail;
    private String cell;
    private String landline;
    private Date dob;

        // constructeur par défaut
    public Customer() {
    }

        // constructeur  avec les champs obligatoires
    public Customer(String surname, String firstname, String pwd, String mail, String cell, Date dob) {
        this.surname = surname;
        this.firstname = firstname;
        this.pwd = pwd;
        this.mail = mail;
        this.cell = cell;
        this.dob = dob;
    }

        // surcharge du constructeur avec l'ajout des champs non obligatoires
        // ajout de landline
    public Customer(String surname, String firstname, String pwd, String mail, String cell, String landline, Date dob) {
        this.surname = surname;
        this.firstname = firstname;
        this.pwd = pwd;
        this.mail = mail;
        this.cell = cell;
        this.landline = landline;
        this.dob = dob;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
       //AJOUTER CONTRAINTE DOB > 1900 
        this.dob = dob;
    }


}
