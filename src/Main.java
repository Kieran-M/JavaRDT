public class Main {

    public static void main(String[] args) {
        NetworkSimulator sim = new NetworkSimulator(10, 0.0, 0.0, 10.0, false, 1);
        TransportLayer transportLayer = new SimpleTransportLayer("RDT", sim);

        // TODO: Set the sender   (sim.setSender)
        sim.setSender(transportLayer);

        // TODO: Set the receiver (sim.setReceiver)
        sim.setReceiver(transportLayer);

        sim.runSimulation();
    }

}
