// -------------------------------------------------------------------------
/**
 * Buffer: Represents an individual buffer in the BufferPool
 * 
 * @author asifrahman
 * @version Apr 12, 2024
 */
public class Buffer {
    private byte[] bf;
    private int blockId;
    private boolean dirtyBit;

    // ----------------------------------------------------------
    /**
     * Create a new Buffer object.
     * 
     * @param id
     *            the block id
     */
    public Buffer(int id) {
        bf = new byte[4096];
        this.blockId = id;
        this.dirtyBit = false;
    }


    // ----------------------------------------------------------
    /**
     * Gets byte array that represents buffer.
     * 
     * @return byte array bf
     */
    public byte[] getBF() {
        return this.bf;
    }


    // ----------------------------------------------------------
    /**
     * Gets block id of buffer.
     * 
     * @return block id
     */
    public int getBlockId() {
        return blockId;
    }


    // ----------------------------------------------------------
    /**
     * Sets block id of buffer to id passed in
     * 
     * @param id
     *            new block id for buffer
     */
    public void setBlockId(int id) {
        this.blockId = id;
    }


    // ----------------------------------------------------------
    /**
     * Checks if buffer is dirty
     * 
     * @return true if dirty, false otherwise
     */
    public boolean isDirty() {
        return dirtyBit;
    }


    // ----------------------------------------------------------
    /**
     * Sets dirty bit of buffer to be false or true
     * 
     * @param dB
     *            boolean change to dirtyBit
     */
    public void setDirty(boolean dB) {
        this.dirtyBit = dB;
    }

}
