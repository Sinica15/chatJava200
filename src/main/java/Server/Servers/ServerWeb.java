package Server.Servers;

import Server.Users.User;
import Server.Users.UserWS;
import Server.Utils.utils;
import io.javalin.Javalin;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static Server.Server.*;
import static Server.Utils.utils.timeLog;
import static j2html.TagCreator.*;

public class ServerWeb extends Thread {
    @Override
    public void run() {
        timeLog("Web server started");


        Javalin.create()
//            .port(HerokuUtil.getHerokuAssignedPort())
                .port(9001)
                .enableStaticFiles("/public")
                .ws("/chat", ws -> {
                    ws.onConnect(session -> {
                        timeLog("New web user request received on session: " + session);
                        int i = getI();
                        addUser(session.hashCode(), new UserWS(i,"WebSocket",session));
                    });
                    ws.onClose((session, status, message) -> {
                        removeUser(session.hashCode());
                    });
                    ws.onMessage((session, message) -> {
                        User user = getClientArr().get(session.hashCode());
                        if(user.getType().equals(userStatuses[0])){
                            user.registerUser(message, 0);
                        }
//                        System.out.println(getClientArr().get(session.hashCode()).getName() + " " + message);

                    });
                })
                .start();
    }

    // Sends a message from one user to all users, along with a list of current usernames
    private static void broadcastMessage(String sender, String message) {
//        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
//            session.send(
//                    new JSONObject()
//                            .put("userMessage", createHtmlMessageFromSender(sender, message))
////                    .put("userlist", userUsernameMap.values()).toString()
//                            .toString()
//            );
//        });
    }

    // Builds a HTML element with a sender-name, a message, and a timestamp
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }
}
