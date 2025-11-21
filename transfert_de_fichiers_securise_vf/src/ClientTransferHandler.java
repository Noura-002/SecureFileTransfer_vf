import java.io.*;
import java.net.Socket;

public class ClientTransferHandler implements Runnable {

    private final Socket clientSocket;

    public ClientTransferHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            // Phase 1 : Authentification
            String login = (String) in.readObject();
            String password = (String) in.readObject();

            if (SecureFileServer.authenticate(login, password)) {
                out.writeObject("AUTH_OK");
                System.out.println("Authentification réussie pour : " + login);
            } else {
                out.writeObject("AUTH_FAIL");
                System.out.println("Échec authentification : " + login);
                return;
            }

            // Phase 2 : Négociation
            FileMetadata metadata = (FileMetadata) in.readObject();
            out.writeObject("READY_FOR_TRANSFER");
            System.out.println("Métadonnées reçues : " + metadata);

            // Phase 3 : Transfert
            long fileSize = metadata.getFileSize();
            byte[] encryptedData = new byte[(int) fileSize];
            DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
            dataIn.readFully(encryptedData);

            // Déchiffrement
            byte[] decryptedData = CryptoUtils.decrypt(encryptedData);

            // Vérification intégrité
            String receivedHash = CryptoUtils.sha256(decryptedData);
            if (!receivedHash.equals(metadata.getSha256Hash())) {
                out.writeObject("TRANSFER_FAIL");
                System.out.println("Échec : hachage incorrect !");
                return;
            }

            // Sauvegarde
            File outputFile = new File(SecureFileServer.getUploadDir(), metadata.getFileName());
            CryptoUtils.writeFileBytes(decryptedData, outputFile);

            out.writeObject("TRANSFER_SUCCESS");
            System.out.println("Transfert réussi : " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
                out.writeObject("TRANSFER_FAIL");
            } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (IOException e) {}
        }
    }
}