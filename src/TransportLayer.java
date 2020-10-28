import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public abstract class TransportLayer {

    int NAK = 0;
    int ACK = 1;
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

    public long genChecksum(byte[] data) {
        Checksum checksum = new CRC32();
        checksum.update(data);
        return checksum.getValue();
    }

    public TransportLayerPacket makePkt(byte[] data) {
        long checksum = genChecksum(data);
        System.out.println("Creating packet data: " + Arrays.toString(data) + " checksum: " + checksum + "\n");
        return new TransportLayerPacket(data, checksum);
    }
}
