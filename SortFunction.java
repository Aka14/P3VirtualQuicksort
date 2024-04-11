import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class SortFunction {

    private BufferPool buffers;
    private static int RECORD_SIZE = 4;

    public SortFunction(BufferPool buffpool) throws IOException {
        buffers = buffpool;
        this.quickSort(0, (int)(buffers.getFileLength() / RECORD_SIZE) - 1);
    }


    public void swap(int indexA, int indexB) throws IOException {
        byte[] byteLeft = new byte[RECORD_SIZE];
        byte[] byteRight = new byte[RECORD_SIZE];
        buffers.getbytes(byteLeft, indexA * RECORD_SIZE);
        buffers.getbytes(byteRight, indexB * RECORD_SIZE);
        buffers.setbytes(byteRight, indexA * RECORD_SIZE);
        buffers.setbytes(byteLeft, indexB * RECORD_SIZE);
    }


    public int partition(int lowIndex, int highIndex, short pivot)
        throws IOException {
        byte[] leftRec = new byte[RECORD_SIZE];
        byte[] rightRec = new byte[RECORD_SIZE];
        
        while (lowIndex <= highIndex) {

            buffers.getbytes(leftRec, lowIndex * RECORD_SIZE);
            buffers.getbytes(rightRec, highIndex * RECORD_SIZE);
            while (toShort(leftRec) < pivot) {
                lowIndex++;
                buffers.getbytes(leftRec, lowIndex * RECORD_SIZE);
            }
            while (highIndex >= lowIndex && toShort(rightRec) >= pivot) {
                highIndex--;
                if (highIndex < 0) {
                    break;
                }
                buffers.getbytes(rightRec, highIndex * RECORD_SIZE);
            }
            if (highIndex > lowIndex)
                swap(lowIndex, highIndex);
        }

        return lowIndex;
    }


    public void quickSort(int lowIndex, int highIndex) throws IOException {
        int pivotIndex = (highIndex + lowIndex) / 2; // value used for quickSort
        byte[] arr = new byte[RECORD_SIZE];
        buffers.getbytes(arr, pivotIndex * RECORD_SIZE);
        swap(pivotIndex, highIndex); // swap it to end of array
        int partitionIndex = partition(lowIndex, highIndex - 1, toShort(arr));

        swap(partitionIndex, highIndex); // places new pivot
//        System.out.println("Partition = " + partitionIndex);
//        System.out.println("high = " + highIndex);
        if ((partitionIndex - lowIndex) > 1)
            quickSort(lowIndex, partitionIndex - 1); // sorts left partition
        if ((highIndex - partitionIndex) > 1)
            quickSort(partitionIndex + 1, highIndex);
    }


    private short toShort(byte[] space) throws IOException {
        ByteBuffer bb;
        bb = ByteBuffer.wrap(space);
        short recordKey = bb.getShort();
        return recordKey;
    }

}
