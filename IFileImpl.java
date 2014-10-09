import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Formatter;
import java.util.Scanner;

public class IFileImpl extends UnicastRemoteObject implements IFile {
    private String outputFolder;
    public IFileImpl () throws RemoteException {
        this.outputFolder = "out/";
    }

    public String read(String fileName) throws RemoteException, FileNotFoundException {
        String output = "";
        Scanner in = new Scanner(new File(this.outputFolder + fileName));
        while(in.hasNext()) {
            output += in.next();
        }
        in.close();
        return output;
    }

    public String write(String fileName, String content) throws RemoteException, FileNotFoundException {
        Formatter f = new Formatter(new File(this.outputFolder + fileName));
        f.format("%s", content);
        f.close();
        return  "Wrote: " + fileName + " | " + content;
    }
}
