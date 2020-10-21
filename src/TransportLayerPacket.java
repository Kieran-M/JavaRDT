public class TransportLayerPacket {

    // Maybe remove these
    // You may need extra fields
    private int seqnum;
    private int acknum;


    CheckSum cSum = new CheckSum();

    byte[] data;
    long sum;

    // You may need extra methods
    public TransportLayerPacket(byte[] data) {
        this.data = data;
        this.sum = cSum.createChecksum(data);
    }

    public TransportLayerPacket(TransportLayerPacket pkt) {
        data = pkt.getData();
        sum = pkt.getCheckSum();
    }

    public void setSeqnum(int seqnum) {
        this.seqnum = seqnum;
    }

    public void setAcknum(int acknum) {
        this.acknum = acknum;
    }

    public byte[] getData() {
        return data;
    }

    public long getCheckSum(){
        return sum;
    }

}
