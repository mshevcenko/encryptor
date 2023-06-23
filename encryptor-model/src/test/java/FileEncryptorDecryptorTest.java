import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class FileEncryptorDecryptorTest {

    @Test
    public void encryptTest() {
        File file = new File("src/main/resources/testFile.txt");
        Assert.assertFalse(!file.exists());
    }

}
