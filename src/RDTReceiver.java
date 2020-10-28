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
        System.out.println("Received: " + pkt.getData());
        simulator.sendToApplicationLayer(this, pkt.getData());
        TransportLayerPacket received = new TransportLayerPacket("Received".getBytes());
        simulator.sendToNetworkLayer(this, received);
    }

    @Override
    public void timerInterrupt() {

    }
}
