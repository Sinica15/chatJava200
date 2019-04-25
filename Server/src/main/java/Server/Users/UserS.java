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

    public UserS(String id, String ConnectionType, Socket socket){
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

    void disable(){
        isActive = false;
    }

    void setUserName(String str) {
        String words[] = str.replaceAll("[\\s]{2,}", " ").split(" ");
        if (words.length > 2) {
            this.Name = words[2];
        }
    }

    @Override
    public void SendMsgToSelf(String msg, String from) {
        try {
            this.dOutS.writeUTF(from + ": "+ msg);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("send msg err");
        }
    }

    @Override
    public boolean registerUser(String str, int userType) {
        this.Type = userTypes[userType];
        setUserName(str);
        this.SendMsgToSelf("you are registered as " + this.Type, "Server");
        clientTimeLog(this.getName() + " registered");
        if (userType == 0) {
            this.setStatus(userStatuses[1]);
        } else {
            this.setStatus(userStatuses[3]);
        }
        return true;
    }

    @Override
    boolean checkingForCommands(String received) {
//        System.out.println("checkingForCommands")

        //check slash
        if (received.isEmpty()) return true;
        if (received.trim().charAt(0) != '/') {
            return false;
        }

        //check register
        if (received.trim().indexOf("register") == 1) {
            if (Status.equals(userStatuses[0])) {
                if (received.trim().indexOf(userTypes[0]) <= 14 && received.trim().indexOf(userTypes[0]) > 5) {
                    return registerUser(received, 0);
                }
                if (received.trim().indexOf(userTypes[1]) <= 14 && received.trim().indexOf(userTypes[1]) > 5) {
                    return registerUser(received, 1);
                }
            } else {
                this.SendMsgToSelf("you are already registered!", "Server");
                return true;
            }
        }

        //check leave
        if (received.trim().indexOf("leave") == 1 && this.Status.equals(userStatuses[2])) {
            disconnectingInterlocutors();
            this.SendMsgToSelf("you leave chat with " + Interlocutor.getName(), "Server");
            Interlocutor.SendMsgToSelf( this.getName() + " leave chat with you", "Server");
            clientTimeLog(this.getName() + " leave chat with " + Interlocutor.getName());
            return true;
        }

        //check exit
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

    public void run() {
        String received;

        while (isActive){

            try {

                received = dInpS.readUTF();

                runMethod(received);

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
