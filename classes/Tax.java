package classes;

/*
 ben
 */
public class Tax {

    //p
    private String name;
    private float rate;

    //c   
    public Tax() {
    }

    public Tax(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    //g&s
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
