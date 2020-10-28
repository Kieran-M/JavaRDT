public class TransportLayerPacket {

    // Maybe remove these
    // You may need extra fields
    private int seqnum;
    private int acknum;
    private long checksum;
    private byte[] data;

    // You may need extra methods

    public TransportLayerPacket(TransportLayerPacket pkt) {
        // complete this method
        this.seqnum = pkt.seqnum;
        this.acknum = pkt.acknum;
        this.checksum = pkt.checksum;
        this.data = pkt.data;
    }

    public TransportLayerPacket(byte[] data, long checksum) {
        this.data = data;
        this.checksum = checksum;
    }

    public void setSeqnum(int seqnum) {
        this.seqnum = seqnum;
    }

    public int getAcknum() {
        return acknum;
    }

    public void setAcknum(int acknum) {
        this.acknum = acknum;
    }

    public long getChecksum() {
        return checksum;
    }

    public byte[] getData() {
        return data;
    }

}
