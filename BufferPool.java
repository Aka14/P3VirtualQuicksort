
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
    private static final int BUFFER_SIZE = 4096;
    private int numbersOfBuffs;
    private int diskReads;
    private int diskWrites;
    private int cacheHits;

    // ----------------------------------------------------------
    /**
     * Create a new BufferPool object.
     * 
     * @param filename
     * @param numBuffs
     * @throws IOException
     */
    public BufferPool(String filename, int numBuffs) throws IOException {
        file = new RandomAccessFile(filename, "rw");
        this.buffers = new Buffer[numBuffs];
        this.numbersOfBuffs = numBuffs;

        // Initialize the buffer pool with buffers
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new Buffer(-1);
        }
        diskReads = 0;
        diskWrites = 0;
        cacheHits = 0;
    }


    public long getFileLength() throws IOException {
        return file.length();
    }


    private boolean findLRU(int blockID) throws IOException {
        cacheHits++;
        int index = 0;
        int blocks = 0;
        boolean found = false;
        for (int i = 0; i < numbersOfBuffs; i++) {
            if (buffers[i].getBlockId() != -1) {
                blocks++;
            }
            // if the block id is within the buffer pool already
            if (buffers[i].getBlockId() == blockID) {
                // moveBufferToFront(i); //shifts buffers accordingly
                // TODO: take out the buffer, move it to the front of the list
                // return buffer[0].getRecord(startInBlock)
                index = i;
                found = true;
                break;
            }

        }
      
        if (!found && blocks < numbersOfBuffs) {
            index = blocks;
        }
        else if(!found) {
            index = numbersOfBuffs - 1;
        }
        if (!found && index == numbersOfBuffs - 1) {
         // index = numbersOfBuffs - 1;
                     if (buffers[index].isDirty()) {
                         file.seek(buffers[index].getBlockId() * BUFFER_SIZE);
                         file.write(buffers[index].getBF());
                         buffers[index].setDirty(false);
                     }
                 }
        
        Buffer temp = buffers[index];
        for (int j = index; j > 0; j--) {
            buffers[j] = buffers[j-1];
        }
        buffers[0] = temp;
        return found;
    }


    // ----------------------------------------------------------
    /**
     * Copy "sz" bytes from position "pos" of the buffered storage to "space"
     * 
     * @param pos
     * @throws IOException
     */
    public void getbytes(byte[] space, int pos) throws IOException {

        // Step 1: find block of interest
        // Step 2: see if block has been loaded into pool
        // Step 2a: if it has, move buffer containing it to front
        // Step 2b: if it hasnt't, load it into a buffer
        // --> evict last buffer, writing block back to file if dirty
        // Step 3: System.arraycopy()
        // --> buffer containg block our record is at should be buffers[0]

        int blockPos = pos / BUFFER_SIZE;
        int startInBlock = pos % BUFFER_SIZE;

        if (!findLRU(blockPos)) {
            file.seek(blockPos * BUFFER_SIZE);
            file.read(buffers[0].getBF(), 0, BUFFER_SIZE);
            buffers[0].setBlockId(blockPos);
            diskReads++;
        }
        // if buffers[0] is not what i want:
        // overwrite last buffer with the block i want
// System.out.println("startInBlock = " + startInBlock);
// if (startInBlock >= 0) {
        System.arraycopy(buffers[0].getBF(), startInBlock, space, 0, 4);
// }

        // TODO: kick out last buffer, check if dirty
        // upload associated block to bufferpool
        // return buffer[0].getRecord(startInBlock)

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
    public void setbytes(byte[] space, int pos) throws IOException {

        // Step 1: find block of interest
        // Step 2: see if block has been loaded into pool
        // Step 2a: if it has, move buffer containing it to front
        // Step 2b: if it hasnt't, load it into a buffer
        // --> evict last buffer, writing block back to file if dirty
        // Step 3: System.arraycopy()
        // --> buffer containg block our record is at should be buffers[0]
        // --> arraycopy is opposite direction of getBytes
        // Step 4: mark as dirty

        int blockPos = pos / BUFFER_SIZE;
        int startInBlock = pos % BUFFER_SIZE;

        if (!findLRU(blockPos)) {
            file.seek(blockPos * BUFFER_SIZE);
            file.read(buffers[0].getBF(), 0, BUFFER_SIZE);
            buffers[0].setBlockId(blockPos);
            diskWrites++;
        }
        System.arraycopy(space, 0, buffers[0].getBF(), startInBlock, 4);
        buffers[0].setDirty(true);

    }


    public void flush() throws IOException {
        for (int i = 0; i < buffers.length; i++) {
            if (buffers[i].isDirty()) {
                file.seek(buffers[i].getBlockId() * BUFFER_SIZE);
                file.write(buffers[i].getBF());
                buffers[i].setDirty(false);
                diskWrites++;
            }
        }
        file.close();
    }


    public int getCacheHits() {
        return cacheHits;
    }


    public int getDiskReads() {
        return diskReads;
    }


    public int getDiskWrites() {
        return diskWrites;
    }

}
