import java.util.zip.CRC32;

public class Main {

    public static void main(String[] args) {
        NetworkSimulator sim = new NetworkSimulator(2, 0.0, 0.25, 10.0, false, 2);;
        CRC32 crc = new CRC32();

        // TODO: Set the sender   (sim.setSender)
        sim.setSender(new Sender("sender", sim));

        // TODO: Set the receiver (sim.setReceiver)
        sim.setReceiver(new Receiver("receiver", sim));

        sim.runSimulation();
    }
}
