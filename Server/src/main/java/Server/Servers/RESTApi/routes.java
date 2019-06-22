package Server.Servers.RESTApi;

import static Server.Servers.RESTApi.handlers.allChatsHandler.allChats;

import static Server.Servers.RESTApi.handlers.allUsersHandler.allUsers;
import static Server.Servers.RESTApi.handlers.userWork.*;
import static io.javalin.apibuilder.ApiBuilder.*;

public class routes {
    public static void route(){
        path("api", () ->{
            path("users", () ->{
                get(allUsers);
            });
            path("chats", () -> {
                get(allChats);
            });
            path("userwork", () ->{
                post("register", registerUser);
                post("sendmsg" , sendMsg);
                get("cheknewmsg" , checkNewMsg);

                post("leave", leave);
            });
        });
    }
}
