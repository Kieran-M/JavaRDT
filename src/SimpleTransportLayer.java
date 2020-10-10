public class SimpleTransportLayer extends TransportLayer {


    public SimpleTransportLayer(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

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
        simulator.sendToApplicationLayer(this, pkt.getData());
    }

    @Override
    public void timerInterrupt() {

    }
}
