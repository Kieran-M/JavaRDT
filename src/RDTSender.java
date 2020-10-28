import java.util.ArrayList;
import java.util.Arrays;

public class RDTSender extends TransportLayer {

    private Boolean fstPkt;
    private ArrayList<byte[]> dataQ; //Queue of packets to be sent
    private int seqnum;              //Current seqnum to be sent
    public RDTSender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        fstPkt = true;
        dataQ = new ArrayList<>();
        seqnum = 0;
    }

    @Override
    public void rdt_send(byte[] data) {
        //Add packet to queue
        dataQ.add(data);
        //Check if ready to send to network
        if (fstPkt) {
            fstPkt = false;
            TransportLayerPacket pkt = makePkt(dataQ.get(0).clone(),this.seqnum);
            simulator.sendToNetworkLayer(this, pkt);
        }
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {

        byte[] data = dataQ.get(0).clone();
        int received = pkt.getAcknum();

        System.out.println("Sender: rdt_receive call");
        //Check packet for corruption
        if(isCorrupt(pkt)){
            //If packet is corrupt then resend packet
            System.out.println("Sender: received packet is corrupt");
            System.out.println("Sender: Resending packet " + data + "\n");
            TransportLayerPacket resendPkt = makePkt(dataQ.get(0).clone(),this.seqnum);
            simulator.sendToNetworkLayer(this, resendPkt);
        }else if (received < ACK) {
            //Check for NAK and resend packet
            System.out.println("Sender: NAK received");
            System.out.println("Sender: Resending packet " + data + "\n");
            TransportLayerPacket resendPkt = makePkt(dataQ.get(0).clone(),this.seqnum);
            simulator.sendToNetworkLayer(this, resendPkt);
        } else {
            //Packet is received ok
            System.out.println("Sender: ACK received\n");
            //Remove packet from queue and get next packet
            dataQ.remove(0);
            if(!dataQ.isEmpty()) {
                TransportLayerPacket nextPkt = makePkt(dataQ.get(0).clone(),this.seqnum);
                simulator.sendToNetworkLayer(this, nextPkt);
            }
            //Flip seqnum
            this.seqnum = 1 - seqnum;

        }
    }

    @Override
    public void timerInterrupt() {

    }


}
