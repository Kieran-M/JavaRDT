public class RDTReceiver extends TransportLayer {

    public RDTReceiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {

    }

    @Override
    public void rdt_send(byte[] data) {

    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        long originalChecksum = pkt.getChecksum();
        long newChecksum = genChecksum(pkt.getData());

        //if checksums match deliver data to application layer and send ACK
        if (newChecksum == originalChecksum) {
            simulator.sendToApplicationLayer(this, pkt.getData());
            TransportLayerPacket ackPkt = makePkt("ACK".getBytes());    //This is just to have data for the network sim to corrupt
            ackPkt.setAcknum(ACK);
            simulator.sendToNetworkLayer(this, ackPkt);
        } else {    //If the received data is corrupt send NAK
            TransportLayerPacket nakPkt = makePkt("NAK".getBytes());
            nakPkt.setAcknum(NAK);
            simulator.sendToNetworkLayer(this, nakPkt);
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
