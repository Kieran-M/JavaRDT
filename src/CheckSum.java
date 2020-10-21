import java.util.zip.Adler32;
import java.util.zip.Checksum;

public class CheckSum {

    public long createChecksum(byte[] data){
        byte[] arr = data;
        Checksum checksum = new Adler32();
        checksum.update(arr, 0 ,arr.length);
        long res = checksum.getValue();
        System.out.println(res);
        return res;
    }
}





