public class Sender extends TransportLayer {


    public Sender (String name, NetworkSimulator simulator) {
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
        if(pkt.getAcknum() == 0) {
            pkt.setData(this.data);
            rdt_send(this.data);
        }
    }

    @Override
    public void timerInterrupt() {

    }
}
