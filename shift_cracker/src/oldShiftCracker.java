import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class oldShiftCracker {
    // private static DecimalFormat decimalFormat = new DecimalFormat("#0.00000");
	
	public static void main(String[] args) {
		System.out.println(_decrypt("Sjiroevxsjjip"));
	}

    public static String _decrypt(String msg) {
        String source = msg.trim().toUpperCase();

        char[] sourceText = new char[source.length()];
        int[] unicode = new int[source.length()];
        int[] unicodeCopy = new int[source.length()];

        for (int count = 0; count < source.length(); count++) {
            sourceText[count] = source.charAt(count);
        }

        String hex;
        int dec;

        for (int count = 0; count < sourceText.length; count++) {
            hex = Integer.toHexString(sourceText[count]);
            dec = Integer.parseInt(hex, 16);
            unicode[count] = dec;
            unicodeCopy[count] = dec;
        }
        
        List<String> list = new ArrayList<String>();

        for (int shift = 1; shift <= 25; shift++) {
            list.add(smartShift(shift, unicode, unicodeCopy));
        }
        
        return String.join(", ", list);
    }

    private static String smartShift(int shift, int[] unicode, int[] unicodeCopy) {
        for (int x = 0; x <= unicode.length - 1; x++) {
            unicodeCopy[x] = unicode[x];

            if (unicode[x] >= 65 && unicode[x] <= 90) {
                unicodeCopy[x] += shift;
                if (unicodeCopy[x] > 90) {
                    unicodeCopy[x] -= 26;
                }
            }
        }

        String[] processed = new String[unicode.length];
        char[] finalProcess = new char[unicode.length];

        for (int count = 0; count < processed.length; count++) {
            processed[count] = Integer.toHexString(unicodeCopy[count]);
            int hexToInt = Integer.parseInt(processed[count], 16);
            char intToChar = (char) hexToInt;
            finalProcess[count] = intToChar;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (char character : finalProcess) {
            stringBuilder.append(character);
        }
        
        return stringBuilder.toString();
    }
    
    private static oldShiftCracker instance = new oldShiftCracker();

	public static oldShiftCracker getInstance() {
		return instance;
	}

	public Port port;

	public class Port implements ICryptographicCracker {

		@Override
		public String decrypt(String encryptedMessage, File keyfile) {
			return _decrypt(encryptedMessage);
		}
	}

	private oldShiftCracker() {
    	port = new Port();
    }
}