package Server.Servers.RESTApi.handlers;

import Server.Users.User;
import Server.Users.UserREST;
import com.google.gson.Gson;
import io.javalin.Handler;

import java.util.HashMap;
import java.util.UUID;

import static Server.Server.*;
import static Server.Servers.RESTApi.handlers.forHandlers.takeParam;
import static Server.Utils.utils.JSONtoHashMapStrStr;

public class userWork {

    public static Handler registerUser = ctx ->{
        HashMap<String, String> received = new HashMap<>(JSONtoHashMapStrStr(ctx.body()));
        String randomUUID;
        if (received.get("action").equals("register")) {
            for (boolean flag = true; flag;){
                randomUUID = UUID.randomUUID().toString();
                if (!getClientArr().containsKey(randomUUID)){
                    User user = new UserREST(randomUUID, "REST");
                    user.registerUser(received.get("message"), 0);
                    addUser(randomUUID, user);
                    flag = false;
                    Gson gson = new Gson();
                    ctx.result(gson.toJson(getClientArr().get(randomUUID)));
                }
            }
        }
    };

    public static Handler sendMsg = ctx ->{
        HashMap<String, String> received = new HashMap<>(JSONtoHashMapStrStr(ctx.body()));
        String userFromId = received.get("from");
        User user = getClientArr().get(userFromId);
        user.runMethod(ctx.body(), received.get("message"));
    };

    public static Handler checkNewMsg = ctx ->{
        String userId = takeParam(ctx, "userId");

        if(getClientArr().get(userId).getConnectonType().equals(connectionTypes[2])){
            UserREST user = (UserREST)getClientArr().get(userId);
            ctx.result(user.readMessages());
        }else {
            ctx.result("{\"answer\":\"this is not rest user\"}");
        }
    };

    public static Handler leave = ctx ->{
        HashMap<String, String> received = new HashMap<>(JSONtoHashMapStrStr(ctx.body()));
        System.out.println(ctx.body());
        ctx.result(ctx.body() + "))");
    };

}
