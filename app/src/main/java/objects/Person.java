package objects;

public class Person {
    private String pid;
    private String name;
    private String phoneNumber;

    public Person(String name, String phoneNumber) {
        //this.pid = pid;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Person() {

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
