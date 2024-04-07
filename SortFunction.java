import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class SortFunction {
    
    private BufferPool buffers;
    
    public SortFunction(BufferPool buffpool) throws IOException {
        buffers = buffpool;
        this.quickSort(0, (int)(buffers.getFileLength()/4)-1);
    }

    
    private void swap(int indexA, int indexB) throws IOException {
        byte[] byteLeft = new byte[4];
        byte[] byteRight = new byte[4];
        
        buffers.getbytes(byteLeft, indexA*4);
        buffers.getbytes(byteRight, indexB*4);
        
        buffers.setbytes(byteRight, indexA*4);
        buffers.setbytes(byteLeft, indexB*4);
        
    }


    public int partition(int lowIndex, int highIndex, short pivot) throws IOException {
          byte[] leftRec = new byte[4];
          byte[] rightRec = new byte[4];
          
          while(lowIndex <= highIndex) {
             
             //short recVal = toShort(leftRec, lowIndex);
             //System.out.println(recVal);
             while(toShort(leftRec, lowIndex) < pivot) {   //arr[lowIndex]
                 lowIndex+=1;
             }
             //short highVal = toShort(rightRec, highIndex);
             while((highIndex >= lowIndex) && (toShort(rightRec, highIndex) >=pivot)) {
                 //System.out.println("High index: "+highIndex);
                 highIndex-=1; 
             }
             if(highIndex > lowIndex) swap(lowIndex, highIndex);
          }
          
          return lowIndex;
    }


    public void quickSort(int lowIndex, int highIndex) throws IOException {
        int pivotIndex = (highIndex+lowIndex) / 2; //value used for quickSort comparisons
        byte[] arr = new byte[4];
        swap(pivotIndex, highIndex); //swap it to end of array
        
//        System.out.println(toShort(arr, pivotIndex));
        
        int partitionIndex = partition(lowIndex, highIndex-1, toShort(arr, pivotIndex)); //beginning position is array minus pivot
        swap(partitionIndex, highIndex); //places new pivot
        
        if((partitionIndex - highIndex) > 1) quickSort(lowIndex, partitionIndex - 1); //sorts left partition
        if((highIndex - partitionIndex) > 1) quickSort(partitionIndex + 1, highIndex);
    }
    
    private short toShort(byte[] space, int index) throws IOException {
        ByteBuffer bb;
        bb = ByteBuffer.wrap(space);
        short recordKey = bb.getShort();
        return recordKey;
    }

}
