package classes;

/*
 ben
 */
public class Tax {

    //p
    private int id;
    private String name;
    private float rate;

    //c   
    public Tax() {
    }

    public Tax(int id, String name, float rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    //g&s

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        if ((rate >= 0) && (rate <= 100)) {
            this.rate = rate;
            
            
        }
    }

}
