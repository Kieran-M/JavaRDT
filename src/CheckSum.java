import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class CheckSum {

    public long createChecksum(byte[] data){
        //
        byte[] arr = data;
        Checksum checksum = new CRC32();
        checksum.update(arr, 0 ,arr.length);
        long res = checksum.getValue();
        return res;
    }
}





