public class TransportLayerPacket {

    // Maybe remove these
    // You may need extra fields
    private int seqnum = 0;
    private int acknum;


    CheckSum cSum = new CheckSum();

    byte[] data;
    long sum;

    // You may need extra methods
    public TransportLayerPacket(byte[] data) {
        this.data = data;
        this.sum = cSum.createChecksum(data);
        setSeqnum(seqnum);
    }

    public TransportLayerPacket(TransportLayerPacket pkt) {
        data = pkt.getData();
        sum = pkt.getCheckSum();
        seqnum = pkt.getSeqnum();
        acknum = pkt.getAcknum();
    }

    public void setSeqnum(int seqnum) {
        this.seqnum = 1 - seqnum;
    }

    public void setAcknum(int acknum) {
        this.acknum = acknum;
    }

    public int getSeqnum() {
        return this.seqnum;
    }

    public int getAcknum() {
        return this.acknum;
    }
    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public long getCheckSum(){
        return sum;
    }

}
