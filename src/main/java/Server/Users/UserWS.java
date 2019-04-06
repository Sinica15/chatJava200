package Server.Users;

import Server.Server;
import io.javalin.websocket.WsSession;

import static Server.Server.userStatuses;
import static Server.Server.userTypes;

public class UserWS extends User{

    WsSession session;

    public UserWS(int id, String ConnectonType, WsSession session){
        super(id, ConnectonType);
        this.session = session;
        setStatus(userStatuses[0]);
    }

    public void login(String str){
        if (!this.Status.equals(userStatuses[0])) return;


    }

    @Override
    public void setStatus(String status) {

    }

    @Override
    public void SendMsgToSelf(String msg) {
        session.send(msg);
    }

    @Override
    public boolean registerUser(String str, int u) {
        String infAbtUser[] = str.split(" ");
        this.Name = infAbtUser[0];
        this.Type = userTypes[Integer.getInteger(infAbtUser[1])];
        return false;
    }
}
