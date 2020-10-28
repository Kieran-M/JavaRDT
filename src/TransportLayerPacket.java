public class TransportLayerPacket {

    // Maybe remove these
    // You may need extra fields
    private int seqnum;
    private int acknum;

    byte[] data;
    long checksum;

    // You may need extra methods

    public TransportLayerPacket(TransportLayerPacket pkt) {
        // complete this method
        this.data = pkt.data;
        this.checksum = pkt.checksum;
    }

    public TransportLayerPacket(byte[] data, long checksum) {
        this.data = data;
        this.checksum = checksum;
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
}
