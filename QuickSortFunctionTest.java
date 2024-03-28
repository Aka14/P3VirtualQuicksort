import student.TestCase;

public class QuickSortFunctionTest extends TestCase {
    QuickSortFunction qs;

    public void setUp() {
        qs = new QuickSortFunction();
    }


    public void testQuicksort() {
        int[] inputArr = {1, 8, 5, 6, 3, 2, 4, 7 };
        int[] solutionArr = { 1, 2, 3, 4, 5, 6, 7, 8 };
        int[] testArr = qs.quickSort(inputArr, 0, 7);
        for (int i = 0; i < testArr.length; i++) {
            System.out.println(testArr[i]);
            
        }
    }
}
