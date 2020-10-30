import java.util.ArrayList;

public class RDTSender extends TransportLayer {

    private ArrayList<TransportLayerPacket> dataQ;    //Arraylist holding the queue of packets to be sent
    private ArrayList<TransportLayerPacket> window;   //Arraylist representing the window of packets being sent
    private int n;                               //Window size
    private int base;                            //Base seqnum of the window
    private int nextSeqnum;                      //The next available seqnum
    private int expectedAcknum;                  //The expected acknum to be received

    public RDTSender(String name, NetworkSimulator simulator) {
        super(name, simulator);
    }

    @Override
    public void init() {
        n = 3;                      //Set size of window to 3
        base = 1;                   //Set base packet to 1
        nextSeqnum = 1;             //Next expected seqnum is 1
        expectedAcknum = 1;         //Next expected seqnum is 1
        dataQ = new ArrayList<>();  //Initialise packet queue
        window = new ArrayList<>(); //Initialise window

    }

    @Override
    public void rdt_send(byte[] data) {
        //this.data = data;
        if (nextSeqnum < (base + n)) {
            TransportLayerPacket pkt = makePkt(nextSeqnum, data.clone());
            window.add(pkt);
            System.out.println("Sender: Sending to network layer\n");
            simulator.sendToNetworkLayer(this, pkt);

            if (base == nextSeqnum)
                simulator.startTimer(this, TIMEOUT);
            nextSeqnum++;
        } else {
            System.out.println("Sender: Window is full so adding data to queue\n");
            TransportLayerPacket pkt = makePkt(nextSeqnum, data.clone());
            nextSeqnum++;
            dataQ.add(pkt);
        }
    }

    @Override
    public void rdt_receive(TransportLayerPacket pkt) {
        System.out.println("Sender: Receiving packet\n");
        //Check that received packet is not corrupt and has the correct acknum
        if (!isCorrupt(pkt) && pkt.getSeqnum() >= expectedAcknum) {
            //Increment expected acknum and base
            expectedAcknum++;
            base = pkt.getSeqnum() + 1;
            if(base == nextSeqnum){
                simulator.stopTimer(this);
            } else {
                //Remove packet from window, stop current timer and start new timer for next packet
                window.remove(0);
                simulator.stopTimer(this);
                simulator.startTimer(this,TIMEOUT);
            }
            //If dataQ is not empty add packet to the window
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

