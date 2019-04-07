package Server.Users;

import Server.Server;
import io.javalin.websocket.WsSession;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static Server.Server.userStatuses;
import static Server.Server.userTypes;
import static j2html.TagCreator.*;

public class UserWS extends User{

    WsSession session;

    public UserWS(int id, String ConnectonType, WsSession session) {
        super(id, ConnectonType);
        this.session = session;
        setStatus(userStatuses[0]);
    }

    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }

    @Override
    public void SendMsgToSelf(String msg) {
        Map map = new HashMap<Integer, String>();
        map.put(0 , this.getName());
        map.put(1 , Interlocutor.getName());
        session.send(
                new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(Interlocutor.getName(), msg))
                        .put("userlist", map.values()).toString()
        );
    }

    @Override
    public boolean registerUser(String str, int u) {
        String infAbtUser[] = str.split(" ");
        if (!infAbtUser[0].equals("register")) return false;
        this.Name = infAbtUser[1];
        this.Type = userTypes[Integer.parseInt(infAbtUser[2])];
        this.setStatus(userStatuses[1]);
        return false;
    }
}
