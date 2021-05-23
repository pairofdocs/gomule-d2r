package gomule.d2s;
import java.io.*;
 
public class HexDump {
    public static void printHex(byte[] b) {
        for (int i = 0; i < b.length; ++i) {
            if (i % 16 == 0) {
                System.out.print (Integer.toHexString ((i & 0xFFFF) | 0x10000).substring(1,5) + " - ");
            }
            System.out.print (Integer.toHexString((b[i]&0xFF) | 0x100).substring(1,3) + " ");
            if (i % 16 == 15 || i == b.length - 1)
            {
                int j;
                for (j = 16 - i % 16; j > 1; --j)
                    System.out.print ("   ");
                System.out.print (" - ");
                int start = (i / 16) * 16;
                int end = (b.length < i + 1) ? b.length : (i + 1);
                for (j = start; j < end; ++j)
                    if (b[j] >= 32 && b[j] <= 126)
                        System.out.print ((char)b[j]);
                    else
                        System.out.print (".");
                System.out.println ();
            }
        }
        System.out.println();
    }
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream (args[0]);
        byte[] bytes = new byte[fis.available()];
        fis.read (bytes);
        printHex (bytes);
        fis.close();
    }
}
