package Server.Users;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserREST extends User {

    ArrayList<Object> unreadMsg = new ArrayList<>();

    public UserREST(String id, String ConnectonType) {
        super(id, ConnectonType);
    }

    @Override
    void sendUserList(User user) {

    }

    @Override
    public void SendMsgToSelf(String msg, String from) {
        HashMap<String, String> fullMsg = new HashMap<>();
        fullMsg.put("message", msg);
        fullMsg.put("from", from);
        fullMsg.put("time", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        unreadMsg.add(fullMsg);
    }

    public String readMessages(){
        Gson gson = new Gson();
        String out = gson.toJson(unreadMsg);
        unreadMsg.clear();
        return out;
    }
}
