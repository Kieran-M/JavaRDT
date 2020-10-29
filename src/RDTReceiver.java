import java.nio.charset.StandardCharsets;

public class RDTReceiver extends TransportLayer {

    private int expectedSeqnum;     //Seqnum currently expecting
    private int prevSeqnum;         //The last correctly received packet's seqnum

    public RDTReceiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        expectedSeqnum = 0;         //Expecting 0 fro first packet
        prevSeqnum = 0;             //No previous packets so set to 0
    }

    @Override
    public void rdt_send(byte[] data) {

    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        System.out.println("Receiver: Receiving packet\n");
        System.out.println("Receiver: expecting seqnum " + expectedSeqnum + "\n");
        System.out.println("Receiver: Got seqnum " + pkt.getSeqnum() + "\n");
        //Check if packet is not corrupt && has correct seqnum
        if (!isCorrupt(pkt) && expectedSeqnum == pkt.getSeqnum()){
            System.out.println("Receiver: data and seqnum are ok\n");
            sendAck(expectedSeqnum);
            System.out.println("Receiver: sending to application layer\n");
            simulator.sendToApplicationLayer(this, pkt.getData());
            System.out.println("Receiver: Sent " + new String(pkt.getData(), StandardCharsets.UTF_8) + "\n");
            prevSeqnum = pkt.getSeqnum();
            expectedSeqnum = 1 - expectedSeqnum;
            System.out.println("Receiver: new expected seqnum: " + expectedSeqnum + "\n");
        }else{
            //Send back ACK of last received packet
            System.out.println("Receiver: Packet data corrupted or has wrong seqnum- making packet");
            sendAck(prevSeqnum);
        }
    }

    //Send ACK packet with given seqnum
    public void sendAck(int seq) {
        System.out.println("Receiver: Making ACK packet");
        TransportLayerPacket ackPkt = makePkt(seq, ACK);
        System.out.println("Receiver: Sending ACK + Seqnum " + seq + " to network layer\n");
        simulator.sendToNetworkLayer(this, ackPkt);
    }

    @Override
    public void timerInterrupt() {

    }
}
