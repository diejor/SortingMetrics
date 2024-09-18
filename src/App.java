public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        int[] arr = { 12, 13, 5, 7, 9 };

        System.out.println("Insertion Sort");
        sortingMetrics(InsertionSort::insertionSort, arr.clone());

    }

    static void sortingMetrics(SortingAlgorithm sortingAlgorithm, int[] arr) {
        Integer comparisons = 0;
        Integer movements = 0;
        sortingAlgorithm.sort(arr, comparisons, movements);
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Movements: " + movements);
    }
}

@FunctionalInterface
interface SortingAlgorithm {
    void sort(int[] arr, Integer comparisons, Integer movements);
}

class InsertionSort {
    public static void insertionSort(int[] arr, Integer comparisons, Integer movements) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                comparisons++;                          // COMPARISON

                movements++;                            // MOVEMENT            
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            comparisons++;                              // COMPARISON

            movements++;                                // MOVEMENT
            arr[j + 1] = key;
        }
    }
}


class QuickSort {
    public static void quicksort(int[] arr, Integer comparisons, Integer movements) {
        quicksort(arr, 0, arr.length - 1, comparisons, movements);
    }

    public static void quicksort(int[] arr, int low, int high, Integer comparisons, Integer movements) {
        if (low < high) {
            int pi = partition(arr, low, high, comparisons, movements);
            quicksort(arr, low, pi - 1, comparisons, movements);
            quicksort(arr, pi + 1, high, comparisons, movements);
        }
    }

    private static int partition(int[] arr, int low, int high, Integer comparisons, Integer movements) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            comparisons++;                      // COMPARISON     
            if (arr[j] < pivot) {
                i++;

                movements++;                    // MOVEMENT         
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        movements++;                            // MOVEMENT
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}

class HeapSort {
    public static void heapSort(int[] arr, Integer comparisons, Integer movements) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, comparisons, movements);
        }
        for (int i = n - 1; i > 0; i--) {

            movements++;                                               // MOVEMENT
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0, comparisons, movements);
        }
    }

    private static void heapify(int[] arr, int n, int i, Integer comparisons, Integer movements) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        comparisons++;                                                  // COMPARISON
        if (left < n && arr[left] > arr[largest]) largest = left;

        comparisons++;                                                  // COMPARISON                                      
        if (right < n && arr[right] > arr[largest]) largest = right;

        if (largest != i) {
            movements++;                                                // MOVEMENT
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest, comparisons, movements);
        }
    }
}

class MergeSort {
    public static void mergeSort(int[] arr, Integer comparisons, Integer movements) {
        mergeSort(arr, 0, arr.length - 1, comparisons, movements);
    }

    public static void mergeSort(int[] arr, int left, int right, Integer comparisons, Integer movements) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid, comparisons, movements);
            mergeSort(arr, mid + 1, right, comparisons, movements);
            merge(arr, left, mid, right, comparisons, movements);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, Integer comparisons, Integer movements) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; i++) {
            L[i] = arr[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = arr[mid + 1 + j];
        }
        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            comparisons++;              // COMPARISON
            movements++;                // MOVEMENT
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}