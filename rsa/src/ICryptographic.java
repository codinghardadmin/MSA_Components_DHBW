import java.io.File;

public interface ICryptographic {
	String encrypt(String plainMessage, File keyfile);

	String decrypt(String encryptedMessage, File keyfile);
}
