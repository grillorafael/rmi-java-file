import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Thread.sleep;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class IFileImpl extends UnicastRemoteObject implements IFile {
    private String outputFolder;
    private int numReaders;
    private int numWriters;
    private ReadWriteLock locker;

    public IFileImpl () throws RemoteException {
        this.outputFolder = "out/";
        this.numReaders = 0;
        this.numWriters = 0;
        locker=new ReentrantReadWriteLock();
    }

    private void printLog() {
        System.out.println("Number of readers: " + this.numReaders);
        System.out.println("Number of writers: " + this.numWriters);
    }

    public String read(String fileName) throws RemoteException, FileNotFoundException {
        printLog();
        /*
        if(numWriters > 0) {
            try {
                long timeout = 1000;
                wait(timeout);
                return read(fileName);
            }
            catch (Exception e) {
                return "Unable to read file: " + fileName;
            }
        }

        numReaders++;
        */
        System.out.println("Trying to read from " + fileName);
        numReaders++;
        locker.readLock().lock();
        try {
            String output = "";
            long timeout = 1000;
            sleep(timeout);
            Scanner in = new Scanner(new File(this.outputFolder + fileName));
            while(in.hasNext()) {
                output += in.next();
            }
            in.close();
            //numReaders--;
            numReaders--;
            locker.readLock().unlock();
            return output;
        }
        catch (Exception e) {
            //numReaders--;
            numReaders--;
            locker.readLock().unlock();
            return "Unable to read file: " + fileName;
        }finally{
           // numReaders--;
            //locker.readLock().unlock();
        }
    }
    //Pensei em usar o syncronized (que permitiria apenas um acesso por vez ao método),
    // nesse caso o problema seria proteger do read.
    public String write(String fileName, String content) throws RemoteException, FileNotFoundException {
        printLog();
        /*
        if(numWriters > 0) {
            try {
                long timeout = 1000;
                wait(timeout);
                return write(fileName, content);
            }
            catch (Exception e) {
                return "Unable to write file: " + fileName;
            }
        }
        numWriters++;
        */
        System.out.println("Trying to write at " + fileName);
        numWriters++;
        locker.writeLock().lock();
        try {
            long timeout = 1000;
            sleep(timeout);
            Formatter f = new Formatter(new File(this.outputFolder + fileName));
            f.format("%s", content);
            f.close();
           //numWriters--;
            numWriters--;
            locker.writeLock().unlock();
            return  "Wrote: " + fileName + " | " + content;
        }
        catch (Exception e) {
            //numWriters--;
            numWriters--;
            locker.writeLock().unlock();
            return  "Unable to write file: " + fileName;
        }finally{
           // numWriters--;
           // locker.writeLock().unlock();
        }
    };
}
