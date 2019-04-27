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

                for(Map.Entry<String, User> item : Server.getClientArr().entrySet()){
                    System.out.print(item.getValue().fullInfo());
                    System.out.println();
//                    System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue().getFullName());
                }

                System.out.println("======================");

            }

            //todo User kick

            //todo Server stoping
        }
    }
}
