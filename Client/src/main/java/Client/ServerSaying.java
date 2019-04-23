package Client;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerSaying extends Thread {
    public DataInputStream in;
    ServerSaying(DataInputStream in) { this.in = in; }
    String line;
    //            @Override
    public void run() { // Этот метод будет вызван при старте потока
        System.out.println("client running");
        try{
            while (true){
                line = in.readUTF(); // ждем пока сервер отошлет строку текста.
                System.out.println(line);
            }
        }catch (IOException e){
            System.out.println("disabled by Server");
//            e.printStackTrace();
        }

    }
}
