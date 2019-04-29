package Server.test;

public class testObj {
    private String Name;
    private String Id;
    private String[] msgs;

    public testObj(String name, String id, String[] msgs) {
        Name = name;
        Id = id;
        this.msgs = msgs;
    }

    public String getName() {
        return Name;
    }

    public String getId() {
        return Id;
    }

    public String[] getMsgs() {
        return msgs;
    }
}
