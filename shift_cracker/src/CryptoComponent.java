import java.io.File;
import java.text.DecimalFormat;

/*
 * Author: 6439456
 */

public class CryptoComponent {
    private static DecimalFormat decimalFormat = new DecimalFormat("#0.00000");

    private double maxFrequency;
    private StringBuilder decryptedMessageBuilder;

    private static CryptoComponent instance = new CryptoComponent();

    public Port port;

    private CryptoComponent() {
        port = new Port();
    }

    public static CryptoComponent getInstance() {
        return instance;
    }

    public class Port implements ICryptographicCracker {
        public String decrypt(String encryptedMessage, File file) {
            return decryptMessage(encryptedMessage);
        }
    }

    public String decryptMessage(String encryptedMessage) {
        String source = encryptedMessage.trim();
        if (source.equals("")) {
            return "";
        }

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

        maxFrequency = 0.0;

        decryptedMessageBuilder = new StringBuilder();

        for (int shift = 1; shift <= 25; shift++) {
            smartShift(shift, unicode, unicodeCopy);
        }

        String decryptedMessage = decryptedMessageBuilder.toString();

        return decryptedMessage;
    }

    private void smartShift(int shift, int[] unicode, int[] unicodeCopy) {
        for (int x = 0; x <= unicode.length - 1; x++) {
            unicodeCopy[x] = unicode[x];

            // Upper letters
            if (unicode[x] >= 65 && unicode[x] <= 90) {
                unicodeCopy[x] += shift;
                if (unicodeCopy[x] > 90) {
                    unicodeCopy[x] -= 26;
                }
            }
            // Lower letters
            else if (unicode[x] >= 97 && unicode[x] <= 122) {
                unicodeCopy[x] += shift;
                if (unicodeCopy[x] > 122) {
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

        double frequency = 0;
        double aFrequency = 0;
        double eFrequency = 0;
        double iFrequency = 0;
        double oFrequency = 0;
        double uFrequency = 0;

        for (char c : finalProcess) {
            frequency++;

            switch (c) {
                case 'A': case 'a':
                    aFrequency++;
                    break;
                case 'E': case 'e':
                    eFrequency++;
                    break;
                case 'I': case 'i':
                    iFrequency++;
                    break;
                case 'O': case 'o':
                    oFrequency++;
                    break;
                case 'U': case 'u':
                    uFrequency++;
                    break;
                default:
                    break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (char character : finalProcess) {
            stringBuilder.append(character);
        }



        if (eFrequency / frequency >= 0.05 || aFrequency / frequency >= 0.05 || iFrequency / frequency >= 0.05 || oFrequency / frequency >= 0.05 || uFrequency / frequency >= 0.05) {
            //System.out.println();
            //System.out.println("\t" + stringBuilder);
            /*System.out.println("\t\tA : " + decimalFormat.format(aFrequency / frequency));
            System.out.println("\t\tE : " + decimalFormat.format(eFrequency / frequency));
            System.out.println("\t\tI : " + decimalFormat.format(iFrequency / frequency));
            System.out.println("\t\tO : " + decimalFormat.format(oFrequency / frequency));
            System.out.println("\t\tU : " + decimalFormat.format(uFrequency / frequency));*/
            decryptedMessageBuilder.append(stringBuilder.toString());
        }
    }
}