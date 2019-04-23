package Server.Servers.RESTApi;

import Server.Server;
import Server.Users.User;
import io.javalin.Context;
import io.javalin.Handler;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static Server.Server.*;

public class handlers {

    public static Handler hend1 = ctx -> {
        System.out.println("hend1");
        ctx.json("jopa");
    };

    public static Handler hend3 = ctx -> {
//        if(ctx.queryParam("kd").toString().equals("null")){
//            System.out.println("empty");
//        }
        ctx.result(ctx.queryParam("id") + " " + ctx.queryParam("kd"));
    };

    public static Handler e = ctx ->{
        ctx.json("");
    };

    static String takeParam(Context ctx, String param){
        StringBuilder strb = new StringBuilder(ctx.queryParam(param) + "_");
        strb.deleteCharAt(strb.length() - 1);
        return strb.toString();
    }

    static int[] paginationCheck(Context ctx){
        String pageNumber = takeParam(ctx, "pageNumber");
        String pageSize = takeParam(ctx, "pageSize");
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.valueOf(pageNumber);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("set default page number 1");
            pageNumberInt = 1;
        }
        try{
            pageSizeInt = Integer.valueOf(pageSize);
        }catch (Exception e){
            System.out.println(e);
            System.out.println("set default page size 10");
            pageSizeInt = 10;
        }
        int[] outArr = {pageNumberInt, pageSizeInt};
        return outArr;
    }

    static HashMap<Integer, User> pagList(Context ctx){
        String userType = takeParam(ctx, "userType");
        String userStatus = takeParam(ctx, "userStatus");
        HashMap<Integer, User> arrList = new HashMap<>();
        for(Map.Entry<Integer, User> item : Server.getClientArr().entrySet()){
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
        int[] pagLims = paginationCheck(ctx);
        HashMap<Integer, User> userList = pagList(ctx);

        JSONObject jsonOut = new JSONObject();

        int i = 1;
        for(Map.Entry<Integer, User> item : userList.entrySet()){
            if ((i > (pagLims[0] * pagLims[1] - pagLims[1]) && i < (pagLims[0] * pagLims[1] + 1))){
//                userList.remove(item.getKey());
                jsonOut.put(item.getKey().toString(), item.getValue().fullInfo());
            }
            i++;
        }
//        ctx.json("");
        System.out.println(jsonOut);

        String number = takeParam(ctx, "number");
        if (number.equals("true")){
            ctx.result(String.valueOf(userList.size()));
            //json do
        }else {
            ctx.result(jsonOut.toString());
        }

    };

}
