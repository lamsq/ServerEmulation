import java.io.File;

public class Replication {

    public static final int DUB_PORT = 1235;
    public static final int CORK_PORT = 1238;
    private static File dubBase = new File("DublinDatabase.txt");
    private static File newDubBase = new File("newDublinRecords.txt");
    private static File corkBase = new File("CorkDatabase.txt");
    private static File newCorkBase = new File("newCorkRecords.txt");

    public static void main(String[] args) {
        try{
            System.out.println("Simulation started;");
            DublinServer ds = new DublinServer(DUB_PORT, dubBase, newDubBase);
            CorkServer cs = new CorkServer(CORK_PORT, corkBase, newCorkBase);
            ds.start();
            cs.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
