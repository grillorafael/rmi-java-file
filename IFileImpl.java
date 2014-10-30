import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Formatter;
import java.util.Scanner;

public class IFileImpl extends UnicastRemoteObject implements IFile {
    private String outputFolder;
    private int numReaders;
    private int numWriters;

    public IFileImpl () throws RemoteException {
        this.outputFolder = "out/";
        this.numReaders = 0;
        this.numWriters = 0;
    }

    private void printLog() {
        System.out.println("Number of readers: " + this.numReaders);
        System.out.println("Number of writers: " + this.numWriters);
    }

    public String read(String fileName) throws RemoteException, FileNotFoundException {
        printLog();

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
        try {
            String output = "";
            Scanner in = new Scanner(new File(this.outputFolder + fileName));
            while(in.hasNext()) {
                output += in.next();
            }
            in.close();
            numReaders--;
            return output;
        }
        catch (Exception e) {
            numReaders--;
            return "Unable to read file: " + fileName;
        }
    }

    public String write(String fileName, String content) throws RemoteException, FileNotFoundException {
        printLog();

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
        try {
            Formatter f = new Formatter(new File(this.outputFolder + fileName));
            f.format("%s", content);
            f.close();
            numWriters--;
            return  "Wrote: " + fileName + " | " + content;
        }
        catch (Exception e) {
            numWriters--;
            return  "Unable to write file: " + fileName;
        }
    }
}
