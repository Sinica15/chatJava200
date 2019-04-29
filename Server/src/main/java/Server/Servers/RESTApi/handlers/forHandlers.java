package Server.Servers.RESTApi.handlers;

import com.google.gson.Gson;
import io.javalin.Context;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static Server.Utils.utils.debPrt;

public class forHandlers {

    public static String takeParam(Context ctx, String param){
        StringBuilder strb = new StringBuilder(ctx.queryParam(param) + "_");
        strb.deleteCharAt(strb.length() - 1);
        return strb.toString();
    }

    public static int[] paginationCheck(Context ctx){
        String pageNumber = takeParam(ctx, "pageNumber");
        String pageSize = takeParam(ctx, "pageSize");
        int pageNumberInt, pageSizeInt;
        try{
            pageNumberInt = Integer.valueOf(pageNumber);
        }catch (Exception e){
            debPrt(e.toString());
            debPrt("set default page number 1");
            pageNumberInt = 1;
        }
        try{
            pageSizeInt = Integer.valueOf(pageSize);
        }catch (Exception e){
            debPrt(e.toString());
            debPrt("set default page size 10");
            pageSizeInt = 10;
        }
        int[] outArr = {pageNumberInt, pageSizeInt};
        return outArr;
    }

    public static String requestResponse(Context ctx, Map<String, Object> List){

        int[] pagLims = paginationCheck(ctx);

        HashMap<String, Object> userList = new HashMap<>();

        int i = 1;
        for(Map.Entry<String, Object> item : List.entrySet()){
            if ((i > (pagLims[0] * pagLims[1] - pagLims[1]) &&
                    i < (pagLims[0] * pagLims[1] + 1)))
            {
                userList.put(item.getKey(), item.getValue());
            }
            i++;
        }


        String number = takeParam(ctx, "number");
        if (number.equals("true")){
            return new JSONObject().put("number", List.size()).toString();
        }else {
            Gson gson = new Gson();
            return gson.toJson(userList);
        }
    }

}
