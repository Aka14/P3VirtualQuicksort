import java.io.*;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author asifrahman
 *  @version Mar 31, 2024
 */
public class BufferPool {
    
    private RandomAccessFile file;
    private Buffer[] buffers;
    private int bufferSize;
    
    public BufferPool(String filename, int bufSize) throws IOException {
        file = new RandomAccessFile(filename, "r");
        byte[] some = new byte[(int)file.length()];
        file.read(some);
        this.buffers = new Buffer[bufSize];
        this.bufferSize = bufSize;
        
        // Initialize the buffer pool with buffers
        for (int i = 0; i < bufferSize; i++) {
            buffers[i] = new Buffer(bufferSize);
        }
    }
    
    public boolean checkLRU(int blockId) {
        for(int i=0; i<bufferSize; i++) {
            if(buffers[i].getBlockId() == blockId) {
                return true;
            }
        }
        return false;
    }

    // Copy "sz" bytes from position "pos" of the buffered storage to "space"
    public void getbytes(byte[] space, int sz, int pos) throws IOException {
        int bufferPos = pos/bufferSize;
        int startInBlock = pos % bufferSize;
        if(checkLRU(bufferPos) == false) {
            file.seek(bufferPos * bufferSize);
            int tempo = file.read(buffers[0].bf, 0, bufferSize);
            if(tempo != bufferSize) {
                throw new IOException("Block doesn't have enough space");
            }
            buffers[0].blockId = bufferPos;
        }
        System.arraycopy(buffers[0].bf, startInBlock, space, 0, sz);
    }
    
    public void setbytes(byte[] space, int sz, int pos) {
        buffers[pos].dirtyBit = true;
    }
    
    private class Buffer{
        
        private byte[] bf;
        private int blockId;
        private boolean dirtyBit;
        
        public Buffer(int size) {
            bf = new byte[size*4];
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

        public void setDirty(boolean dirtyBit) {
            this.dirtyBit = dirtyBit;
        }
    }

}
