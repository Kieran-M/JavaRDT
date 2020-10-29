import java.util.ArrayList;
import java.util.List;

public class RDTSender extends TransportLayer {

    private List<TransportLayerPacket> dataQ;    //Arraylist holding the queue of packets to be sent
    private List<TransportLayerPacket> window;   //The window of packets being sent
    private int n;                      //Window size
    private int base;                   //Base seqnum of the window
    private int nextSeqnum;             //The next available seqnum
    private int expectedSeqnum;         //The expected seqnum to be received

    public RDTSender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        n = 3;                      //Set size of window to 3
        base = 1;
        nextSeqnum = 1;
        expectedSeqnum = 1;
        dataQ = new ArrayList<>();  //Initialise packet queue
        window = new ArrayList<>(); //Initialise window
    }

    @Override
    public void rdt_send(byte[] data) {
        if (nextSeqnum < (base + n)) {
            TransportLayerPacket pkt = makePkt(nextSeqnum, data);
            window.add(pkt);
            System.out.println("Sender: Sending to network layer\n");
            simulator.sendToNetworkLayer(this, pkt);
            if (base == nextSeqnum)
                simulator.startTimer(this, TIMEOUT);
            nextSeqnum++;
        }else {
            System.out.println("Sender: Window is full so adding data to queue\n");
            TransportLayerPacket pkt = makePkt(nextSeqnum,data);
            nextSeqnum++;
            dataQ.add(pkt);
        }
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        System.out.println("Sender: Receiving packet\n");
        if (!isCorrupt(pkt) && pkt.getSeqnum() >= expectedSeqnum) {
            expectedSeqnum++;
            base = pkt.getSeqnum() + 1;
            if(base == nextSeqnum){
                simulator.stopTimer(this);
            } else {
                window.remove(0);
                simulator.stopTimer(this);
                simulator.startTimer(this,TIMEOUT);
            }
            if(dataQ.size() > 0){
                System.out.println("Sender: Making packet");
                TransportLayerPacket nextPkt = dataQ.get(0);
                window.add(nextPkt);
                dataQ.remove(0);
                System.out.println("Sender: Sending to network layer\n");
                simulator.sendToNetworkLayer(this, nextPkt);
            }
        }else{
            System.out.println("Sender: Waiting for timeout");
        }
        System.out.println("Sender: FINISHED");
    }

    @Override
    public void timerInterrupt() {
        //If time out then assume packet is lost and resend
        System.out.println("Sender: Timeout waiting for ACK");
        //Resend packet
        simulator.startTimer(this,TIMEOUT);
        for(TransportLayerPacket pkt:window){
            simulator.sendToNetworkLayer(this,pkt);
        }
    }


}
