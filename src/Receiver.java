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
        this.data = data;
        TransportLayerPacket pkt = new TransportLayerPacket(data);
        simulator.sendToNetworkLayer(this, pkt);
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        if(isCorrupt(pkt) == true){
            rdt_send(this.data);
        }
        simulator.sendToApplicationLayer(this, pkt.getData());
    }

    public boolean isCorrupt(TransportLayerPacket pkt){
        CheckSum cSum = new CheckSum();
        long ogSum = pkt.getCheckSum();
        long newSum = cSum.createChecksum(pkt.getData());
        if(ogSum == newSum){
            System.out.println("Data is not corrupt");
            return false;
        }else{
            System.out.println("Data is corrupt");
            return true;
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
