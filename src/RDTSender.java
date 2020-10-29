import java.util.ArrayList;
import java.util.Arrays;

public class RDTSender extends TransportLayer {

    private int seqnum;                 //The current seqnum to be used
    private int lastAckdPacket;         //The seqnum of the last ACk'd packet
    private boolean fstPkt;             //Flag for first packet to be sent
    private ArrayList<byte[]> dataQ;    //Arraylist holding the queue of packets to be sent

    public RDTSender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        seqnum = 0;                 //Send seqnum 0 to begin with
        lastAckdPacket = 1;         //No previously ack'd packets for first receive so set to 1
        fstPkt = true;              //First packet flag
        dataQ = new ArrayList<>();  //Initialise packet queue
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
        System.out.println("Sender: Waiting for ack + seqnum " + seqnum);
        //If packet is corrupt OR receive duplicate ACK, resend the packet
        if (isCorrupt(pkt) || lastAckdPacket == pkt.getSeqnum()) {
            System.out.println("Sender: Packet corrupted or has wrong seqnum\n");
            System.out.println("Sender: Making resend packet");
            TransportLayerPacket resendPkt = makePkt(seqnum, dataQ.get(0).clone()); //Create a packet with the current data
            System.out.println("Sender: Resending packet to network layer with seqnum: " + seqnum + "\n");
            simulator.sendToNetworkLayer(this, resendPkt);
        } else {
            //ACK is received with correct Seqnum
            System.out.println("Sender: ACK received");
            dataQ.remove(0);    //Remove the previously sent data from the list
            lastAckdPacket = pkt.getSeqnum();   //Set last ACK'd packet seqnum
            System.out.println("Sender: new seqnum: " + seqnum + "\n");
            seqnum = 1 - seqnum; //Flips the seqnum
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
