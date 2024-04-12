import java.io.IOException;
import java.nio.ByteBuffer;
// -------------------------------------------------------------------------
/**
 * SortFunction: Contains quicksort functionality of program
 * 
 * @author asifrahman
 * @version Apr 12, 2024
 */
public class SortFunction {

    private BufferPool buffers;
    private static int RECORD_SIZE = 4;

    // ----------------------------------------------------------
    /**
     * Create a new SortFunction object.
     * 
     * @param buffpool
     *            bufferPool that quickSort gets called on
     * @throws IOException
     */
    public SortFunction(BufferPool buffpool) throws IOException {
        buffers = buffpool;
        this.quickSort(0, (int)(buffers.getFileLength() / RECORD_SIZE) - 1);
    }

    // ----------------------------------------------------------
    /**
     * Swap: changes the left byte array position to indexB and right byte array
     * position to indexA
     * 
     * @param indexA
     *            starting position of left arr
     * @param indexB
     *            starting position of right arr
     * @param left
     *            left record
     * @param right
     *            right record
     * @throws IOException
     */
    public void swap(int indexA, int indexB, byte[] left, byte[] right)
        throws IOException {
        buffers.setbytes(right, indexA * RECORD_SIZE);
        buffers.setbytes(left, indexB * RECORD_SIZE);
    }

    // ----------------------------------------------------------
    /**
     * Executes partitioning of array using short pivot as comparison.
     * 
     * @param lowIndex
     *            represents beginning of partition arr
     * @param highIndex
     *            represents end of partition arr
     * @param pivot
     *            value used for comparisons
     * @return lowIndex where the pivot should swap to
     * @throws IOException
     */
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

            if (highIndex > lowIndex) {
                swap(lowIndex, highIndex, leftRec, rightRec);
            }
        }

        return lowIndex;
    }


    // ----------------------------------------------------------
    /**
     * method that executes quickSort functionalities recursively
     * 
     * @param lowIndex
     *            beginning of partition arr
     * @param highIndex
     *            end of partition arr
     * @throws IOException
     */
    public void quickSort(int lowIndex, int highIndex) throws IOException {
        int pivotIndex = (highIndex + lowIndex) / 2;// value used for quickSort

        byte[] pivotArr = new byte[RECORD_SIZE];
        buffers.getbytes(pivotArr, pivotIndex * RECORD_SIZE);

        byte[] highArr = new byte[RECORD_SIZE];
        buffers.getbytes(highArr, highIndex * RECORD_SIZE);

        swap(pivotIndex, highIndex, pivotArr, highArr);

        int partitionIndex = partition(lowIndex, highIndex - 1, toShort(
            pivotArr));
        byte[] partitionArr = new byte[RECORD_SIZE];
        buffers.getbytes(partitionArr, partitionIndex * RECORD_SIZE);

        swap(partitionIndex, highIndex, partitionArr, pivotArr);

        if (partitionIndex == lowIndex && checkKey(lowIndex + 1, highIndex,
            toShort(pivotArr)))
            return;

        if ((partitionIndex - lowIndex) > 1)
            quickSort(lowIndex, partitionIndex - 1); // sorts left partition
        if ((highIndex - partitionIndex) > 1)
            quickSort(partitionIndex + 1, highIndex);
    }


    /**
     * turns a byte array to a short value
     * 
     * @param space
     *            byte[] that is being turned to a short
     * @return
     * @throws IOException
     */
    private short toShort(byte[] space) throws IOException {
        ByteBuffer bb;
        bb = ByteBuffer.wrap(space);
        short recordKey = bb.getShort();
        return recordKey;
    }


    /**
     * checks if a group of keys are equal
     * 
     * @param lowIndex
     *            the index where the method starts
     * @param highIndex
     *            the index where the method ends
     * @param pivot
     *            the pivot value that is being compared to everything
     * @return true or false
     * @throws IOException
     */
    private boolean checkKey(int lowIndex, int highIndex, short pivot)
        throws IOException {
        byte[] arr = new byte[RECORD_SIZE];
        for (int i = lowIndex; i <= highIndex; i++) {
            buffers.getbytes(arr, i * RECORD_SIZE);
            if (toShort(arr) != pivot) {
                return false;
            }
        }
        return true;
    }
}
