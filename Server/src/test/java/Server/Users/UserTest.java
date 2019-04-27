package Server.Users;

import Server.Server;
import io.javalin.websocket.WsSession;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.junit.Test;
import org.mockito.Spy;

import java.net.Socket;

import static Server.Server.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserTest {

    @Test
    public void connectionWith() {

        Socket s1 = mock(Socket.class);
//        WsSession session = mock(WsSession.class);

//        UserWS spyWS1 = spy(new UserWS(0, "WebSocket", session));

        UserS spyS1 = spy(new UserS( "1", "Socket", s1));
        UserS spyS2 = spy(new UserS( "2", "Socket", s1));

//        UserS spyS1 = mock(UserS.class);
//        UserWS spyS2 = mock(UserWS.class);

        spyS1.setType(userTypes[0]);
        spyS2.setType(userTypes[1]);
//        spyWS1.setType(userTypes[1]);

        doNothing().when(spyS1).setStatus(anyString());
        doNothing().when(spyS2).setStatus(anyString());
//        doNothing().when(spyWS1).setStatus(anyString());

        doNothing().when(spyS1).SendMsgToSelf(anyString(), anyString());
        doNothing().when(spyS2).SendMsgToSelf(anyString(), anyString());
//        doNothing().when(spyWS1).SendMsgToSelf(anyString(), anyString());

        doNothing().when(spyS1).SendMsgToInterloc(anyString());
        doNothing().when(spyS2).SendMsgToInterloc(anyString());
//        doNothing().when(spyWS1).SendMsgToInterloc(anyString());

        spyS1.connectionWith(spyS2);
//        spyS1.connectionWith(spyWS1);

//        System.out.println(spyS1);
//        System.out.println(spyS2);

        assertEquals(spyS1, spyS2.Interlocutor);
        assertEquals(spyS2, spyS1.Interlocutor);

    }

    @Test
    public void setStatus() {

        Socket s1 = mock(Socket.class);

        UserS spyS1 = spy(new UserS( "1", "Socket", s1));
        UserS spyS2 = spy(new UserS( "2", "Socket", s1));

        doNothing().when(spyS1).lookingFor(anyString());
        doNothing().when(spyS2).lookingFor(anyString());

        spyS1.setType(userTypes[0]);
        spyS2.setType(userTypes[1]);

        spyS1.setStatus(userStatuses[1]);
        spyS2.setStatus(userStatuses[1]);

        assertEquals(spyS1.getStatus(), userStatuses[1]);
        assertEquals(spyS2.getStatus(), userStatuses[3]);

        spyS1.setStatus(userStatuses[3]);
        spyS2.setStatus(userStatuses[3]);

        assertEquals(spyS1.getStatus(), userStatuses[3]);
        assertEquals(spyS2.getStatus(), userStatuses[3]);

        spyS1.setStatus(userStatuses[2]);
        spyS2.setStatus(userStatuses[2]);

        assertEquals(spyS1.getStatus(), userStatuses[2]);
        assertEquals(spyS2.getStatus(), userStatuses[2]);
    }

    @Test
    public void lookingFor() {

        Socket s1 = mock(Socket.class);

        UserS spyS1 = spy(new UserS( "1", "Socket", s1));
        UserS spyS2 = spy(new UserS( "2", "Socket", s1));

        spyS1.setType(userTypes[0]);
        spyS2.setType(userTypes[1]);
        spyS2.setStatus(userStatuses[3]);

        doNothing().when(spyS1).setStatus(anyString());
        doNothing().when(spyS2).setStatus(anyString());
        doNothing().when(spyS1).SendMsgToSelf(anyString(), anyString());
        doNothing().when(spyS2).SendMsgToSelf(anyString(), anyString());
        doNothing().when(spyS1).SendMsgToInterloc(anyString());
        doNothing().when(spyS2).SendMsgToInterloc(anyString());

        addUser(spyS1.getId(), spyS1);
        addUser(spyS2.getId(), spyS2);

        spyS1.lookingFor(userTypes[1]);

        assertEquals(spyS1, spyS2.Interlocutor);
        assertEquals(spyS2, spyS1.Interlocutor);

    }

    @Test
    public void disconnectingInterlocutors() {

        Socket s1 = mock(Socket.class);

        UserS spyS1 = spy(new UserS( "1", "Socket", s1));
        UserS spyS2 = spy(new UserS( "2", "Socket", s1));

        spyS1.setInterlocutor(spyS2);
        spyS2.setInterlocutor(spyS1);

        spyS1.setType(userTypes[0]);
        spyS2.setType(userTypes[1]);

        spyS1.disconnectingInterlocutors();

        assertEquals(spyS1.getStatus(), userStatuses[1]);
        assertEquals(spyS2.getStatus(), userStatuses[3]);

    }

    @Test
    public void usedDisconnect() {

        Socket s1 = mock(Socket.class);

        UserS spyS1 = spy(new UserS( "1", "Socket", s1));
        UserS spyS2 = spy(new UserS( "2", "Socket", s1));

        addUser(spyS1.getId(), spyS1);
        addUser(spyS2.getId(), spyS2);

        assertEquals(getClientArr().size(), 2);

        spyS1.usedDisconnect("exit");
        spyS2.usedDisconnect("disconnected");

        assertEquals(getClientArr().size(), 0);

    }

    @Test
    public void runMethod() {

        Socket s1 = mock(Socket.class);

        UserS spyS1 = spy(new UserS( "1", "Socket", s1));
        UserS spyS2 = spy(new UserS( "2", "Socket", s1));

        doNothing().when(spyS1).SendMsgToSelf(any(), any());

        spyS1.setType(userTypes[0]);
        spyS1.setStatus(userStatuses[1]);

        spyS1.runMethod("", "some text 1");
        spyS1.runMethod("", "some text 1");

        System.out.println(spyS1.getStatus());
        System.out.println(spyS1.clientMassages.size());

    }
}
