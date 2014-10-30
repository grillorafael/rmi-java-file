import java.rmi.*;

public class FileServer {
    public static void main (String[] args) {
        try {
            System.out.println("Create server handlers...");
            IFileImpl pi = new IFileImpl();

            System.out.println("Binding handlers to services...");
            Naming.rebind("file_service", pi);
            System.out.println("Server is ready to receive requests");
        } catch (Exception e) {
            System.out.println("Server Error: " + e);
        }
    }
}
