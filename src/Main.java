public class Main {

    public static void main(String[] args) {
        NetworkSimulator sim = new NetworkSimulator(10, 0.0, 0.5, 10.0, false, 1);;
        // TODO: Set the sender   (sim.setSender)
        sim.setSender(new Sender("RDT_Sender", sim));

        // TODO: Set the receiver (sim.setReceiver)
        sim.setReceiver(new Sender("RDT_Receiver", sim));

        sim.runSimulation();
    }

}
