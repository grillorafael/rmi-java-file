
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
    private static final long TIMEOUT = 5000;

    public IFileImpl() throws RemoteException {
        this.outputFolder = "out/";
        this.numReaders = 0;
        this.numWriters = 0;
        locker = new ReentrantReadWriteLock(true);
    }

    private void printLog() {
        System.out.println("Number of readers: " + this.numReaders);
        System.out.println("Number of writers: " + this.numWriters);
    }

    public String read(String fileName) throws RemoteException, FileNotFoundException {
        printLog();
        System.out.println("Trying to read from " + fileName);
        numReaders++;
        locker.readLock().lock();
        try {
            String output = "";
            sleep(TIMEOUT);
            Scanner in = new Scanner(new File(this.outputFolder + fileName));
            while (in.hasNext()) {
                output += in.nextLine() + "\n";
            }
            in.close();
            return output;
        } catch (Exception e) {
            return "Unable to read file: " + fileName;
        } finally {
            numReaders--;
            locker.readLock().unlock();
        }
    }

    public String write(String fileName, String content) throws RemoteException, FileNotFoundException {
        printLog();
        System.out.println("Trying to write at " + fileName);
        numWriters++;
        locker.writeLock().lock();
        try {
            sleep(TIMEOUT);
            Formatter f = new Formatter(new File(this.outputFolder + fileName));
            f.format("%s", content);
            f.close();
            return "Wrote: " + fileName + " | " + content;
        } catch (Exception e) {
            return "Unable to write file: " + fileName;
        } finally {
            numWriters--;
            locker.writeLock().unlock();
        }
    }
;
}
