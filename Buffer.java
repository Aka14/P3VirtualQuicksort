public class Buffer {
    private byte[] bf;
    private int blockId;
    private boolean dirtyBit;

    public Buffer(int size) {
        bf = new byte[size * 4];
    }
    
    public byte[] getBF() {
        return this.bf;
    }


    public int getBlockId() {
        return blockId;
    }


    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }


    public boolean isDirty() {
        return dirtyBit;
    }


    public void setDirty(boolean dB) {
        this.dirtyBit = dB;
    }
    
    

}
