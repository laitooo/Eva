package zxc.laitooo.eva.messages;

/**
 * Created by Zizo on 6/27/2018.
 */
public class Message {

    private boolean isFromUser;
    private String name;


    public Message(boolean is, String n){
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
