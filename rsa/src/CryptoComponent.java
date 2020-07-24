import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;

public class CryptoComponent {
	
	public static void main(String[] args) {
		String plainMessage = "morpheus";
        RSA rsa = null;
        
        while(rsa == null) {
        	try {
        		rsa = new RSA(8);
        	} catch (ArithmeticException ex) {
        		System.out.println("ArithmeticException catched!");
        	}
        }

        System.out.println("p                 : " + rsa.getP());
        System.out.println("q                 : " + rsa.getQ());
        System.out.println("n                 : " + rsa.getN());
        System.out.println("t                 : " + rsa.getT());
        System.out.println("e                 : " + rsa.getE());
        System.out.println("d                 : " + rsa.getD());
        System.out.println("isCoPrime e and t : " + rsa.isCoPrime(rsa.getE(), rsa.getT()));

        Cipher cipher = new Cipher();
        byte[] encryptedMessage = cipher.encrypt(plainMessage, rsa.getPublicKey());
        String decryptedMessage = cipher.decrypt(encryptedMessage, rsa.getPrivateKey());

        System.out.println("plainMessage      : " + plainMessage);
        System.out.println("encryptedMessage  : " + Base64.getEncoder().encodeToString(encryptedMessage));
        System.out.println("decryptedMessage  : " + decryptedMessage);

        //assertEquals(plainMessage, decryptedMessage);

        encryptedMessage = cipher.encrypt(plainMessage, rsa.getPrivateKey());
        decryptedMessage = cipher.decrypt(encryptedMessage, rsa.getPublicKey());

        //assertEquals(plainMessage, decryptedMessage);
        
        System.err.println("======================");
        System.err.println("plain: " + plainMessage);
        String enc = Base64.getEncoder().encodeToString(cipher.encrypt(plainMessage, rsa.getPublicKey()));
        System.err.println("enc: " + enc);
        String dec = cipher.decrypt(Base64.getDecoder().decode(enc), rsa.getPrivateKey());
        System.err.println("dec: " + dec);
        
		saveKeysToFile(new File("rsa.json"), rsa.getPublicKey(), rsa.getPrivateKey());
        
		Cipher cipher2 = new Cipher();
		
		Key[] keys = loadKeysFromFile(new File("rsa.json"));
		
		System.out.println("# N Orig: " + rsa.getPrivateKey().getN());
		System.out.println("# N Load: " + keys[1].getN());
		
		String plainMessage2 = "morpheus";
		System.err.println("======================");
        System.err.println("plain: " + plainMessage2);
        String enc2 = Base64.getEncoder().encodeToString(cipher2.encrypt(plainMessage2, keys[0]));
        System.err.println("enc: " + enc2);
        String dec2 = cipher2.decrypt(Base64.getDecoder().decode(enc2), keys[1]);
        System.err.println("dec: " + dec2);
        
        System.out.println("========================================== FINAL");
        String temp = CryptoComponent.getInstance()._encrypt("a", new File("rsa.json"));
        System.out.println(temp);
        temp = CryptoComponent.getInstance()._decrypt(temp, new File("rsa.json"));
        System.out.println(temp);
	}

	public String _encrypt(String plainText, File keyfile) {
		Cipher cipher = new Cipher();
		Key[] keys = loadKeysFromFile(keyfile);
		
        return Base64.getEncoder().encodeToString(cipher.encrypt(plainText, keys[0]));
		
	}

	public String _decrypt(String cipherText, File keyfile) {
		Cipher cipher = new Cipher();
		Key[] keys = loadKeysFromFile(keyfile);
		
        return cipher.decrypt(Base64.getDecoder().decode(cipherText), keys[1]);
	}
	
	private static Key[] loadKeysFromFile(File keyfile) {
		BigInteger n = null;
		BigInteger e = null;
		BigInteger d = null;
		
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
				if (s.contains("\"n\":")) n = new BigInteger(s.trim().split(":")[1].replace("\"", "").replace(",", "").trim());
				if (s.contains("\"e\":")) e = new BigInteger(s.trim().split(":")[1].replace("\"", "").replace(",", "").trim());
				if (s.contains("\"d\":")) d = new BigInteger(s.trim().split(":")[1].replace("\"", "").replace(",", "").trim());
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		Key publicKey = new Key(n, e);
		Key privateKey = new Key(n, d);
		
		Key[] keys = {publicKey, privateKey};
		
		return keys;
	}
	
	private static void saveKeysToFile(File keyfile, Key publicKey, Key privateKey) {
		if (publicKey.getN().equals(privateKey.getN())) {
			System.out.println("Equal!");
		}
		
		try {
			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(keyfile));
			
			System.out.println("SAVE NOW N FROM PUBLIC KEY: " + publicKey.getN());
			
			writer.write("{");
			writer.newLine();
			writer.write("\t" + "\"n\": \"" + publicKey.getN() + "\",");
			writer.newLine();
			writer.write("\t" + "\"e\": \"" + publicKey.getE() + "\",");
			writer.newLine();
			writer.write("\t" + "\"d\": \"" + privateKey.getE() + "\"");
			writer.newLine();
			writer.write("}");
			writer.close();
			
			
		} catch (IOException ex) {
			ex.printStackTrace();
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
