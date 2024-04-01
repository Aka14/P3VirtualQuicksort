import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class SortFunction {
    
    private BufferPool buffers;
    
    public SortFunction(String filename, BufferPool buffpool) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filename, "r");
        byte[] some = new byte[(int)file.length()];
        file.read(some);
        buffers = buffpool;
        this.quickSort(some, 0, some.length-1);
    }

    
    private void swap(int indexA, int indexB) throws IOException {
        byte[] byteLeft = new byte[4];
        byte[] byteRight = new byte[4];
        buffers.getbytes(byteLeft, 4, indexA);
        buffers.getbytes(byteRight, 4, indexB);
        buffers.setbytes(byteRight, 4, indexA);
        buffers.setbytes(byteLeft, 4, indexB);
        
    }


    public int partition(int lowIndex, int highIndex, short pivot) throws IOException {
          byte[] arr = new byte[4];
          ByteBuffer bb;
          
          while(lowIndex <= highIndex) {
             buffers.getbytes(arr, 4, lowIndex);
             bb = ByteBuffer.wrap(arr);
             short recordKey = bb.getShort();
             while(recordKey - pivot < 0) {
                 lowIndex+=1;
                 buffers.getbytes(arr, 4, lowIndex);
                 bb = ByteBuffer.wrap(arr);
                 short newKey = bb.getShort();
                 recordKey = newKey;
             }
             buffers.getbytes(arr, 4, highIndex);
             bb = ByteBuffer.wrap(arr);
             short highKey = bb.getShort();
             while((highIndex >= lowIndex) && (highKey - pivot >=0)) {
                 highIndex-=1; 
                 buffers.getbytes(arr, 4, lowIndex);
                 bb = ByteBuffer.wrap(arr);
                 short newKey = bb.getShort();
                 highKey = newKey;
             }
             if(highIndex > lowIndex) swap(lowIndex, highIndex);
          }
          
          return lowIndex;
    }


    public void quickSort(byte[] arr, int lowIndex, int highIndex) throws IOException {
        int pivotIndex = (highIndex + lowIndex) / 2; //value used for quickSort comparisons
        
        swap(pivotIndex, highIndex); //swap it to end of array
        
        int partitionIndex = partition(lowIndex, highIndex-1, arr[highIndex]); //beginning position is array minus pivot
        swap(partitionIndex, highIndex); //places new pivot
        
        if((partitionIndex - highIndex) > 1) quickSort(arr, lowIndex, partitionIndex - 1); //sorts left partition
        if((highIndex - partitionIndex) > 1) quickSort(arr, partitionIndex + 1, highIndex);
    }

}
