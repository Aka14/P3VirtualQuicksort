/**
 * {Project Description Here}
 */



/**
 * The class containing the main method.
 *
 * @author {Your Name Here}
 * @version {Put Something Here}
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Quicksort {

    /**
     * @param args
     *      Command line parameters.  See the project spec!!!
     */
    public static void main(String[] args) {
        // This is the main file for the program.
    }

    public Quicksort(String filename) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filename, "r");
        byte[] some = new byte[(int)file.length()];
        file.read(some);
        this.quickSort(some, 0, some.length-1);
    }

    
    private void swap(byte[] arr, int indexA, int indexB) {
        byte[] byteLeft = new byte[4];
        byte[] byteRight = new byte[4];
        System.arraycopy(arr, indexA, byteLeft, 0, 4);
        System.arraycopy(arr, indexB, byteRight, 0, 4);
        System.arraycopy(byteLeft,0 ,arr, indexB, 4);
        System.arraycopy(byteRight,0, arr, indexA, 4);
        //return arr;
    }

    public void quickSort(byte[] arr, int lowIndex, int highIndex) {
        long beginning = System.currentTimeMillis(); 
        int pivotIndex = (highIndex + lowIndex) / 2; //value used for quickSort comparisons
        
        swap(arr, pivotIndex, highIndex); //swap it to end of array
        
        int partitionIndex = partition(arr, lowIndex, highIndex-1, arr[highIndex]); //beginning position is array minus pivot
        swap(arr, partitionIndex, highIndex); //places new pivot
        
        if((partitionIndex - highIndex) > 1) quickSort(arr, lowIndex, partitionIndex - 1); //sorts left partition
        if((highIndex - partitionIndex) > 1) quickSort(arr, partitionIndex + 1, highIndex);
        long end = System.currentTimeMillis();
        long timeToSort = end - beginning;
    }
    public int partition(byte[] arr, int lowIndex, int highIndex, short pivot) {
        ByteBuffer bb = ByteBuffer.wrap(arr);
        int i = lowIndex - 1;
        for (int j = lowIndex; j < highIndex; j++) {
            if (bb.getShort(j) < pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, highIndex);
        return i + 1;
    }
}
