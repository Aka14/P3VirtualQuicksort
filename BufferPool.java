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
        
        
        // if not in buffer pool, seek in disk file
        
//        moveBufferToFront(buffers.length-1);
//        if(buffers[0].isDirty()) {
//            file.seek(blockPos * bufferSize);
//            file.write(buffers[0].getBF(), 0, 4096);
//            buffers[0].setBlockId(blockPos);
//            buffers[0].setDirty(false);
//        }
//        file.seek(blockPos * bufferSize);
//        file.read(buffers[0].getBF(), 0, 4096);
        
        
        
        System.arraycopy(buffers[0].getBF(), startInBlock, space, 0, 4);
        
        
        //TODO: kick out last buffer, check if dirty
        //upload associated block to bufferpool
        //return buffer[0].getRecord(startInBlock)
        
        
//        if (checkLRU(bufferPos) == -1) {
//            file.seek(bufferPos * bufferSize);
//            Buffer temp = null;
//            if (currentInBP == buffers.length) {
//                temp = buffers[buffers.length - 1];
//            }
//            for (int i = 0; i < currentInBP - 1; i++) {
//                buffers[i + 1] = buffers[i];
//            }
//            if (temp != null && temp.isDirty() == true) {
//                file.seek(temp.getBlockId() * bufferSize);
//                file.write(temp.getBF(), 0, bufferSize);
//            }
//            int tempo = file.read(buffers[0].getBF(), 0, bufferSize);
//            if (tempo != bufferSize) {
//                throw new IOException("Block doesn't have enough space");
//            }
//            buffers[0].setBlockId(bufferPos);
//        }
//        else {
//            for (int j = 0; j < checkLRU(bufferPos); j++) {
//                buffers[j + 1] = buffers[j];
//            }
//        }
//        currentInBP += 1;
//        System.arraycopy(buffers[0].getBF(), startInBlock, space, 0, 4);
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
        
//        moveBufferToFront(buffers.length-1);
//        if(buffers[buffers.length-1].isDirty()) {
//            file.write(buffers[buffers.length-1].getBF(), 0, 4096);
//        }
//        
//        buffers[0].setBlockId(blockPos);
//        buffers[0].setDirty(false);
//        
//        file.seek(blockPos * bufferSize);
//        file.read(buffers[0].getBF(), 0, 4096);
        
        
        System.arraycopy(space, 0, buffers[0].getBF(), startInBlock, 4);
        buffers[0].setDirty(true);
        //TODO: check if in bufferpool, same as above
        //copy space into index pos of the buffer[0]
        
        
//        int bufferPos = pos / bufferSize;
//        int startInBlock = pos % bufferSize;
//        if (checkLRU(bufferPos) == -1) {
//            file.seek(bufferPos * bufferSize);
//            Buffer temp = null;
//            if (currentInBP == buffers.length) {
//                temp = buffers[buffers.length - 1];
//            }
//            for (int i = 0; i < currentInBP - 1; i++) {
//                buffers[i + 1] = buffers[i];
//            }
//            if (temp != null && temp.isDirty() == true) {
//                file.seek(temp.getBlockId() * bufferSize);
//                file.write(temp.getBF(), 0, bufferSize);
//            }
//            int tempo = file.read(buffers[0].getBF(), 0, bufferSize);
//            if (tempo != bufferSize) {
//                throw new IOException("Block doesn't have enough space");
//            }
//            buffers[0].setBlockId(bufferPos);
//        }
//        else {
//            for (int j = 0; j < checkLRU(bufferPos); j++) {
//                buffers[j + 1] = buffers[j];
//            }
//        }
//        currentInBP += 1;
//        System.arraycopy(space, 0, buffers[0].getBF(), startInBlock, 4);
    }


}
