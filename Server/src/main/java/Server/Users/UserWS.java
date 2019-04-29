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

    transient WsSession session;

    public UserWS(int id, String ConnectonType, WsSession session) {
        super(session.getId(), ConnectonType);
        this.session = session;
        setStatus(userStatuses[0]);
    }

    private String createHtmlMessageFromSender(String sender, String message) {
        String article_class = "interlocutor-article";
        if (sender.equals(this.getName())){
            sender = "me";
            article_class = "sender-article";
        }
        if (sender.equals("Server")){
            article_class = "server-article";
        }
        return article(
                p(message),
                p(
                        span(sender + ", ").attr("class", "sender"),
                        span(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()))
                                .attr("class", "datetime")
                ).attr("class", "sender-datetime")
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
}
