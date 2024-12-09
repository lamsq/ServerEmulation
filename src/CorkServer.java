import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class CorkServer extends Thread {

    protected int port;
    static int counter=0;
    private File db;
    private File nsdb;

    public CorkServer(int port, File database, File temDatabase){
        this.port = port;
        this.db = database;
        this.nsdb = temDatabase;
    }

    public void run(){
        System.out.println("Server Cork running...");
        Data data = new Data();
        new NewRecCheckC(data, nsdb).start();
        new TransferC(data, db).start();
        new StudentAdd(nsdb).start();
        new ClientInC(Replication.DUB_PORT, data).start();
        try{
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket socket = ss.accept();
                new ServerOutC(socket, data).start();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

class ClientInC extends Thread {

    private int port;
    private Data data;
    private String slogin = "Dublin";
    private String spassword = "Test";

    public ClientInC(int port, Data data) {
        this.port = port;
        this.data = data;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                Socket s = new Socket(InetAddress.getLocalHost(), port);
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                out.writeUTF(slogin);
                out.writeUTF(spassword);
                if (in.readBoolean()) {
                    out.writeUTF("get");
                    int m = in.readInt();
                    if (m != 0) {
                        System.out.println("Cork server: New Dublin server records found");
                        for (int i = 0; i < m; i++) {
                            Student st = new Student();
                            st.readInputStream(in);
                            data.add(st);
                        }
                    }
                } else {
                    System.out.println("Incorrect credentials;");
                }
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ServerOutC extends Thread {

    private Socket s;
    private Data data;
    private String accLogin = "Cork";
    private String accPassword = "Test";

    ServerOutC(Socket p, Data data) {
        this.s = p;
        this.data = data;
    }

    public void run(){
        try {
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            String login = in.readUTF();
            String pass = in.readUTF();
            if(login.equals(accLogin) && pass.equals(accPassword)){
                out.writeBoolean(true);
                String opt = in.readUTF();
                if(opt.equals("get")){
                    if(!data.emptyFetch()){
                        System.out.println("Cork server: new Cork records are transferred to Dublin server...");
                        ArrayList<Student> dubStudents = new ArrayList<>(data.getFetchData());
                        out.writeInt(dubStudents.size());
                        for (Student st : dubStudents) {
                            st.writeOutputStream(out);
                        }
                        data.clearFetch();
                    }
                    else {
                        out.writeInt(0);
                    }
                }
            }
            else{
                out.writeBoolean(false);
            }
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class TransferC extends Thread {

    Data data;
    File file;

    TransferC(Data data, File file) {
        this.data = data;
        this.file = file;
    }

    public void run() {
        Scanner reader;
        try{
            file.createNewFile();
            reader = new Scanner(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            Pattern pattern = Pattern.compile("C(\\d+) ##");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int num = Integer.parseInt(matcher.group(1));
                if(num>CorkServer.counter){
                    CorkServer.counter = Integer.parseInt(matcher.group(1));
                }
            }
        }
        reader.close();
        while (true) {
            try{
                Thread.sleep(1*60*1000);
                while (!data.empty()){
                    System.out.println("Cork server: new Cork records are transferred to the Cork database");
                    FileWriter fw = new FileWriter(file, true);
                    Student s = data.get(0);
                    if(s.id()==null){
                        ++CorkServer.counter;
                        s.setId("C"+CorkServer.counter);
                        Student s1 = s.copy();
                        s1.setReplication("F");
                        data.addFetch(s1);
                        s.setReplication("T");
                    }
                    fw.write(s.toString()+"\n");
                    data.remove(0);
                    fw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class NewRecCheckC extends Thread {

    Data data;
    File file;

    NewRecCheckC(Data data, File file) {
        this.file = file;
        this.data = data;
    }

    public void run(){
        while(true) {
            try {
                Thread.sleep(1000);
                if (file.exists()) {
                    Scanner reader = new Scanner(file);
                    while (reader.hasNextLine()) {
                        String line = reader.nextLine();
                        String[] info = line.split(":");
                        Student s = new Student(info[0], info[1], null, null);
                        data.add(s);
                    }
                    System.out.println("Cork server: new records in Cork server found");
                    reader.close();
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

