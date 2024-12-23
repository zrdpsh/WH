package org.example.Hoare3s;

public class InsertionSort {

/*
* { ∀i,j (i,j ∈ Z) } for k=1 to n−1 do Insert(A,k) { ∀i,j ((0≤i<j<n) ⟹ (A[i]≤A[j])) }
* предусловие: элементы списка - целые числа
* программа выполняет сортировку
* постусловие: весь список отсортирован
*/
    public static void insertionSort(int[] arr) {
        // { ∀i,j (i,j ∈ Z) }
        for (int k = 1; k < arr.length; k++) {
            insert(arr, k);
        }
        // { ∀i,j ((0≤i<j<n) ⟹ (A[i]≤A[j])) }
    }

/*
* { ∀i,j (( 0≤i<j<k ) ⟹ ( A[i] ≤ A[j] )) } insertKey (A,k) { ∀i,j ((0≤i<j≤k) ⟹ (A[i]≤A[j])) }
* предусловие: уже обработанная часть списка - отсортирована от меньшего к большему
* программа переносит (или не переносит) след. по порядку элемент из оставшейся части списка
* постусловие: увеличившаяся отсортированная часть - отсортирована от меньшего к большему
*/

    private static void insert(int[] arr, int k) {
        // ∀i,j (( 0≤i<j<k ) ⟹ ( A[i] ≤ A[j] ))
        int key = arr[k];
        int i = k - 1;

        while (i >= 0 && arr[i] > key) {
            arr[i + 1] = arr[i];
            i--;
        }
        arr[i + 1] = key;
        // ∀i,j ((0≤i<j≤k) ⟹ (A[i]≤A[j]))
    }

    public static void main(String[] args) {
        int[] arr = {5, 2, 4, 6, 1, 3};
        insertionSort(arr);

        for (int value : arr) {
            System.out.print(value + " ");
        }
    }
}

/*
 * композиция в данном случае
 * - внутренний цикл сопоставляет паре {отсортированная часть списка, след. число}
 * отсортированную часть с этим новым числом на нужном месте
 * - внешний цикл сопоставляет неотсортированный список с сортированным
 */
