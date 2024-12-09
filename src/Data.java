import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Data {

    private List<Student> unsynchData = new ArrayList<>();
    private List<Student> data = Collections.synchronizedList(unsynchData);

    private List<Student> unsynchDataFetch = new ArrayList<>();
    private List<Student> dataFetch = Collections.synchronizedList(unsynchDataFetch);

    private Lock lock = new ReentrantLock();

    public void add(Student s){
        lock.lock();
        try{
            data.add(s);
        }
        finally{
            lock.unlock();
        }
    }

    public void remove(int i){
        lock.lock();
        try{
            data.remove(i);
        }
        finally{
            lock.unlock();
        }
    }

    public boolean empty(){
        lock.lock();
        try{
            if(!data.isEmpty())
                return false;
        }
        finally{
            lock.unlock();
        }
        return true;
    }

    public Student get(int index){
        lock.lock();
        try{
            return data.get(index);
        }
        finally{
            lock.unlock();
        }
    }



    public void addFetch(Student s){
        lock.lock();
        try{
            dataFetch.add(s);
        }
        finally{
            lock.unlock();
        }
    }

    public void clearFetch(){
        lock.lock();
        try{
            dataFetch.clear();
        }
        finally{
            lock.unlock();
        }
    }

    public void removeFetch(int i){
        lock.lock();
        try{
            dataFetch.remove(i);
        }
        finally{
            lock.unlock();
        }
    }

    public boolean emptyFetch(){
        lock.lock();
        try{
            if(!dataFetch.isEmpty())
                return false;
        }
        finally{
            lock.unlock();
        }
        return true;
    }

    public Student getFetch(int index){
        lock.lock();
        try{
            return dataFetch.get(index);
        }
        finally{
            lock.unlock();
        }
    }

    public List<Student> getFetchData(){
        lock.lock();
        try{
            return dataFetch;
        }
        finally{
            lock.unlock();
        }
    }






   public ArrayList<Student> getStudents() {
        lock.lock();
        try {
            return new ArrayList<>(data);
        } finally {
            lock.unlock();
        }
    }

    boolean search(Student s){
        lock.lock();
        try{
            return data.contains(s);
        }
        finally{
            lock.unlock();
        }
    }

    boolean clear(){
        lock.lock();
        try{
            data.clear();
            if(data.isEmpty())
                return true;
            else
                return false;
        }
        finally{
            lock.unlock();
        }
    }
}