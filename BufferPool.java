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
    private int numbersOfBuffs;

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
        this.bufferSize = 4096;
        this.numbersOfBuffs= numBuffs;

        // Initialize the buffer pool with buffers
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new Buffer(-1);
        }
    }
    
    public long getFileLength() throws IOException {
        return file.length();
    }


    
    private boolean findLRU(int blockID) throws IOException {
        int i=0;
        for(; i<buffers.length && buffers[i] != null; i++) {
            //if the block id is within the buffer pool already
            if (buffers[i].getBlockId() == blockID) {
                //moveBufferToFront(i); //shifts buffers accordingly
                //TODO: take out the buffer, move it to the front of the list
                //return buffer[0].getRecord(startInBlock)
                break;
            }
        }
        if(i==numbersOfBuffs) {
            i=numbersOfBuffs-1;
            if(buffers[i].isDirty()) {
                file.seek(buffers[i].getBlockId()*bufferSize);
                file.write(buffers[i].getBF(), 0, bufferSize);
                buffers[i].setDirty(false);
            }
        }
        Buffer temp = buffers[i];
        for(int j=i; j>0; j--) {
            buffers[j] = buffers[j-1];
        }
        buffers[0] = temp;
        return blockID == buffers[i].getBlockId();
    }


    // ----------------------------------------------------------
    /**
     * Copy "sz" bytes from position "pos" of the buffered storage to "space"
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
        
        int blockPos = pos / bufferSize;
        int startInBlock = pos % bufferSize;
        
        if(!findLRU(blockPos)) {
            file.seek(blockPos*bufferSize);
            file.read(buffers[0].getBF(), 0, bufferSize);
            buffers[0].setBlockId(blockPos);
        }
        // if buffers[0] is not what i want:
        // overwrite last buffer with the block i want
  
        
        
        System.arraycopy(buffers[0].getBF(), startInBlock, space, 0, 4);
        
        
        //TODO: kick out last buffer, check if dirty
        //upload associated block to bufferpool
        //return buffer[0].getRecord(startInBlock)
       
    }
    


    // ----------------------------------------------------------
    /**
     * Copy "sz" bytes from "space" to position "pos" in the buffered storage.
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
        
        int blockPos = pos / bufferSize;
        int startInBlock = pos % bufferSize;
        
        if(!findLRU(blockPos)) {
            file.seek(blockPos*bufferSize);
            file.read(buffers[0].getBF(), 0, bufferSize);
            buffers[0].setBlockId(blockPos);
        }
 
        
        System.arraycopy(space, 0, buffers[0].getBF(), startInBlock, 4);
        buffers[0].setDirty(true);
        
    }
    
    public void flush() throws IOException {
        for(int i=0; i<buffers.length; i++) {
            if(buffers[i].isDirty()) {
                file.seek(buffers[i].getBlockId()*bufferSize);
                file.write(buffers[i].getBF(), 0, bufferSize);
                buffers[i].setDirty(false);
            }
        }
        file.close();
    }


}
