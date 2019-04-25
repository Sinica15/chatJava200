package Server.Servers;

import Server.Servers.RESTApi.routes;
import Server.Users.User;
import Server.Users.UserWS;
import io.javalin.Javalin;

import static Server.Server.*;
import static Server.Utils.utils.timeLog;

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
                        System.out.println("close");
                        removeUser(session.getId());
                    });
                    ws.onMessage((session, message) -> {
                        User user = getClientArr().get(session.getId());
                        System.out.println(user.getName() + " " + message);
                        user.runMethod(message);
                    });
                    ws.onError((wsSession, throwable) -> System.out.println(throwable));
                })
                .routes(() -> {
                    routes.route();
                })
                .start();
    }

}
