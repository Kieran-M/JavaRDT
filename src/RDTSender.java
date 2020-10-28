import java.util.ArrayList;
import java.util.Arrays;

public class RDTSender extends TransportLayer {

    private Boolean fstPkt;
    private ArrayList<byte[]> dataQ;

    public RDTSender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        fstPkt = true;
        dataQ = new ArrayList<>();
    }

    @Override
    public void rdt_send(byte[] data) {
        dataQ.add(data);
        if (fstPkt) {
            fstPkt = false;
            data = dataQ.get(0);
            TransportLayerPacket pkt = makePkt(data);
            simulator.sendToNetworkLayer(this, pkt);
        }
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        byte[] data;
        int received = pkt.getAcknum();

        if (received < ACK) {
            System.out.println("Sender: NAK received");
            data = dataQ.get(0);
            System.out.println("Sender: Resending packet " + dataQ.get(0) + "\n");
            TransportLayerPacket resendPkt = makePkt(data);
            simulator.sendToNetworkLayer(this, resendPkt);
        } else {
            System.out.println("Sender: ACK received\n");
            dataQ.remove(0);
            if (!dataQ.isEmpty()) {
                data = dataQ.get(0);
                TransportLayerPacket nextPkt = makePkt(data);
                simulator.sendToNetworkLayer(this, nextPkt);
            }
        }
    }

    @Override
    public void timerInterrupt() {

    }


}
