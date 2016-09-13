
package classes;

/*
ben
 */
public class Genre {
    //p
    private String name;
    //c
    public Genre(){
        
    }
    public Genre(String name){
        this.name = name;
    }
    //g&s
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    //m

    @Override
    public String toString() {
        return name;
    }
    
    
}
