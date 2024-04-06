public class Buffer {
    private byte[] bf;
    private int blockId;
    private boolean dirtyBit;

    public Buffer(int id) {
        bf = new byte[4096];
        this.blockId=id;
        this.dirtyBit = false;
    }
    
    public byte[] getBF() {
        return this.bf;
    }


    public int getBlockId() {
        return blockId;
    }
    
    public void setBlockId(int id) {
        this.blockId = id;
    }


    public boolean isDirty() {
        return dirtyBit;
    }


    public void setDirty(boolean dB) {
        this.dirtyBit = dB;
    }
    
//    public byte[] getRecord(int index) {
//        byte[] rec = new byte[4];
//        System.arraycopy(bf, index*4, rec, 0, 4);
//        return rec;
//        
//    }


}
