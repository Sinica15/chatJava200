package ClientWS;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ClientWS.ClientSay.formingOut;

@javax.websocket.ClientEndpoint
public class ClientEndpoint {

    @OnOpen
    public void onOpen(Session session) {

        System.out.println("Connected to endpoint: " + session.getBasicRemote());

        Gson gson = new Gson();
        String outMsg = gson.toJson(formingOut("service", "setConnectionType", "console"));
        try {
            session.getBasicRemote().sendText(outMsg);
        } catch (IOException ex) {
            Logger.getLogger(ClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }

        ClientSay ClSay = new ClientSay(session);
        ClSay.start();

    }

    @OnMessage
    public void processMessage(String message) {
//        System.out.println("incoming msg " + message);
        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(message);
        JsonObject jsonObject = jsonTree.getAsJsonObject();
        JsonElement msg = jsonObject.get("message");

        System.out.println(msg.getAsString());
//        Client.messageLatch.countDown();
    }

    @OnClose
    public void onClose(){
        System.out.println("close?");
    }

    @OnError
    public void processError(Throwable e) {
        e.printStackTrace();
    }
}