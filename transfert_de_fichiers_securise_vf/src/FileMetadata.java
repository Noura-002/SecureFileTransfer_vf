import java.io.Serializable;

public class FileMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private long fileSize;
    private String sha256Hash;

    public FileMetadata(String fileName, long fileSize, String sha256Hash) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.sha256Hash = sha256Hash;
    }

    // Getters
    public String getFileName() { return fileName; }
    public long getFileSize() { return fileSize; }
    public String getSha256Hash() { return sha256Hash; }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", sha256Hash='" + sha256Hash + '\'' +
                '}';
    }
}

