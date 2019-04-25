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
        super(session.getId(), ConnectonType);
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
    public void SendMsgToSelf(String msg, String from) {
        Map map = new HashMap<Integer, String>();
        map.put(0 , this.getName());
        if (Interlocutor != null){
            map.put(1 , Interlocutor.getName());
        }
        session.send(
                new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(from, msg))
                        .put("userlist", map.values()).toString()
        );
    }

    @Override
    public boolean registerUser(String str, int u) {
        String infAbtUser[] = str.split(" ");
        if (!infAbtUser[1].equals("register")) return false;
        this.Name = infAbtUser[2];
        this.Type = userTypes[Integer.parseInt(infAbtUser[3])];
        this.setStatus(userStatuses[1]);
        return false;
    }

    @Override
    boolean checkingForCommands(String received) {
        //checking for command
        if(!received.split(" ")[0].equals("$%$$")) return false;

        //extracting the keyword
        String keyword = received.split(" ")[1];

        //register
        if(this.getStatus().equals(userStatuses[0]) && keyword.equals("register")){
            this.registerUser(received, 0);
            return true;
        }

        //leave
        if(this.getStatus().equals(userStatuses[2]) && keyword.equals("leave")){
            System.out.println(this.getName() + " leave");
            disconnectingInterlocutors();
            this.SendMsgToSelf("you leave chat with " + Interlocutor.getName(), "Server");
            Interlocutor.SendMsgToSelf( this.getName() + " leave chat with you", "Server");
            clientTimeLog(this.getName() + " leave chat with " + Interlocutor.getName());
            return true;
        }


        //exit
        if(keyword.equals("exit")){
            System.out.println(this.getName() + " exit");
            disconnectingInterlocutors();
            usedDisconnect("exit");
            return true;
        }



        return false;
    }
}
