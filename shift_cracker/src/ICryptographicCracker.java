import java.io.File;

public interface ICryptographicCracker {
	String decrypt(String encryptedMessage, File keyfile);
}
