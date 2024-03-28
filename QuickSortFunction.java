
public class QuickSortFunction {

    private int[] swap(int[] arr, int indexA, int indexB) {
        int[] newArr = arr;
        int valA = arr[indexA];
        int valB = arr[indexB];
        newArr[indexA] = valB;
        newArr[indexB] = valA;
        return newArr;
    }


    public int partition(int[] arr, int lowIndex, int highIndex) {
        int pivotIndex = (highIndex - lowIndex) / 2;
        int pivot = arr[pivotIndex];
        System.out.println("pivotIndex = " + pivotIndex);
        System.out.println("pivot = " + pivot);
        arr = swap(arr, pivotIndex, highIndex);
        int newHigh = highIndex - 1;
        int swapLow = 0;
        int swapHigh = 0;
        boolean flag = false;
        while (!flag) {
            for (int j = lowIndex; j < highIndex; j++) {
                if (arr[j] >= pivot) {
                    swapLow = j;
                }
            }
            for (int j = newHigh; j >= lowIndex; j--) {
                if (arr[j] < pivot) {
                    swapHigh = j;
                }
            }
            if (swapLow >= swapHigh) {
                flag = true;
                break;
            }
            arr = swap(arr, swapLow, swapHigh);
        }
        return swapHigh;
    }


    public int[] quickSort(int[] arr, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int partitionIndex = partition(arr, lowIndex, highIndex);
            quickSort(arr, lowIndex, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, highIndex);
        }
        return arr;
    }
}
