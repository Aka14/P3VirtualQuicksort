
/**
 * {Project Description Here}
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
     *            Command line parameters. See the project spec!!!
     * @throws IOException
     * @throws NumberFormatException
     */
    public static void main(String[] args)
        throws NumberFormatException,
        IOException {
        // This is the main file for the program.
        if (args.length != 3) {
            System.out.println("Invalid parameters, input correct parameters.");
            return;
        }
        File file = new File(args[0]);
        if (!file.exists())
            System.out.println(
                "File does not exist. Please input exisiting file");

        BufferPool bp = new BufferPool(args[0], Integer.parseInt(args[1]));

        PrintWriter statFile = new PrintWriter(args[2]);
        long beginning = System.currentTimeMillis();
        SortFunction sf = new SortFunction(bp);
        bp.flush();
        long end = System.currentTimeMillis();
        long timeToSort = end - beginning;
        System.out.println("Time to sort: " + timeToSort + "ms");
        int cacheHits = bp.getCacheHits();
        int diskReads = bp.getDiskReads();
        int diskWrites = bp.getDiskWrites();
        statFile.println("Standard sort on " + args[0]);
        statFile.println("Cache hits: " + cacheHits);
        statFile.println("Disk reads: " + diskReads);
        statFile.println("Disk writes: " + diskWrites);
        statFile.println("Time to sort: " + timeToSort);
        statFile.close();
    }

}
