package Server.Users;

import java.util.ArrayList;

import static Server.Server.userStatuses;

abstract public class User {
    int id;
    String Name = "noname";
    String Type = "undef";
    String Status = userStatuses[0];
    String ConnectonType;
    User Interlocutor;
    ArrayList<String> clientMassages = new ArrayList<>();

    public User(int id, String ConnectonType){
        this.id = id;
        this.ConnectonType = ConnectonType;
    }

    public String getName(){
        if (Name.equals("noname")){
            return Type + "_" + id;
        }
        return this.Name;
    }

    public String getStatus() {
        return Status;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return Type;
    }

    public String fullInfo() {
        return String.valueOf(id) + " " + Type + " " + Name + " " + Status;
    }

    public void setInterlocutor(User user){
        this.Interlocutor = user;
    }

    void clientTimeLog(String str) {
        Server.Utils.logWrite.prt(str);
    }

    public void SendMsgToInterloc (String msg){
        this.Interlocutor.SendMsgToSelf(this.getName() + ": " + msg);
    }

    abstract public void setStatus(String status);

    abstract public void SendMsgToSelf(String msg);

    abstract public boolean registerUser(String str, int userType);
}
