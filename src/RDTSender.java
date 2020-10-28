import java.util.ArrayList;

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
        //Add data to be sent to a list
        dataQ.add(data);

        //If no packets have been sent yet send the first one to start off
        if (fstPkt) {
            fstPkt = false;
            TransportLayerPacket pkt = makePkt(dataQ.get(0).clone());
            simulator.sendToNetworkLayer(this, pkt);
        }
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        //If NAK or corrupted ACK is received resend the packet
        if (pkt.getAcknum() < ACK) {
            TransportLayerPacket resendPkt = makePkt(dataQ.get(0).clone()); //Create a packet with the current data
            simulator.sendToNetworkLayer(this, resendPkt);
        } else {  //ACK received
            dataQ.remove(0);    //Remove the previously sent data from the list
            if (!dataQ.isEmpty()) {
                TransportLayerPacket nextPkt = makePkt(dataQ.get(0).clone());
                simulator.sendToNetworkLayer(this, nextPkt);
            }
        }
    }

    @Override
    public void timerInterrupt() {

    }


}
