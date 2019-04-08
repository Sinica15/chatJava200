package Server.Utils;

import org.junit.Test;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class utilsTest {

    static String read(String fileName){
        try{
            FileInputStream fstream = new FileInputStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine = br.readLine();
            return strLine;
        }catch (IOException e){
            System.out.println("Ошибка");
        }
        return "";
    }

    @Test
    public void timeLog() {
        Server.Utils.utils.creLog();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String out = "[" + dateFormat.format(date) + "] " + "test log";

        System.out.println(out);

        Server.Utils.utils.timeLog("test log");
        String strLine = read("Log.txt");


        assertEquals(true, out.equals(strLine));
    }

    @Test
    public void creLog() {

        Server.Utils.utils.creLog();
        File file = new File("Log.txt");
        assertEquals(true, file.exists());

    }
}