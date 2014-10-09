// Programa Java com RMI
// Calculadora.java
// Rafaelli de Carvalho Coutinho
//-------------------------------------------------------------

import java.io.FileNotFoundException;
import java.rmi.*;

public interface IFile extends Remote {
    public String read(String fileName) throws RemoteException, FileNotFoundException;
    public String write(String fileName, String content) throws RemoteException, FileNotFoundException;
}
