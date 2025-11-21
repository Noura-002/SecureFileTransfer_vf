import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SecureFileServer {

    private static final int PORT = 5000;
    private static final String UPLOAD_DIR = "server_uploads";
    private static final Map<String, String> USERS = new HashMap<>();

    static {
        // Utilisateurs codés en dur
        USERS.put("alice", "pass123");
        USERS.put("bob", "secret456");
        USERS.put("charlie", "pwd789");

        // Créer le dossier de réception
        new File(UPLOAD_DIR).mkdirs();
    }

    public static void main(String[] args) {
        System.out.println("Serveur démarré sur le port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion : " + clientSocket.getInetAddress());

                // Déléguer à un thread
                new Thread(new ClientTransferHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour vérifier les identifiants
    public static boolean authenticate(String login, String password) {
        return USERS.containsKey(login) && USERS.get(login).equals(password);
    }

    public static String getUploadDir() {
        return UPLOAD_DIR;
    }
}