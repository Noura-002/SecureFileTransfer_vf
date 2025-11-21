import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SecureFileClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Adresse IP du serveur (ex: localhost) : ");
        String host = scanner.nextLine();

        System.out.print("Port (ex: 5000) : ");
        int port = scanner.nextInt();
        scanner.nextLine(); // consommer le \n

        System.out.print("Login : ");
        String login = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        System.out.print("Chemin du fichier à envoyer : ");
        String filePath = scanner.nextLine();

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println("Fichier introuvable !");
            return;
        }

        try (
                Socket socket = new Socket(host, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            // Phase 1 : Authentification
            out.writeObject(login);
            out.writeObject(password);

            String authResponse = (String) in.readObject();
            if (!"AUTH_OK".equals(authResponse)) {
                System.out.println("Échec d'authentification !");
                return;
            }
            System.out.println("Authentification réussie.");

            // Pré-traitement : hash + chiffrement
            byte[] fileBytes = CryptoUtils.readFileBytes(file);
            String sha256 = CryptoUtils.sha256(fileBytes);
            byte[] encryptedBytes = CryptoUtils.encrypt(fileBytes);

            FileMetadata metadata = new FileMetadata(
                    file.getName(),
                    encryptedBytes.length,
                    sha256
            );

            // Phase 2 : Négociation
            out.writeObject(metadata);
            String readyResponse = (String) in.readObject();
            if (!"READY_FOR_TRANSFER".equals(readyResponse)) {
                System.out.println("Serveur non prêt !");
                return;
            }

            // Phase 3 : Transfert
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.write(encryptedBytes);
            dataOut.flush();

            String finalResponse = (String) in.readObject();
            if ("TRANSFER_SUCCESS".equals(finalResponse)) {
                System.out.println("Transfert réussi !");
            } else {
                System.out.println("Échec du transfert.");
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}