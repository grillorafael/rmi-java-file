import java.io.FileNotFoundException;
import java.rmi.*;

public interface IFile extends Remote {
    public String read(String fileName) throws RemoteException, FileNotFoundException, InterruptedException;
    public String write(String fileName, String content) throws RemoteException, FileNotFoundException;
}
