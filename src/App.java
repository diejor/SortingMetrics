import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
        int start = 0;
        int range = 60000;
        int size = 50000;

        System.out.println("Experimental Results");
        System.out.println("Size\t" + size);
        System.out.println();

        // sorting algorithms
        List<SortingAlgorithm> algorithms = Arrays.asList(
            new SortingAlgorithm() {
                @Override
                public void sort(int[] arr, MutInt comparisons, MutInt movements) {
                    InsertionSort.insertionSort(arr, comparisons, movements);
                }
                @Override
                public String toString() {
                    return "Insertion Sort";
                }
            },
            new SortingAlgorithm() {
                @Override
                public void sort(int[] arr, MutInt comparisons, MutInt movements) {
                    QuickSort.quicksort(arr, comparisons, movements);
                }
                @Override
                public String toString() {
                    return "Quick Sort";
                }
            },
            new SortingAlgorithm() {
                @Override
                public void sort(int[] arr, MutInt comparisons, MutInt movements) {
                    HeapSort.heapSort(arr, comparisons, movements);
                }
                @Override
                public String toString() {
                    return "Heap Sort";
                }
            },
            new SortingAlgorithm() {
                @Override
                public void sort(int[] arr, MutInt comparisons, MutInt movements) {
                    MergeSort.mergeSort(arr, comparisons, movements);
                }
                @Override
                public String toString() {
                    return "Merge Sort";
                }
            }
        );

        // FIRST TABLE
        int[] randomArray = getRandomArray(start, range, size);

        System.out.println("Random Order");
        System.out.printf("%-25s %-15s %-15s %-10s%n", "Sorting Algorithm", "Comparisons", "Movements", "Total Time");
        System.out.println("------------------------------------------------------------------------------------------");

        List<AlgorithmMetrics> randomMetrics = runAndCollectMetrics(algorithms, randomArray);

        // Print metrics for random array
        for (AlgorithmMetrics metrics : randomMetrics) {
            System.out.printf("%-25s %-15d %-15d %-10dms%n",
                metrics.algorithmName, metrics.comparisons, metrics.movements, metrics.totalTime);
        }

        System.out.println();



        // SECOND TABLE
        int[] sortedArray = Arrays.copyOf(randomArray, randomArray.length);
        Arrays.sort(sortedArray);

        // Run algorithms on sorted array
        System.out.println("Ascending Order");
        System.out.printf("%-25s %-15s %-15s %-10s%n", "Sorting Algorithm", "Comparisons", "Movements", "Total Time");
        System.out.println("------------------------------------------------------------------------------------------");

        List<AlgorithmMetrics> sortedMetrics = runAndCollectMetrics(algorithms, sortedArray);

        // Print metrics for sorted array
        for (AlgorithmMetrics metrics : sortedMetrics) {
            System.out.printf("%-25s %-15d %-15d %-10dms%n",
                metrics.algorithmName, metrics.comparisons, metrics.movements, metrics.totalTime);
        }
    }

    static int[] getRandomArray(int start, int range, int size) {
        return new Random().ints(size, start, range).toArray();
    }

    private static List<AlgorithmMetrics> runAndCollectMetrics(List<SortingAlgorithm> algorithms, int[] inputArray) {
        List<AlgorithmMetrics> metricsList = new ArrayList<>();
        for (SortingAlgorithm algorithm : algorithms) {
            int[] arrCopy = Arrays.copyOf(inputArray, inputArray.length);
            MutInt comparisons = new MutInt(0);
            MutInt movements = new MutInt(0);

            long startTime = System.currentTimeMillis();
            algorithm.sort(arrCopy, comparisons, movements);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            metricsList.add(new AlgorithmMetrics(algorithm.toString(), comparisons.value, movements.value, totalTime));
        }
        return metricsList;
    }

    private static class AlgorithmMetrics {
        String algorithmName;
        int comparisons;
        int movements;
        long totalTime;

        AlgorithmMetrics(String algorithmName, int comparisons, int movements, long totalTime) {
            this.algorithmName = algorithmName;
            this.comparisons = comparisons;
            this.movements = movements;
            this.totalTime = totalTime;
        }
    }        
}

class MutInt {
    int value;

    MutInt(int value) {
        this.value = value;
    }

    public void increment() {
        value++;
    }

    public String toString() {
        return Integer.toString(value);
    }
}

@FunctionalInterface
interface SortingAlgorithm {
    void sort(int[] arr, MutInt comparisons, MutInt movements);
}

class InsertionSort {
    public static void insertionSort(int[] arr, MutInt comparisons, MutInt movements) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                comparisons.increment();                // COMPARISON

                movements.increment();                  // MOVEMENT
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            comparisons.increment();                   // COMPARISON

            movements.increment();                     // MOVEMENT
            arr[j + 1] = key;
        }
    }

    public String toString() {
        return "Insertion Sort";
    }

    public class insertionSort {
    }
}


class QuickSort {
    public static void quicksort(int[] arr, MutInt comparisons, MutInt movements) {
        quicksort(arr, 0, arr.length - 1, comparisons, movements);
    }

    public static void quicksort(int[] arr, int low, int high, MutInt comparisons, MutInt movements) {
        if (low < high) {
            int pi = partition(arr, low, high, comparisons, movements);
            quicksort(arr, low, pi - 1, comparisons, movements);
            quicksort(arr, pi + 1, high, comparisons, movements);
        }
    }

    private static int partition(int[] arr, int low, int high, MutInt comparisons, MutInt movements) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            comparisons.increment();
            if (arr[j] < pivot) {
                i++;

                movements.increment();             // MOVEMENT
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        movements.increment();                   // MOVEMENT
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}

class HeapSort {
    public static void heapSort(int[] arr, MutInt comparisons, MutInt movements) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, comparisons, movements);
        }
        for (int i = n - 1; i > 0; i--) {
            movements.increment();                              // MOVEMENT
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0, comparisons, movements);
        }
    }

    private static void heapify(int[] arr, int n, int i, MutInt comparisons, MutInt movements) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        comparisons.increment();                                // COMPARISON
        if (left < n && arr[left] > arr[largest]) largest = left;

        comparisons.increment();                                // COMPARISON
        if (right < n && arr[right] > arr[largest]) largest = right;

        if (largest != i) {
            movements.increment();                              // MOVEMENT
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest, comparisons, movements);
        }
    }
}

class MergeSort {
    public static void mergeSort(int[] arr, MutInt comparisons, MutInt movements) {
        mergeSort(arr, 0, arr.length - 1, comparisons, movements);
    }

    public static void mergeSort(int[] arr, int left, int right, MutInt comparisons, MutInt movements) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid, comparisons, movements);
            mergeSort(arr, mid + 1, right, comparisons, movements);
            merge(arr, left, mid, right, comparisons, movements);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, MutInt comparisons, MutInt movements) {
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
            comparisons.increment();                // COMPARISON
            movements.increment();                  // MOVEMENT
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