package ClientWS;

import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSay extends Thread {

    public static final String[] userTypes = {"client", "agent"};
    String line = null;
    Session session;

    ClientSay(Session session){
        this.session = session;
    }

    @Override
    public void run() {
        System.out.println("client running");

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        try{
            while (true){
                line = keyboard.readLine();

                HashMap<String, String> msg = checkingForCommands(line);
                if (msg.get("isEmpty").equals("true")) continue;

                Gson gson = new Gson();
                String outMsg = gson.toJson(msg);

                System.out.println(outMsg);

                try {
                    session.getBasicRemote().sendText(outMsg);
                } catch (IOException ex) {
                    Logger.getLogger(ClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }

            }
        }catch (IOException e){
            System.out.println("disabled by Server");
            e.printStackTrace();
        }

    }

    HashMap<String, String> formingOut (String msgType, String action, String massage){
        HashMap<String, String> out = new HashMap<>();

        if (massage.isEmpty() && msgType.equals("massage")){
            out.put("isEmpty", "true");
        }else {
            out.put("isEmpty", "false");
        }

        out.put("msgType", msgType);
        out.put("action", action);
        out.put("massage", massage);

        if (action.equals("register")){
            if (massage.indexOf(userTypes[0]) > 0){
                out.put("massage", "0" + massage.substring(17));
            }

            if (massage.indexOf(userTypes[1]) > 0){
                out.put("massage", "1" + massage.substring(16));
            }

        }

        return out;
    }

    HashMap<String, String> checkingForCommands(String in) {
//        System.out.println("checkingForCommands")
        String received = in.trim().replaceAll("[\\s]{2,}", " ");

        //check slash
        if (received.isEmpty()) {
            return formingOut("massage","massage", "");
        }
        if (received.charAt(0) != '/') {
            return formingOut("massage", "massage", received);
        }

        //check register
        if (received.trim().indexOf("register") == 1) {
            if (received.trim().indexOf(userTypes[0]) <= 14 &&
                    received.indexOf(userTypes[0]) > 5) {
                return formingOut("service", "register", received);
            }
            if (received.trim().indexOf(userTypes[1]) <= 14 &&
                    received.indexOf(userTypes[1]) > 5) {
                return formingOut("service", "register", received);
            }
        }

        //check leave
        if (received.indexOf("leave") == 1) {
            return formingOut("service", "leave", "");
        }

        //check exit
        if (received.indexOf("exit") == 1) {
            return formingOut("service", "exit", "");
        }

        return formingOut("massage","massage", "");
    }

}
