package Server.test;

import com.google.gson.Gson;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {

        HashMap<String, String> userFullInfo = new HashMap<>();
        userFullInfo.put("Id", "jhgjhg-fgdfg");
        userFullInfo.put("Name", "noName");
        userFullInfo.put("Type", "agent");
        userFullInfo.put("Status", "not logined");
        userFullInfo.put("ConnectonType", "web");

        Gson gson = new Gson();
        System.out.println(gson.toJson(userFullInfo));

        HashMap<String, Object> usli = new HashMap<>();

        String[] msgs = {"ff","ss"};

        usli.put("vsia", new testObj("vsia", "228", msgs));
        usli.put("petia", new testObj("petia", "337", msgs));
        usli.put("jeka", new testObj("jeka", "322", msgs));

        System.out.println(gson.toJson(usli));


    }
}
