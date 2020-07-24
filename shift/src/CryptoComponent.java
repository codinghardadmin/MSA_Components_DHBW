import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CryptoComponent {
	
	public static void main(String[] args) {
		CryptoComponent cipher = new CryptoComponent();
		String encrypted = cipher._encrypt("test", new File("shift.json"));
		System.out.println("Encrypted = " + encrypted);
		String decrypted = cipher._decrypt(encrypted, new File("shift.json"));
		System.out.println("Decrypted = " + decrypted);
	}

	private int key;

	public String _encrypt(String plainText, File keyfile) {
		loadKeyFromFile(keyfile);
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < plainText.length(); i++) {
			char character = (char) (plainText.codePointAt(i) + key);
			stringBuilder.append(character);
		}

		return stringBuilder.toString();
	}

	public String _decrypt(String cipherText, File keyfile) {
		loadKeyFromFile(keyfile);
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < cipherText.length(); i++) {
			char character = (char) (cipherText.codePointAt(i) - key);
			stringBuilder.append(character);
		}

		return stringBuilder.toString();
	}
	
	private void loadKeyFromFile(File keyfile) {
		try {
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(keyfile));
			StringBuilder builder = new StringBuilder();
			
			String str;
			while((str = reader.readLine()) != null) {
				builder.append(str);
			}
			reader.close();
			
			String json = builder.toString();
			
			json = json.replace("{", "").replace("}", "");
			String[] array = json.split(",");
			
			for (String s : array) {
				if (!s.contains("\"key\":")) {
					continue;
				}
				
				try {
					this.key = Integer.parseInt(s.trim().split(":")[1].replace("\"", "").replace(",", "").trim());
					// System.out.println("Datei erfolgreich geladen!");
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static CryptoComponent instance = new CryptoComponent();

	public static CryptoComponent getInstance() {
		return instance;
	}

	public Port port;

	public class Port implements ICryptographic {

		@Override
		public String encrypt(String plainMessage, File keyfile) {
			return _encrypt(plainMessage, keyfile);
		}

		@Override
		public String decrypt(String encryptedMessage, File keyfile) {
			return _decrypt(encryptedMessage, keyfile);
		}

	}

	private CryptoComponent() {
    	port = new Port();
    }
}
