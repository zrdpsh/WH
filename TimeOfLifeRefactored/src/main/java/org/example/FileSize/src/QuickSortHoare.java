package org.example.FileSize.src;/*
Тройка Хоара для основного алгоритма со всеми рекурсивными вызовами:
- предусловие - имеем список, содержащий целые числа
- постусловие - полученный список отсортирован от меньшего к большему
 */


public class QuickSortHoare {
    public static void main(String[] args) {
        int[] tst = {817, 101, 17, 3,1};
        quickSort(tst, 0, tst.length-1);
        for (int i = 0; i < tst.length; i++) {
            System.out.println(tst[i]);
        }

    }

// The whole function precodition { All elements in arr are whole numbers }
    public static void quickSort(int[] arr, int low, int high) {

        if (low < high) {

            int pivotIndex = partition(arr, low, high);

            quickSort(arr, low, pivotIndex - 1);

            quickSort(arr, pivotIndex + 1, high);

        }
    }
// The whole function postcondition { arr is sorted }


/*
Тройка Хоара, чтобы найти опорный элемент
- предусловие - элемент с меньшим индексом меньше или равен опорному элементу, элемент с большим индексом больше или равен опорному
- постусловие - все элементы с индексами меньше индекса опорного элемента меньше опорного элемента,
все, которые больше - больше

на каждой итерации цикла возможны два варианта:
- если текущий элемент меньше опорного - он меняется местами с последним бОльшим элементом, т.е. перемещается влево
таким образом, на каждом каждой из итераций все элементы включительно с индексами меньше или равными текущему, "меньшему", с индексом i,
будут меньше опорного элемента, инвариант сохраняется
- если текущий элемент больше - оставляем всё как есть, инвариант сохраняется
- финальная операция - меняем местами опорный элемент и элемент больше него, находящийся левее всех. по всему списку все элементы с индексами
меньше опорного будут меньше опорного, все которые больше - больше
 */
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
// Precondition: {low ≤ pivotIndex ≤ high}
        int i = low - 1;
// Precondition: { low <= j < high, i is the index of the smaller element }
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }
// Postcondition: { All elements arr[low...i] <= pivot and arr[i+1...j] > pivot }
        swap(arr, i + 1, high);
        return i + 1;
// Postcondition: { (∀i ∈ [low,pivotIndex], arr[i] ≤ arr[pivotIndex]) ∧ (∀j ∈ [pivotIndex+1,high], arr[j] ≥ arr[pivotIndex]) }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}


