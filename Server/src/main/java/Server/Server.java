package Server;

import Server.Servers.ServerCons;
import Server.Servers.ServerWeb;
import Server.Users.User;
import Server.Utils.ServerCommands;
import Server.Utils.utils;

import java.util.HashMap;

public class Server {

    public static final String[] userStatuses = {"not logined", "logined", "in conversation", "waiting"};
    public static final String[] userTypes = {"client", "agent"};
    public static final String[] connectionTypes = {"Socket", "WebSocket"};

    private static HashMap<String, User> clientArr = new HashMap<String, User>();
    protected static int i = 0;

    public static HashMap<String, User> getClientArr(){
        return clientArr;
    }

    synchronized public static void addUser(String i, User user){
        clientArr.put(i, user);
    }

    public static void removeUser(String i){
        clientArr.remove(i);
    }

    synchronized public static int getI(){
        i++;
        return i;
    }

    public static void main(String[] args) {

        utils.creLog();
        utils.timeLog("Server powered on!");

        ServerWeb SWeb = new ServerWeb();
        SWeb.start();

        //ServerCons SCons = new ServerCons();
        //SCons.start();

        ServerCommands commands = new ServerCommands();
        commands.start();

    }
}
