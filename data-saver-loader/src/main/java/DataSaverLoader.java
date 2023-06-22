import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;


public class DataSaverLoader {

    private String extension;
    private byte[] file;

    private DataSaverLoader() {}

    public static DataSaverLoader loadEncrypted(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        DataSaverLoader dataSaverLoader = new DataSaverLoader();
        dataSaverLoader.extension = new String(Arrays.copyOfRange(bytes, 1, bytes[0] + 1));
        dataSaverLoader.file = Arrays.copyOfRange(bytes, 1 + bytes[0], bytes.length);
        return dataSaverLoader;
    }

    public static void saveEncrypted(String name, String path, byte[] bytes) throws IOException {
        byte[] extensionBytes = FilenameUtils.getExtension(name).getBytes();
        String baseName = FilenameUtils.getBaseName(name);
        FileOutputStream fos = new FileOutputStream(path + "/" + baseName);
        fos.write((byte)extensionBytes.length);
        fos.write(extensionBytes);
        fos.write(bytes);
        fos.close();
    }

    public static void save(String name, String path, byte[] bytes) throws IOException {
        FileOutputStream fos = new FileOutputStream(path + "/" + name);
        fos.write(bytes);
        fos.close();
    }

    public static byte[] load(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getFile() {
        return file;
    }
}
