import java.io.*;
import java.util.*;

public class StudentAdd extends Thread {

    private File file;

    public StudentAdd(File file) {
        this.file = file;
    }

    public void run() {
        try{
            while(true){
                if (!file.createNewFile()) {
                    String[] names = {"Jack", "Tim", "Tom", "John", "Paul", "Ben", "Rose", "Katrin", "Jessica", "Angelina"};
                    String[] surnames = {"Smith", "Taylor", "Evans", "Jones", "Brown", "Wilson", "Williams", "Michael", "Thomas", "Davies"};
                    String[] courses = {"Computer Science", "Finance", "Engineering", "Medicine", "Economics", "Law", "Psychology", "Management", "Architecture", "Healthcare" };

                    FileWriter fw = new FileWriter(file, true);
                    Random random = new Random();
                    int num = random.nextInt(5)+1;

                    for(int i = 0; i<num; i++){
                        StringBuilder record = new StringBuilder();
                        record.append(names[(int)(Math. random() * names.length)]).append(" ");
                        record.append(surnames[(int)(Math. random() * surnames.length)]).append(":");
                        record.append(courses[(int)(Math. random() * courses.length)]).append("\n");
                        fw.write(record.toString());
                    }
                    fw.close();
                    Thread.sleep(2*60*1000);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
