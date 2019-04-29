package Server.Servers.RESTApi;

import static Server.Servers.RESTApi.handlers.allChatsHandler.allChats;
import static Server.Servers.RESTApi.handlers.allUsersHandler.hend3;

import static Server.Servers.RESTApi.handlers.allUsersHandler.allUsers;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class routes {
    public static void route(){
        path("api", () ->{
            path("users", () ->{
                get(allUsers);
            });
            path("chats", () -> {
                get(allChats);
            });
            get(hend3);
        });
    }
}
