package Server.Utils;

import Server.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class utils {
    public static void timeLog (String str){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String out = "[" + dateFormat.format(date) + "] " + str;
        System.out.println(out);
        try
        {
            FileWriter writerIn = new FileWriter("Log.txt", true);
            writerIn.append(out + '\n');
            writerIn.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void creLog(){
        try{
            FileWriter writer = new FileWriter("Log.txt", false);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
