import java.util.zip.CRC32;

public class Main {

    public static void main(String[] args) {
        NetworkSimulator sim = new NetworkSimulator(2, 0.0, 0.5, 10.0, false, 3);

        // TODO: Set the sender   (sim.setSender)
        sim.setSender(new RDTSender("sender", sim));

        // TODO: Set the receiver (sim.setReceiver)
        sim.setReceiver(new RDTReceiver("receiver", sim));

        sim.runSimulation();
    }
}
