import java.io.*;

// -------------------------------------------------------------------------
/**
 * Write a one-sentence summary of your class here.
 * Follow it with additional details about its purpose, what abstraction
 * it represents, and how to use it.
 * 
 * @author asifrahman
 * @version Mar 31, 2024
 */
public class BufferPool {

    private RandomAccessFile file;
    private Buffer[] buffers;
    private int bufferSize;
    private int numBuffers;
    private int currentInBP;
    private int cacheHits;
    private int diskReads;
    private int diskWrites;

    // ----------------------------------------------------------
    /**
     * Create a new BufferPool object.
     * 
     * @param filename
     * @param bufSize
     * @throws IOException
     */
    public BufferPool(String filename, int bufSize) throws IOException {
        file = new RandomAccessFile(filename, "r");
        this.buffers = new Buffer[bufSize];
        this.bufferSize = 4096;
        this.numBuffers = bufSize;
        currentInBP = 0;
        cacheHits = 0;
        diskReads = 0;
        diskWrites = 0;

        // Initialize the buffer pool with buffers
        for (int i = 0; i < numBuffers; i++) {
            buffers[i] = new Buffer(bufferSize);
        }
    }


    // ----------------------------------------------------------
    /**
     * Checks if blockId is within the buffer pool currently.
     * 
     * @param blockId
     *            block to be checked
     * @return
     *         index of block within buffer pool.
     */
    public int checkLRU(int blockId) {
        for (int i = 0; i < currentInBP; i++) {
            if (buffers[i].getBlockId() == blockId) {
                cacheHits++;
                return i;
            }
        }
        return -1;
    }


    // ----------------------------------------------------------
    /**
     * Copy "sz" bytes from position "pos" of the buffered storage to "space"
     * 
     * @param space
     * @param sz
     * @param pos
     * @throws IOException
     */
    public void getbytes(byte[] space, int sz, int pos) throws IOException {
        int bufferPos = pos / bufferSize;
        int startInBlock = pos % bufferSize;
        if (checkLRU(bufferPos) == -1) {
            file.seek(bufferPos * bufferSize);
            Buffer temp = null;
            if (currentInBP == buffers.length) {
                temp = buffers[buffers.length - 1];
            }
            for (int i = 0; i < currentInBP - 1; i++) {
                buffers[i + 1] = buffers[i];
            }
            if (temp != null && temp.isDirty() == true) {
                file.seek(temp.getBlockId() * bufferSize);
                file.write(temp.getBF(), 0, bufferSize);
            }
            int tempo = file.read(buffers[0].getBF(), 0, bufferSize);
            if (tempo != bufferSize) {
                throw new IOException("Block doesn't have enough space");
            }
            buffers[0].setBlockId(bufferPos);
        }
        else {
            for (int j = 0; j < checkLRU(bufferPos); j++) {
                buffers[j + 1] = buffers[j];
            }
        }
        currentInBP += 1;
        System.arraycopy(buffers[0].getBF(), startInBlock, space, 0, 4);
        diskWrites++;
    }


    // ----------------------------------------------------------
    /**
     * Copy "sz" bytes from "space" to position "pos" in the buffered storage.
     * 
     * @param space
     * @param sz
     * @param pos
     * @throws IOException
     */
    public void setbytes(byte[] space, int sz, int pos) throws IOException {
        int bufferPos = pos / bufferSize;
        int startInBlock = pos % bufferSize;
        if (checkLRU(bufferPos) == -1) {
            file.seek(bufferPos * bufferSize);
            Buffer temp = null;
            if (currentInBP == buffers.length) {
                temp = buffers[buffers.length - 1];
            }
            for (int i = 0; i < currentInBP - 1; i++) {
                buffers[i + 1] = buffers[i];
            }
            if (temp != null && temp.isDirty() == true) {
                file.seek(temp.getBlockId() * bufferSize);
                file.write(temp.getBF(), 0, bufferSize);
            }
            int tempo = file.read(buffers[0].getBF(), 0, bufferSize);
            if (tempo != bufferSize) {
                throw new IOException("Block doesn't have enough space");
            }
            buffers[0].setBlockId(bufferPos);
        }
        else {
            for (int j = 0; j < checkLRU(bufferPos); j++) {
                buffers[j + 1] = buffers[j];
            }
        }
        currentInBP += 1;
        System.arraycopy(space, 0, buffers[0].getBF(), startInBlock, 4);
        diskReads++;
    }


    /**
     * gets number of cache hits in the buffer pool
     * 
     * @return cacheHits
     */
    public int getCacheHits() {
        return cacheHits;
    }


    /**
     * gets the number of times data is read to a buffer
     * 
     * @return
     */
    public int getDiskReads() {
        return diskReads;
    }


    /**
     * gets the number of times data is written from a buffer
     * 
     * @return
     */
    public int getDiskWrites() {
        return diskWrites;
    }

}
