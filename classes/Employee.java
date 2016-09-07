package classes;

/*
 Gab 
 */
public class Employee {

        //p
    private String surname;
    private String firstname;
    private String pwd;
    private String mail;
    private String phone;
    private Status status;

        // constructeur par défaut
    public Employee() {
    }

        // constructeur avec les champs obligatoires
    public Employee(String surname, String firstname, String pwd, String mail, Status status) {
        this.surname = surname;
        this.firstname = firstname;
        this.pwd = pwd;
        this.mail = mail;
        this.status = status;
    }

        // surcharge du constructeur avec l'ajout des champs non obligatoires
    public Employee(String surname, String firstname, String pwd, String mail, String phone, Status status) {
        this.surname = surname;
        this.firstname = firstname;
        this.pwd = pwd;
        this.mail = mail;
        this.phone = phone;
        this.status = status;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if ((status.getNumber() > 500) && (status.getNumber() < 600)) {
            this.status = status;
        }
    }

}
