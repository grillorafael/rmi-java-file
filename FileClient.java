
import java.util.*;
import java.rmi.*;

public class FileClient {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String fileName, fileContent="";
        boolean end = false;
        int opcao;

        System.setSecurityManager(new RMISecurityManager());
        String url = "rmi://localhost/";

        try {
            IFile fileService = (IFile) Naming.lookup(url + "file_service");
            while (!end) {
                System.out.println("Remote File\n1 - Read File\n2 - Write File\n3 - Exit\nOpcao: ");
                opcao = in.nextInt();
                switch (opcao) {
                    case 1: {
                        System.out.print("Insert file name: ");
                        fileName = in.next();
                        System.out.println(fileService.read(fileName));
                        break;
                    }
                    case 2: {
                        System.out.print("Insert file name: ");
                        fileName = in.next();
                        System.out.print("Insert file content: ");
                        String sentence;
                        while (true) {
                            sentence = in.nextLine();
                            if (sentence.equals("Fim")) {
                                break;
                            }
                            fileContent += sentence + "\n";

                        }
                        System.out.println(fileService.write(fileName, fileContent));
                        break;
                    }
                    case 3: {
                        end = true;
                        break;
                    }
                }
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("Client Error: " + e);
        }
        System.exit(0);
    }
}
