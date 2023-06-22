import org.apache.commons.crypto.cipher.CryptoCipher;
import org.apache.commons.crypto.cipher.CryptoCipherFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

public class FileEncryptorDecryptor {

    private File source;
    private String key;
    private boolean isEncrypted;
    private byte[] loadedFile;
    private String extension = "";

    public FileEncryptorDecryptor(File source, String key, boolean isEncrypted) {
        this.source = source;
        this.key = key;
        this.isEncrypted = isEncrypted;
    }

    public void encrypt() {
        if(this.isEncrypted) {
            return;
        }
        try {
            SecretKeySpec key = new SecretKeySpec(this.key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
            CryptoCipher cipher = CryptoCipherFactory.getCryptoCipher("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] bytes = DataSaverLoader.load(source);
            this.loadedFile = new byte[bytes.length];
            cipher.doFinal(bytes, 0, bytes.length, this.loadedFile, 0);
            cipher.close();
            this.isEncrypted = true;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decrypt() {
        if(!this.isEncrypted) {
            return;
        }
        try {
            SecretKeySpec key = new SecretKeySpec(this.key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);
            CryptoCipher cipher = CryptoCipherFactory.getCryptoCipher("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            DataSaverLoader dsl = DataSaverLoader.loadEncrypted(source);
            this.extension = dsl.getExtension();
            byte[] bytes = dsl.getFile();
            this.loadedFile = new byte[bytes.length];
            cipher.doFinal(bytes, 0, bytes.length, this.loadedFile, 0);
            cipher.close();
            this.isEncrypted = false;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(File target) throws IOException {
        if(this.isEncrypted) {
            DataSaverLoader.saveEncrypted(this.source.getName(), target.getPath(), this.loadedFile);
        }
        else {
            DataSaverLoader.save(this.source.getName() + "." + this.extension, target.getPath(), this.loadedFile);
        }
    }

}
