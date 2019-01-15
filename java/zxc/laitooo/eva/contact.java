package zxc.laitooo.eva;

/**
 * Created by Zizo on 6/30/2018.
 */
public class contact {

    private String name;
    private String phone;


    public contact(String n,String p){
        this.name = n;
        this.phone = p;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
