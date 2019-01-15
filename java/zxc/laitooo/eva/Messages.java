package zxc.laitooo.eva;

/**
 * Created by Zizo on 6/27/2018.
 */
public class Messages {

    private boolean isFromUser;
    private String name;


    public Messages(boolean is,String n){
        this.isFromUser = is;
        this.name = n;
    }

    public String getName() {
        return name;
    }

    public boolean getIsFromUser() {
        return isFromUser;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setFromUser(boolean phone) {
        this.isFromUser = phone;
    }

}
