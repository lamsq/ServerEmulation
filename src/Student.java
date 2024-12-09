import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Student {

    private String name;
    private String course;
    private String id;
    private String replication;

    public Student(String name, String course, String id, String replication) {
        this.name = name;
        this.course = course;
        this.id = id;
        this.replication = replication;
    }

    public Student() {
        this.name = null;
        this.course = null;
        this.id = null;
        this.replication = null;
    }

    public String getName(){
        return name;
    }

    public String getCourse(){
        return course;
    }

    public String toString(){
        return id+" ## Name: "+name+" ## Course: "+course+" >>"+replication;
    }

    public boolean equals(Object o){
        if(!(o instanceof Student))
            return false;
        Student s = (Student)o;
        return this.toString().equals(s.toString());
    }

    public void setId(String id){
        this.id = id;
    }

    public void setReplication(String replication){
        this.replication = replication;
    }

    public String id(){
        return this.id;
    }

    public Student copy(){
        return new Student(name, course, id, replication);
    }

    public String replication(){
        return this.replication;
    }

    public void writeOutputStream(DataOutputStream out){
        try{
            out.writeUTF(name);
            out.writeUTF(course);
            out.writeUTF(id);
            out.writeUTF(replication);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void readInputStream(DataInputStream in){
        try{
            name = in.readUTF();
            course = in.readUTF();
            id = in.readUTF();
            replication = in.readUTF();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
