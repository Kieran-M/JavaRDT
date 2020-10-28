import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CRC32;

public class Sender extends TransportLayer {

    boolean sndNxt;
    boolean fstPkt;
    ArrayList<TransportLayerPacket> pktQ;

    public Sender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        sndNxt = true;
        fstPkt = true;
        pktQ = new ArrayList<>();
    }

    @Override
    public void rdt_send(byte[] data) {
        System.out.println("Sender: rdt_send call");
        long checksum = createChecksum(data);
        TransportLayerPacket sndpkt = make_pkt(data, checksum);
        System.out.println("Sender: Adding packet to Q");
        pktQ.add(sndpkt);
        if (fstPkt) {
            System.out.println("Sender: udt_send");
            fstPkt = false;
            udt_send(pktQ.get(0));
        }
    }

    private void udt_send(TransportLayerPacket pkt) {
        simulator.sendToNetworkLayer(this, pkt);
    }

    private TransportLayerPacket make_pkt(byte[] data, long checksum) {
        System.out.println("Sender: make_pkt");
        return new TransportLayerPacket(data, checksum);
    }

    @Override
    public void rdt_receive(TransportLayerPacket rcvpkt) {
        System.out.println("Sender: rdt_receive call");
        if (isNAK(rcvpkt)) {
            System.out.println("Sender: received NAK");
            udt_send(pktQ.get(0));
        } else {
            System.out.println("Sender: received ACK");
            pktQ.remove(0);
            if (!pktQ.isEmpty()) {
                udt_send(pktQ.get(0));
            }
        }
    }

    private boolean isNAK(TransportLayerPacket rcvpkt) {
        return !Arrays.equals(rcvpkt.data, "ACK".getBytes());
    }

    @Override
    public void timerInterrupt() {

    }
}
