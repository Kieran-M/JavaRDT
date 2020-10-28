import java.util.Arrays;

public class RDTReceiver extends TransportLayer {

    public RDTReceiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {

    }

    @Override
    public void rdt_send(byte[] data) {

    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        long originalChecksum = pkt.getChecksum();
        byte[] data = pkt.getData();

        System.out.println("Received: " + Arrays.toString(data) + " with checksum: " + originalChecksum);
        long newChecksum = genChecksum(data);
        System.out.println("New Checksum: " + newChecksum + "\n ");

        if (newChecksum == originalChecksum) {
            System.out.println("Received ACK\n");
            simulator.sendToApplicationLayer(this, pkt.getData());
            TransportLayerPacket ackPkt = makePkt("ACK".getBytes());
            ackPkt.setAcknum(ACK);
            simulator.sendToNetworkLayer(this, ackPkt);
        } else {
            System.out.println("Received NAK\n");
            TransportLayerPacket nakPkt = makePkt("NAK".getBytes());
            nakPkt.setAcknum(NAK);
            simulator.sendToNetworkLayer(this, nakPkt);
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
