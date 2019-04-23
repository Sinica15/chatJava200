package Server.Utils;

public class logWrite {
    private static final logWrite inst = new logWrite();

    private logWrite() {
        super();
    }

    private synchronized void writeToFile(String str) {
        utils.timeLog(str);
    }

    private static logWrite getInstance() {
        return inst;
    }

    public static void prt(String str){
        logWrite.getInstance().writeToFile(str);
    }
}
