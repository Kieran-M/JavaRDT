public class RDTReceiver extends TransportLayer {

    int expectedSeqnum;

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
        System.out.println("Receiver: Receiving packet\n");
        long originalChecksum = pkt.getChecksum();
        long newChecksum = genChecksum(pkt.getData());
        System.out.println("Receiver: New Checksum: " + newChecksum + "\nOriginal Checksum: " + pkt.getChecksum() + "\n");

        if (newChecksum != originalChecksum) {
            System.out.println("Receiver: Data corrupted - making packet");
            TransportLayerPacket nakPkt = makePkt(expectedSeqnum, NAK);
            System.out.println("Receiver: Sending NAK to network layer\n");
            simulator.sendToNetworkLayer(this, nakPkt);
        } else {
            System.out.println("Receiver: expectedSeqnum: " + expectedSeqnum + "\nreceived seqnum: " + pkt.getSeqnum() + "\n");
            if (pkt.getSeqnum() != expectedSeqnum) {
                System.out.println("Receiver: Wrong seqnum sending ACK not delivering to application layer");
                TransportLayerPacket ackPkt = makePkt(expectedSeqnum, ACK);
                System.out.println("Receiver: Sending ACK to network layer\n");
                simulator.sendToNetworkLayer(this, ackPkt);
            } else {
                System.out.println("Receiver: data and seqnum are ok\n");
                expectedSeqnum ^= 1;
                System.out.println("Receiver: new expectedSeqnum: " + expectedSeqnum);
                System.out.println("Receiver: sending to application layer");
                simulator.sendToApplicationLayer(this, pkt.getData());

                System.out.println("Receiver: making ACK packet");
                TransportLayerPacket ackPkt = makePkt(expectedSeqnum, ACK);
                System.out.println("Receiver: Sending ACK to network layer\n");
                simulator.sendToNetworkLayer(this, ackPkt);
            }
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
