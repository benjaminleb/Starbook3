
package classes;

/*
ben
 */
public class SubKeyword {
    //p
    private int id;
    private String name;
    private Keyword keyword;
    
    //c
    public SubKeyword(){
        
    }
    
    public SubKeyword(int id, String name, Keyword keyword){
        this.id = id;
        this.name = name;
        this.keyword = keyword;
    }
    
    //g&s

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Keyword getKeyword(){
        return keyword;
    }
    public void setKeyword(Keyword keyword){
        this.keyword = keyword;
    }
    //m
}
