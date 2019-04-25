package ClientWS;

import javax.websocket.*;

@javax.websocket.ClientEndpoint
public class ClientEndpoint {
    @OnOpen
    public void onOpen(Session session) {

        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        ClientSay ClSay = new ClientSay(session);
        ClSay.start();

    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println(message);
        Client.messageLatch.countDown();
    }

    @OnClose
    public void onClose(){
        System.out.println("close?");
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}