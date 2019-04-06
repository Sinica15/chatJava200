package Server.Utils;

import Server.Server;
import Server.Users.User;

import java.util.Map;
import java.util.Scanner;

public class ServerCommands extends Thread  {

    Scanner in = new Scanner(System.in);

    @Override
    public void run() {
        utils.timeLog("Server commands started");

        while (true){
//            System.out.println("waiting commands");
            String line = in.nextLine();
            if (line.equals("list")){
                System.out.println("==== clients list ====");

                for(Map.Entry<Integer, User> item : Server.getClientArr().entrySet()){
                    System.out.print(item.getValue().fullInfo());
                    System.out.println();
//                    System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue().getFullName());
                }

                System.out.println("======================");

            }
            if (line.indexOf("kick ") >= 0){
//                System.out.println("kick");
                StringBuffer str = new StringBuffer(line.trim());
                int id = Integer.parseInt(new String(str.delete(0, 4)).trim());
                System.out.println(id);

                Server.getClientArr().get(id);

                if (Server.getClientArr().get(id) != null){
                    System.out.println("disconnect " + Server.getClientArr().get(id).fullInfo());
                }

//                for (int i = 0; i < Server.clientArr.length; i++){
//                    if (Server.clientArr[i].getId() == id){
//                        System.out.println("disconnect " + Server.clientArr[i].getFullName());
//                    }
//                }
            }
//            if (line.equals("stop")){
//                System.out.println("try stopping");
//                for (int i = 0; i < Server.clientArr.length; i++){
//                    Server.clientArr[i].disable();
//                }
//                Server.servDisable();
//                break;
//            }
        }
    }
}
