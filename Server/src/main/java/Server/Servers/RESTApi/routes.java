package Server.Servers.RESTApi;

import static Server.Servers.RESTApi.handlers.*;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class routes {
    public static void route(){
        path("api", () ->{
            path("users", () ->{
                get(allUsers);
            });
            get(hend3);
        });
    }
}
