package Server.Servers.RESTApi.handlers;

import Server.Server;
import Server.Users.User;
import com.google.gson.Gson;
import io.javalin.Context;
import io.javalin.Handler;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static Server.Server.*;
import static Server.Servers.RESTApi.handlers.forHandlers.*;

public class allUsersHandler {

    public static Handler hend3 = ctx -> {
//        if(ctx.queryParam("kd").toString().equals("null")){
//            System.out.println("empty");
//        }
        ctx.result(ctx.queryParam("id") + " " + ctx.queryParam("kd"));
    };

    public static Handler e = ctx ->{
        ctx.json("");
    };

    static HashMap<String, Object> pagList(Context ctx){
        HashMap<String, Object> arrList = new HashMap<>();
        String userType = takeParam(ctx, "userType");
        String userStatus = takeParam(ctx, "userStatus");
        for(Map.Entry<String, User> item : Server.getClientArr().entrySet()){
            if (userType.equals(userTypes[0]) || userType.equals(userTypes[1])){
                if(!userType.equals(item.getValue().getType())){
                    continue;
                }
            }
            if (userStatus.equals("registered") ||
                userStatus.equals("waiting") ||
                userStatus.equals("free")){
                if( userStatus.equals("registered") && item.getValue().getStatus().equals(userStatuses[0])){
                    continue;
                }
                if( userStatus.equals("waiting") && !item.getValue().getStatus().equals(userStatuses[3])){
                    continue;
                }
                if( userStatus.equals("free") &&
                    (item.getValue().getStatus().equals(userStatuses[0]) ||
                     item.getValue().getStatus().equals(userStatuses[2]))){
                    continue;
                }
            }

            arrList.put(item.getKey(), item.getValue());
        }
        return arrList;
    }

    public static Handler allUsers = ctx ->{

        ctx.result(requestResponse(ctx, pagList(ctx)));
    };

}
