package Server.Users;

import Server.Server;

import java.util.ArrayList;
import java.util.Map;

import static Server.Server.*;
import static Server.Server.userStatuses;
import static Server.Server.userTypes;
import static Server.Utils.logWrite.prt;
import static Server.Utils.utils.debPrt;

abstract public class User {


    public void deb(String str){
        if(DebMode){
            System.out.println( "[debMode] " + this.fullInfo() + ": " + str);
        }
    }

    String id;
    String Name = "noname";
    String Type = "undef";
    String Status = userStatuses[0];
    String ConnectonType;
    transient User Interlocutor;
    ArrayList<String> clientMassages = new ArrayList<>();

    public User(String id, String ConnectonType){
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

    public String getId() {
        return id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public void setConnectonType(String connectonType){
        this.ConnectonType = connectonType;
    }

    public String getConnectonType() {
        return ConnectonType;
    }

    public String fullInfo() {
        return String.valueOf(id) + " " + Type + " " + Name + " " + Status;
    }

    public User getInterlocutor() {
        return Interlocutor;
    }

    public void setInterlocutor(User user){
        this.Interlocutor = user;
    }

    void clientTimeLog(String str) {
        prt(str);
    }

    public void SendMsgToInterloc (String msg){
        this.Interlocutor.SendMsgToSelf(msg, this.getName());
    }

    void connectionWith(User interlocutor) {
        deb("connectionWith " + interlocutor.fullInfo());

        //connect
        this.setInterlocutor(interlocutor);
        interlocutor.setInterlocutor(this);

        //change status
        this.setStatus(userStatuses[2]);
        Interlocutor.setStatus(userStatuses[2]);

        //welcome msg
        this.SendMsgToSelf(Interlocutor.getName() + " found, you in chat!", "Server");
        this.SendMsgToInterloc((this.getName() + " found, you in chat!"));

        clientTimeLog(this.getName() + " in chat with " + Interlocutor.getName());
        //send accumulated msg
        if (Interlocutor.getType().equals(userTypes[1])) {
//        if (true) {
            for (String massage : this.clientMassages) {
                this.SendMsgToInterloc(massage);
                clientTimeLog(Interlocutor.getName() + " to " + this.getName() + " " + massage);
            }
            this.clientMassages.clear();
        } else {
            for (String massage : Interlocutor.clientMassages) {
                this.SendMsgToSelf(massage, Interlocutor.getName());
                clientTimeLog(this.getName() + " to " + Interlocutor.getName() + " " + massage);
            }
            Interlocutor.clientMassages.clear();
        }
    }

    public void setStatus(String status) {
        deb("setStatus " + status);

        if (this.Type.equals(userTypes[1]) && status.equals(userStatuses[1])){
            this.Status = userStatuses[3];
        }else {
            this.Status = status;
        }
        clientTimeLog(this.getName() + " changed status to " + this.getStatus());
        if (this.Type.equals(userTypes[1]) && this.Status.equals(userStatuses[3])) {
            this.lookingFor(userTypes[0]);
        }
    }

    synchronized void lookingFor(String Type) {
        deb("lookingFor " + Type);

        for (Map.Entry<String, User> item : getClientArr().entrySet()) {
            if (item.getValue().getType().equals(Type) &&
                    item.getValue().getStatus().equals(userStatuses[3])) {
                this.connectionWith(item.getValue());
                break;
            }
        }
    }

    abstract void sendUserList(User user);

    void disconnectingInterlocutors() {
        deb("disconnectingInterlocutors");

        sendUserList(this);
        sendUserList(this.Interlocutor);

        if (this.Type.equals(userTypes[0])) {
            this.setStatus(userStatuses[1]);
            if (Interlocutor != null) {
                Interlocutor.setStatus(userStatuses[3]);
            }
        } else {
            this.setStatus(userStatuses[3]);
            if (Interlocutor != null) {
                Interlocutor.setStatus(userStatuses[1]);
            }
        }
    }

    void usedDisconnect(String mode) {
        deb("usedDisconnect " + mode);

        if (mode.equals("exit")) {
            clientTimeLog(this.getName() + " " + mode);

        }
        if (mode.equals("disconnected")) {
            clientTimeLog(this.getName() + " " + mode);

        }
//        getClientArr().remove(this.getId());
        removeUser(this.getId());
    }

    public void runMethod(String received, String message){
        deb("runMethod " + message);

        if (this.checkingForCommands(received)) return;

        //show user message
        if ((this.getType().equals(userTypes[0]) ||
            this.getType().equals(userTypes[1]) && this.getStatus().equals(userStatuses[2])) &&
            this.getConnectonType().equals("web")){
            debPrt(this.getId() + " show user message");
                this.SendMsgToSelf(message, this.getName());
        }

        //client change status on waiting
        if (this.Type.equals(userTypes[0]) && this.Status.equals(userStatuses[1])) {
            debPrt(this.getId() + " client change status on waiting");
            this.Status = userStatuses[3];
            this.lookingFor(userTypes[1]);
            if (this.Status.equals(userStatuses[3])) {
                this.clientMassages.add(message);
                return;
            }
        }

        //write message of waiting client
        if (this.Type.equals(userTypes[0]) && this.Status.equals(userStatuses[3])) {
            debPrt(this.getId() + " write message of waiting client");
            this.lookingFor(userTypes[1]);
            if (this.Status.equals(userStatuses[3])) {
                this.clientMassages.add(message);
                return;
            }
        }

        //conversation
        if (Status.equals(userStatuses[2])) {
            debPrt(this.getId() + " conversation");
            this.SendMsgToInterloc(message);
            clientTimeLog(this.getName() + " to " + Interlocutor.getName() + " " + message);
        }
    }

    abstract public void SendMsgToSelf(String msg, String from);

    abstract public boolean registerUser(String str, int userType);

    abstract boolean checkingForCommands(String received);
}
