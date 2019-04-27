package Server.Servers;

import Server.Servers.RESTApi.routes;
import Server.Users.User;
import Server.Users.UserWS;
import io.javalin.Javalin;

import static Server.Server.*;
import static Server.Utils.utils.*;

public class ServerWeb extends Thread {
    @Override
    public void run() {
        timeLog("Web server started");

        Javalin.create()
                .port(9003)
                .enableStaticFiles("/public")
                .ws("/chat", ws -> {
                    ws.onConnect(session -> {
                        timeLog("New web user request received on session: " + session);
                        int i = getI();
                        addUser(session.getId(), new UserWS(i,"WebSocket", session));
                    });
                    ws.onClose((session, status, message) -> {
                        debPrt("close");
                        removeUser(session.getId());
                    });
                    ws.onMessage((session, received) -> {
                        User user = getClientArr().get(session.getId());
                        String message = String.valueOf(JSONtoHashMapStrStr(received).get("message"));
                        debPrt(received);
                        debPrt(user.getName() + " " + message);
                        user.runMethod(received, message);
                    });
                    ws.onError((wsSession, throwable) -> System.out.println(throwable));
                })
                .routes(() -> {
                    routes.route();
                })
                .start();
    }

}
