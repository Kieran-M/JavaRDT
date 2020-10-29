import java.util.ArrayList;
import java.util.Arrays;

public class RDTSender extends TransportLayer {

    private int seqnum;
    private Boolean fstPkt;
    private ArrayList<byte[]> dataQ; //Queue of packets to be sent
    private int seqnum;              //Current seqnum to be sent
    public RDTSender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        seqnum = 0;
        fstPkt = true;
        dataQ = new ArrayList<>();
        seqnum = 0;
    }

    @Override
    public void rdt_send(byte[] data) {
        System.out.println("Sender: Adding data to queue\n");
        //Add data to be sent to a list
        dataQ.add(data);

        //If no packets have been sent yet send the first one to start off
        if (fstPkt) {
            fstPkt = false;
            System.out.println("Sender: Making packet");
            TransportLayerPacket pkt = makePkt(seqnum, dataQ.get(0).clone());
            System.out.println("Sender: Sending to network layer\n");
            simulator.sendToNetworkLayer(this, pkt);
        }
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        System.out.println("Sender: Receiving packet\n");
        long newChecksum = genChecksum(pkt.getData());
        System.out.println("Sender: New Checksum: " + newChecksum + "\nOriginal Checksum: " + pkt.getChecksum() + "\n");
        //If NAK or corrupted ACK is received resend the packet
        if (newChecksum != pkt.getChecksum()) {
            System.out.println("Sender: corrupted ACK received\n");
            System.out.println("Sender: Making resend packet");
            TransportLayerPacket resendPkt = makePkt(seqnum, dataQ.get(0).clone()); //Create a packet with the current data
            System.out.println("Sender: Resending packet to network layer\n");
            simulator.sendToNetworkLayer(this, resendPkt);

        } else if (Arrays.equals(pkt.getData(), NAK)) {
            System.out.println("Sender: NAK received\n");
            System.out.println("Sender: Making resend packet");
            TransportLayerPacket resendPkt = makePkt(seqnum, dataQ.get(0).clone()); //Create a packet with the current data
            System.out.println("Sender: Resending packet to network layer\n");
            simulator.sendToNetworkLayer(this, resendPkt);
        }
        else {  //ACK received
            System.out.println("Sender: ACK received");
            dataQ.remove(0);    //Remove the previously sent data from the list
            seqnum ^= 1;    //seqnum XOR 1. Flips the seqnum
            System.out.println("Sender: new seqnum: " + seqnum + "\n");

            if (!dataQ.isEmpty()) {
                System.out.println("Sender: Making packet");
                TransportLayerPacket nextPkt = makePkt(seqnum, dataQ.get(0).clone());
                System.out.println("Sender: Sending to network layer\n");
                simulator.sendToNetworkLayer(this, nextPkt);
            }
            System.out.println("Sender: FINISHED");
        }
    }

    @Override
    public void timerInterrupt() {

    }


}
