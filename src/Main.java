public class Main {

    public static void main(String[] args) {
        NetworkSimulator sim = new NetworkSimulator(5, 0.3, 0.4, 10, false, 2);

        // TODO: Set the sender   (sim.setSender)
        sim.setSender(new RDTSender("sender", sim));

        // TODO: Set the receiver (sim.setReceiver)
        sim.setReceiver(new RDTReceiver("receiver", sim));

        sim.runSimulation();
    }
}
