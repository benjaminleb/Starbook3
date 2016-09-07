
package classes;

/*
ben
 */
public class SubKeyword {
    //p
    private String name;
    private Keyword keyword;
    //c
    public SubKeyword(){
        
    }
    public SubKeyword(String name, Keyword keyword){
        this.name = name;
        this.keyword = keyword;
    }
    //g&s
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
