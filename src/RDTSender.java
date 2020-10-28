import java.util.ArrayList;
import java.util.Arrays;

public class RDTSender extends TransportLayer {

    Boolean sndNxt;
    ArrayList<byte[]> dataQ;

    public RDTSender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        sndNxt = true;
        dataQ = new ArrayList<>();
    }

    @Override
    public void rdt_send(byte[] data) {
        if (!dataQ.contains(data)) {
            dataQ.add(data);
        }
        if (sndNxt) {
            sndNxt = false;
            System.out.println("Sending: " + dataQ.get(0));
            TransportLayerPacket pkt = new TransportLayerPacket(dataQ.get(0));
            simulator.sendToNetworkLayer(this, pkt);
        }
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        sndNxt = true;
        dataQ.remove(0);
        if (!dataQ.isEmpty()) {
            rdt_send(dataQ.get(0));
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
