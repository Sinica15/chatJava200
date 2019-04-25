package Server.Servers;

import Server.Users.UserS;
import Server.Utils.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static Server.Server.getI;
import static Server.Server.addUser;
import static Server.Utils.utils.timeLog;

public class ServerCons extends Thread {
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(1234);
            Socket s;
            utils.timeLog("Console server started");
            // running infinite loop for getting
            // client request


            while (true){

                // Accept the incoming request
                s = ss.accept();
                timeLog("New user request received : " + s);
                String i = String.valueOf(getI());
                UserS mtch = new UserS(i, "Socket", s);
                // Create a new Thread with this object.
                Thread t = new Thread(mtch);
                addUser(i, mtch);
//                timeLog("Adding this client to active client list № : " + clientArr.size());
//                timeLog("Active clients ids list: " + currClientIds());
                // start the thread.
                t.start();

            }
        }catch (IOException e){
            System.out.println(e);
        }

    }
}
