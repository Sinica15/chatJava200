package Server.Users;

import Server.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import static Server.Server.userStatuses;
import static Server.Server.userTypes;

public class UserS extends User implements Runnable{

    boolean isActive;

    Socket socket;
    DataInputStream dInpS;
    DataOutputStream dOutS;

    public UserS(int id, String ConnectionType, Socket socket){
        super(id, ConnectionType);
        isActive = true;
        this.socket = socket;
        try {
            this.dInpS = new DataInputStream(socket.getInputStream());
            this.dOutS = new DataOutputStream(socket.getOutputStream());
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void SendMsgToSelf(String msg) {
        try {
            this.dOutS.writeUTF(msg);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("send msg err");
        }
    }

    void disable(){
        isActive = false;
    }

    void usedDisconnect(String mode) {
        if (mode.equals("exit")) {
            clientTimeLog(this.getName() + " " + mode);

        }
        if (mode.equals("disconnected")) {
            clientTimeLog(this.getName() + " " + mode);

        }
        Server.getClientArr().remove(this.getId());
    }

    @Override
    public void setStatus(String status) {
        this.Status = status;
        clientTimeLog(this.getName() + " changed status to " + this.getStatus());
        if (this.Type.equals(userTypes[1]) && status.equals(userStatuses[3])) {
            lookingFor(userTypes[0]);
        }
    }

    void setClientName(String str) {
        String words[] = str.replaceAll("[\\s]{2,}", " ").split(" ");
        if (words.length > 2) {
            this.Name = words[2];
        }
    }

    @Override
    public boolean registerUser(String str, int userType) {
        this.Type = userTypes[userType];
        setClientName(str);
        this.SendMsgToSelf("you are registered as " + this.Type);
        clientTimeLog(this.getName() + " registered");
        if (userType == 0) {
            this.setStatus(userStatuses[1]);
        } else {
            this.setStatus(userStatuses[3]);
        }
        return true;
    }

    void disconnectingInterlocutors() {
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

    boolean checkingForSlashCommads(String received) {
//        System.out.println("123");
        if (received.isEmpty()) return true;
        if (received.trim().charAt(0) != '/') {
            return false;
        }
        if (received.trim().indexOf("register") == 1) {
            if (Status.equals(userStatuses[0])) {
                if (received.trim().indexOf(userTypes[0]) <= 14 && received.trim().indexOf(userTypes[0]) > 5) {
                    return registerUser(received, 0);
                }
                if (received.trim().indexOf(userTypes[1]) <= 14 && received.trim().indexOf(userTypes[1]) > 5) {
                    return registerUser(received, 1);
                }
            } else {
                this.SendMsgToSelf("you are already registered!");
                return true;
            }
        }
        if (received.trim().indexOf("leave") == 1 && this.Status.equals(userStatuses[2])) {
            disconnectingInterlocutors();
            this.SendMsgToSelf("you leave chat with " + Interlocutor.getName());
            clientTimeLog(this.getName() + " leave chat with " + Interlocutor.getName());
            return true;
        }
        if (received.trim().indexOf("exit") == 1) {
            disconnectingInterlocutors();
            try {
                this.socket.close();
            } catch (Exception e) {
                System.out.println(e);
            }
            usedDisconnect("exit");
            disable();
            return true;
        }
        return false;
    }

    void connectionWith(User interlocutor) {
        //connect
        this.setInterlocutor(interlocutor);
        interlocutor.setInterlocutor(this);
        //change status
        this.setStatus(userStatuses[2]);
        Interlocutor.setStatus(userStatuses[2]);
        //welcome msg
        this.SendMsgToSelf(Interlocutor.getName() + " found, you in chat!");
        this.Interlocutor.SendMsgToSelf(this.getName() + " found, you in chat!");

        clientTimeLog(this.getName() + " in chat with " + Interlocutor.getName());

        if (Interlocutor.getType().equals(userTypes[1])) {
            for (String massage : this.clientMassages) {
                Interlocutor.SendMsgToSelf(this.getName() + ": " + massage);
                clientTimeLog(Interlocutor.getName() + " to " + this.getName() + " " + massage);
            }
            this.clientMassages.clear();
        } else {
            for (String massage : Interlocutor.clientMassages) {
                this.SendMsgToSelf(Interlocutor.getName() + ": " + massage);
                clientTimeLog(this.getName() + " to " + Interlocutor.getName() + " " + massage);

            }
            Interlocutor.clientMassages.clear();
        }
    }

    synchronized void lookingFor(String Type) {
        for (Map.Entry<Integer, User> item : Server.getClientArr().entrySet()) {
            if (item.getValue().getType().equals(Type) &&
                    item.getValue().getStatus().equals(userStatuses[3])) {
                connectionWith(item.getValue());
                break;
            }
        }
    }

    public void run() {
        String received;

        while (isActive){

            try {
                // receive the string
                received = dInpS.readUTF();
//                System.out.println(received);

                if (checkingForSlashCommads(received)) continue;

                if (this.Type.equals(userTypes[0]) && this.Status.equals(userStatuses[1])) {
//                    System.out.println("look");
                    this.Status = userStatuses[3];
                    lookingFor(userTypes[1]);
                    if (this.Status.equals(userStatuses[3])) {
                        this.clientMassages.add(received);
                        continue;
                    }
                }

                if (this.Type.equals(userTypes[0]) && this.Status.equals(userStatuses[3])) {
                    lookingFor(userTypes[1]);
                    if (this.Status.equals(userStatuses[3])) {
                        this.clientMassages.add(received);
                        continue;
                    }
                }

                if (Status.equals(userStatuses[2])) {
                    this.SendMsgToInterloc(received);
                    clientTimeLog(this.getName() + " to " + Interlocutor.getName() + " " + received);
                }

            } catch (IOException e) {
                disconnectingInterlocutors();
                usedDisconnect("disconnected");
//                e.printStackTrace();
                disable();
            }

        }

        try {
            // closing resources
            this.dOutS.close();
            this.dInpS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
