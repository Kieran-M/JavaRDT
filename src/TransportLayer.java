import java.util.zip.CRC32;
import java.util.zip.Checksum;

public abstract class TransportLayer {

    String name;
    NetworkSimulator simulator;

    public TransportLayer(String name, NetworkSimulator simulator) {
        this.name = name;
        this.simulator = simulator;
    }

    public abstract void init();

    public abstract void rdt_send(byte[] data);

    public abstract void rdt_receive(TransportLayerPacket pkt);

    public abstract void timerInterrupt();

    public String getName() {
        return this.name;
    }

    public long createChecksum(byte[] data){
        //
        byte[] arr = data;
        Checksum checksum = new CRC32();
        checksum.update(arr, 0 ,arr.length);
        long res = checksum.getValue();
        return res;
    }
}
