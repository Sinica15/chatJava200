package Server.Servers.RESTApi.handlers;

import Server.Server;
import Server.Users.User;
import io.javalin.Context;
import io.javalin.Handler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Server.Server.userStatuses;
import static Server.Servers.RESTApi.handlers.forHandlers.requestResponse;

public class allChatsHandler {

    static HashMap<String, Object> chatCounter(Context ctx){
        HashMap<String, Object> chatsList = new HashMap<>();

        ArrayList<String> ids = new ArrayList<>();
        for(Map.Entry<String, User> item : Server.getClientArr().entrySet()){
            if (item.getValue().getStatus().equals(userStatuses[2]) &&
                ids.indexOf(item.getValue().getId()) < 0){

                String chatId = item.getValue().getId().substring(24) + " " +
                                item.getValue().getInterlocutor().getId().substring(24);
                ids.add(item.getValue().getId());
                ids.add(item.getValue().getInterlocutor().getId());

                ArrayList<Object> usersInChat = new ArrayList<>();
                usersInChat.add(item.getValue());
                usersInChat.add(item.getValue().getInterlocutor());
                chatsList.put(chatId, usersInChat);
            }
        }

        return chatsList;
    }

    public static Handler allChats = ctx ->{
        ctx.result(requestResponse(ctx, chatCounter(ctx)));
    };
}
