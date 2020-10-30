public class TransportLayerPacket {

    private int seqnum;
    private int acknum;
    private long checksum;
    private byte[] data;


    public TransportLayerPacket(TransportLayerPacket pkt) {
        this.seqnum = pkt.seqnum;
        this.acknum = pkt.acknum;
        this.checksum = pkt.checksum;
        this.data = pkt.data.clone();
    }

    public TransportLayerPacket(int seqnum, byte[] data, long checksum) {
        this.seqnum = seqnum;
        this.data = data;
        this.checksum = checksum;
    }

    public int getSeqnum() {
        return seqnum;
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
