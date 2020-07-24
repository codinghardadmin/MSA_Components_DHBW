import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;

public class Application {
	public static void main(String... args) {
        String message = "Iik=";
    	
    	byte[] cipher = Base64.getDecoder().decode(message);
    	
    	BigInteger n_e[] = loadN_EFromFile(new File("rsa_cracker.json"));
    	
        BigInteger n = n_e[0];
        BigInteger e = n_e[1];
        
        //n = new BigInteger("42593");
        //e = new BigInteger("42173");
    	
        BigInteger bcipher = new BigInteger(cipher);

        RSACracker rsaCracker = new RSACracker(e, n, bcipher);

        try {
            String plainMessage = new String(rsaCracker.execute().toByteArray());
            System.out.println("plainMessage : " + plainMessage);
        } catch (RSACrackerException rsae) {
            System.out.println(rsae.getMessage());
        }
    }

	private static BigInteger[] loadN_EFromFile(File keyfile) {
		BigInteger n = null;
		BigInteger e = null;

		try {
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(keyfile));
			StringBuilder builder = new StringBuilder();

			String str;
			while ((str = reader.readLine()) != null) {
				builder.append(str);
			}
			reader.close();

			String json = builder.toString();

			json = json.replace("{", "").replace("}", "");
			String[] array = json.split(",");

			for (String s : array) {
				if (s.contains("\"n\":"))
					n = new BigInteger(s.trim().split(":")[1].replace("\"", "").replace(",", "").trim());
				if (s.contains("\"e\":"))
					e = new BigInteger(s.trim().split(":")[1].replace("\"", "").replace(",", "").trim());
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		BigInteger[] n_e = { n, e };

		return n_e;
	}
}