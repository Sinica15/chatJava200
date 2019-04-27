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
import static Server.Utils.utils.JSONtoHashMapStrStr;
import static Server.Utils.utils.debPrt;
import static j2html.TagCreator.*;

public class UserWS extends User{

    WsSession session;

    public UserWS(int id, String ConnectonType, WsSession session) {
        super(session.getId(), ConnectonType);
        this.session = session;
        setStatus(userStatuses[0]);
    }

    private String createHtmlMessageFromSender(String sender, String message) {
        String article_class = "interlocutor_article";
        if (sender.equals(this.getName())){
            sender = "me";
            article_class = "sender_article";
        }
        if (sender.equals("Server")){
            article_class = "server_article";
        }
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).attr("class", article_class).render();
    }

    @Override
    public void SendMsgToSelf(String msg, String from) {
        Map map = new HashMap<String, String>();
        map.put(this.getId() , this.getName() + " (" + this.getType() + ")");
        if (Interlocutor != null){
            map.put(this.Interlocutor.getId() , this.Interlocutor.getName() +
                    " (" + this.Interlocutor.getType() + ")");
        }
        session.send(
                new JSONObject()
                        .put("message", msg)
                        .put("userMessage", createHtmlMessageFromSender(from, msg))
                        .put("userlist", map.values()).toString()
        );
    }

    @Override
    public void sendUserList (User user){
        Map map = new HashMap<String, String>();
        map.put(user.getId(), user.getName());
        session.send(new JSONObject()
                .put("userlist", map.values()).toString()
        );
    }

    @Override
    public boolean registerUser(String str, int u) {
        debPrt("registerUser " + str);
        String infAbtUser[] = str.split(" ");
//        if (!infAbtUser[1].equals("register")) return false;
        if(infAbtUser.length > 1){
            this.Name = infAbtUser[1];
        }else {
            this.Name = "incognito";
        }
        this.Type = userTypes[Integer.parseInt(infAbtUser[0])];
        this.setStatus(userStatuses[1]);
        return false;
    }

    @Override
    boolean checkingForCommands(String received) {

        HashMap receivedJSON = new HashMap<>(JSONtoHashMapStrStr(received));

        //checking for command
        if(receivedJSON.get("msgType").equals("message")) return false;

        //extracting the action
        String action = String.valueOf(receivedJSON.get("action"));
//        String keyword = "ll";

        //set connection type
        if(action.equals("setConnectionType")){
            this.setConnectonType(String.valueOf(receivedJSON.get("message")));
        }

        //register
        if(this.getStatus().equals(userStatuses[0]) && action.equals("register")){
            debPrt("register " + this.getId());
            this.registerUser(String.valueOf(receivedJSON.get("message")), 0);
            return true;
        }

        //leave
        if(this.getStatus().equals(userStatuses[2]) && action.equals("leave")){
            debPrt(this.getName() + " leave");
            disconnectingInterlocutors();
            this.SendMsgToSelf("you leave chat with " + Interlocutor.getName(), "Server");
            Interlocutor.SendMsgToSelf( this.getName() + " leave chat with you", "Server");
            clientTimeLog(this.getName() + " leave chat with " + Interlocutor.getName());
            return true;
        }

        //exit
        if(action.equals("exit")){
            debPrt(this.getName() + " exit");
            disconnectingInterlocutors();
            usedDisconnect("exit");
            return true;
        }

        return false;
    }
}
