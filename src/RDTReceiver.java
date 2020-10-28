import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RDTReceiver extends TransportLayer {

    public RDTReceiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    private int expectedSeqnum;     //Seqnum receiver is waiting for

    @Override
    public void init() {
        expectedSeqnum = 0;
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

        //Check if received packet is corrupt
        if (isCorrupt(pkt)) {
            System.out.println("Received corrupted packet\n");
            simulator.sendToApplicationLayer(this, pkt.getData());
            //Send NACK to network layer
            TransportLayerPacket ackPkt = makePkt("NACK".getBytes(), expectedSeqnum);
            ackPkt.setAcknum(NAK);
            simulator.sendToNetworkLayer(this, ackPkt);
        } else if(expectedSeqnum != pkt.getSeqnum()){
            //Packet is not corrupt but wrong seqnum received
            System.out.println("Received incorrect seqnum, sending ACK\n");
            System.out.println("Expected " + expectedSeqnum + "got " + pkt.getSeqnum());
            //Send ACK to network layer
            TransportLayerPacket nakPkt = makePkt("ACK".getBytes(),expectedSeqnum);
            nakPkt.setAcknum(ACK);
            simulator.sendToNetworkLayer(this, nakPkt);
        }else{
            //Packet is received ok
            System.out.println("Received packet OK, sending ACK\n");
            System.out.println("Receiver: extract data");
            System.out.println("Receiver: deliver_data");
            //Send data to application layer
            simulator.sendToApplicationLayer(this, data);
            System.out.println("Receiver: Sent " + new String(data, StandardCharsets.UTF_8));
            //Send ACK to network layer
            TransportLayerPacket ackPkt = makePkt("ACK".getBytes(), expectedSeqnum);
            ackPkt.setAcknum(ACK);
            simulator.sendToNetworkLayer(this, ackPkt);
            //Flip expected seqnum
            expectedSeqnum = 1 - expectedSeqnum;
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
