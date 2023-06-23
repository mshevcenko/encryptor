import org.junit.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;


public class FileEncryptorDecryptorTest {

    @Test
    public void encryptTest() throws IOException {
        File file = new File("src/main/resources/testFile.txt");
        byte[] bytes = DataSaverLoader.load(file);
        FileEncryptorDecryptor fed = new FileEncryptorDecryptor(file, "1234567890123456", false);
        fed.encrypt();
        fed.save(new File("src/main/resources"));
        FileEncryptorDecryptor fed2 = new FileEncryptorDecryptor(new File("src/main/resources/testFile"), "1234567890123456", true);
        fed2.decrypt();
        assertArrayEquals(bytes, fed2.getLoadedFile());
    }

}
