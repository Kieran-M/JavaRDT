public class Receiver extends TransportLayer {


    public Receiver(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }


    byte[] data;

    @Override
    public void init() {

    }

    @Override
    public void rdt_send(byte[] data) {
        TransportLayerPacket pkt = new TransportLayerPacket(data);
        simulator.sendToNetworkLayer(this, pkt);
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        if(isCorrupt(pkt) == true){
            //if data is corrupt send NAK
            TransportLayerPacket ackPkt = new TransportLayerPacket(new byte[1]);
            pkt.setAcknum(0);
            System.out.println("Packet is corrupt, resending");
            simulator.sendToNetworkLayer(this,ackPkt);
        }else{
            //Send ACK
            TransportLayerPacket ackPkt = new TransportLayerPacket(new byte[1]);
            pkt.setAcknum(1);
            System.out.println("Packet is not corrupt, sending to application layer");
            simulator.sendToApplicationLayer(this, pkt.getData());
            simulator.sendToNetworkLayer(this,ackPkt);
        }
    }

    public boolean isCorrupt(TransportLayerPacket pkt){
        CheckSum cSum = new CheckSum();
        long ogSum = pkt.getCheckSum();
        long newSum = cSum.createChecksum(pkt.getData());
        if(ogSum == newSum){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
