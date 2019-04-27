package Server.Utils;

import Server.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static Server.Server.DebMode;

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
        catch(IOException e){
            e.printStackTrace();
            timeLog(e.toString());
        }
    }

    public static void creLog(){
        try{
            FileWriter writer = new FileWriter("Log.txt", false);
            writer.flush();
        }
        catch(IOException e){
            e.printStackTrace();
            timeLog(e.toString());
        }
    }

    public static void debPrt (String str){
        if (DebMode){
            System.out.println( "[DebMode] " + str);
        }

    }

    public static HashMap<String, String> JSONtoHashMapStrStr(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        HashMap out = new HashMap<String, String>(gson.fromJson(json, type));
        return out;
    }
}
