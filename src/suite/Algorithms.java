/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suite;

/**
 *
 * @author Jeff
 */
public class Algorithms {
    public static void selectionSort(double[] array){
        int minimumIndex;
        double holder;
        for(int i = 0; i < array.length -1; i++){
            minimumIndex = i;
            for(int j = i+1; j < array.length; j++){
                if(array[j] < array[minimumIndex]){
                    minimumIndex = j;
                }
            }
            holder = array[i];
            array[i] = array[minimumIndex];
            array[minimumIndex] = holder;
        }
    }
    public static void insertionSort(double[] array){
        for(int i = 0; i < array.length; i++){
            double holder;
            int index = i;
            while(index > 0){
                if(array[index] < array[index-1]){
                    holder = array[index -1];
                    array[index - 1] = array[index];
                    array[index] = holder;
                }
                else{
                    break;
                }
                index--;
            }
        }
    }
    /*
        Permutates the array to the next lexicographic permutation.
        Outputs the array if it is the last permutation 
        or the next in the sequence.
        
        if array has two consecutive elements in increasing order{
        find the largest index i so that ai < ai+1
        find the largest index j so that ai < aj (where j >= i+1)
        swap(ai and aj)
        reverse the order of ai+1 all the way up to an
        return the new array;
    */
    public static int[] lexicographicPermutation(int[] array){
        int i = array.length - 1;
        int j = array.length - 1;
        
        for(int x = array.length - 2; x >= 0; x--){
            if(array[x] < array[i]){
                i = x;
                break;
            }
            i = x;
        }//got biggest index i where ai < ai+1
        
        for(int y = array.length -1; y > i; y--){
            j = y;
            if(array[i] < array[y]){
                break;
            }
        }//got biggest index j where ai < aj
        
        //return original array if max permutation
        if(array[j] < array[i]){
            return array;
        }
        //swap ai and aj
        int holder = array[i];
        array[i] = array[j];
        array[j] = holder;
        
        //reverse ai+1 to an
        //swap ai+1 and an;
        //swap ai+2 and an-1
        for(int a = i+1, n = array.length-1; a < n; a++,n--){
            holder = array[a];
            array[a] = array[n];
            array[n] = holder;
        }
        return array;
    }
    
}
