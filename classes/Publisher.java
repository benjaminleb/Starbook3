package classes;

/*
 ben
 */
public class Publisher {

    //p

    private String code;
    private String name;
    private Status status;

    //c
    public Publisher() {
    }

    public Publisher(String code, String name, Status status) {
        this.code = code;
        this.name = name;
        this.status = status;
    }

    //g&s
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if ((status.getNumber() > 400) && (status.getNumber() < 500)) {
            this.status = status;
        }
    }

}
