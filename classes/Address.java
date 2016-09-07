
package classes;

/*
Gab
   

*/
public class Address {
    
    //p
    
    private String street; 
    private String other;
    private String zipcode;
    private String city; 
    private String country; 
    
    // constructeur par défaut

    public Address() {
    }
    
    
    // constructeur avec les champs obligatoires

    public Address(String zipcode, String city, String country) {
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }
    
    // surcharge du constructeur avec l'ajout des champs non obligatoires

    
    public Address(String street, String other, String zipcode, String city, String country) {
        this.street = street;
        this.other = other;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }
    
    //g&s

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    

    
    
    
    
}
