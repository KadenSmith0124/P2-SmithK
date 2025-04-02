package P2SmithK;

import java.util.Random;

public class P2SmithK {

    public static void main(String[] args) {
        
        int itemCount = 1000;
        int maxWeight = 100;
        int maxValue = 500;
        int[] weights = new int[itemCount];
        int[] values = new int[itemCount];
        
        assignWeight(itemCount, maxWeight, weights);
        assignValue(itemCount, maxValue, values);

        

        // Prints lists (comment out when not needed)
        for (int i = 0; i < 1000; i++) {
            System.out.println("Item " + i + " -> Weight: " + weights[i] + ", Value: " + values[i]);
        }
        
    }
    
    public static void assignWeight(int count, int weight, int [] arr)
    {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            arr[i] = rand.nextInt(weight) + 1;
        }
    }
    
    public static void assignValue(int count, int value, int [] arr)
    {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            arr[i] = rand.nextInt(value) + 1;
        }
    }
    
}
