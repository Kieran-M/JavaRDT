import java.util.zip.CRC32;
import java.util.zip.Checksum;

public abstract class TransportLayer {


    byte[] ACK = "ACK".getBytes();
    String name;
    NetworkSimulator simulator;
    Checksum checksum = new CRC32(); //Create checksums using CRC32 algorithm
    protected static int TIMEOUT = 500; //Timer timeout value

    public TransportLayer(String name, NetworkSimulator simulator) {
        this.name = name;
        this.simulator = simulator;
    }

    public abstract void init(
    );

    public abstract void rdt_send(byte[] data);

    public abstract void rdt_receive(TransportLayerPacket pkt);

    public abstract void timerInterrupt();

    public String getName() {
        return this.name;
    }

    public long genChecksum(byte[] data) {
        //Reset checksum value
        checksum.reset();
        //Generate new checksum
        checksum.update(data);
        return checksum.getValue();
    }

    public boolean isCorrupt(TransportLayerPacket pkt){
        long packetChecksum = pkt.getChecksum();
        return (packetChecksum != genChecksum(pkt.getData()));
    }

    public TransportLayerPacket makePkt(int seqnum, byte[] data) {
        long checksum = genChecksum(data.clone());
        return new TransportLayerPacket(seqnum, data, checksum);
    }
}
