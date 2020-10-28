import java.util.zip.CRC32;

public class Receiver extends TransportLayer {

    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {

    }

    @Override
    public void rdt_send(byte[] data) {
        System.out.println("Receiver: rdt_send call");
        long checksum = createChecksum(data);
        TransportLayerPacket sndpkt = new TransportLayerPacket(data,checksum);
        System.out.println("Receiver: udt_send");
        udt_send(sndpkt);
    }

    private void udt_send(TransportLayerPacket pkt) {
        simulator.sendToNetworkLayer(this, pkt);
    }

    @Override
    public void rdt_receive(TransportLayerPacket rcvpkt) {
        System.out.println("Receiver: rdt_rcv call");
        if (isCorrupt(rcvpkt)) {
            System.out.println("Receiver: data corrupt udt_send(NAK)");
            rdt_send("NAK".getBytes());
        } else {
            System.out.println("Receiver: extract data");
            byte[] data = rcvpkt.getData();
            System.out.println("Receiver: deliver_data");
            simulator.sendToApplicationLayer(this, data);
            rdt_send("ACK".getBytes());
        }
    }

    private boolean isCorrupt(TransportLayerPacket rcvpkt) {
        long receivedChecksum = createChecksum(rcvpkt.data);
        return receivedChecksum != rcvpkt.checksum;
    }

    @Override
    public void timerInterrupt() {

    }
}
