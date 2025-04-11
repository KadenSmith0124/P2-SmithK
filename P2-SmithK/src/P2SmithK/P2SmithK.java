package P2SmithK;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class P2SmithK {

    // Item class
    static class Item {
        int weight;
        int value;

        public Item(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(W: " + weight + ", V: " + value + ")";
        }
    }

    public static void main(String[] args) {
        runTestCase1();
        runTestCase2();

        // Generate random items
        Item[] randomItems = generateRandomItems(10, 10, 20); // 10 items, weight: 1–10, value: 1–20
        int randomCapacity = 30;
        int dpRandom = knapsackDP(randomItems, randomCapacity);
        int greedyRandom = greedyByRatio(randomItems, randomCapacity);
        System.out.println("\n[Random Test] DP Value: " + dpRandom + ", Greedy by Ratio: " + greedyRandom);
    }

    // Test Case 1
    public static void runTestCase1() {
        System.out.println("\nTest Case 1: Greedy by Value-to-Weight Ratio Fails");

        Item[] items = {
            new Item(1, 2), new Item(2, 3), new Item(3, 4), new Item(4, 5),
            new Item(5, 6), new Item(6, 7), new Item(7, 8), new Item(8, 9),
            new Item(9, 10), new Item(10, 100)
        };
        int capacity = 10;

        displayItems(items);
        System.out.println("Knapsack Capacity: " + capacity);

        int greedyValue = greedyByRatio(items, capacity);
        int dpValue = knapsackDP(items, capacity);

        System.out.println("Greedy by Ratio Value: " + greedyValue);
        System.out.println("Dynamic Programming Value: " + dpValue);
    }

    // Test Case 2
    public static void runTestCase2() {
        System.out.println("\nTest Case 2: Greedy by Max Value Fails");

        Item[] items = {
            new Item(1, 2), new Item(1, 2), new Item(1, 2), new Item(1, 2),
            new Item(1, 2), new Item(1, 2), new Item(1, 2), new Item(1, 2),
            new Item(1, 2), new Item(8, 10)
        };
        int capacity = 8;

        displayItems(items);
        System.out.println("Knapsack Capacity: " + capacity);

        int greedyValue = greedyByValue(items, capacity);
        int dpValue = knapsackDP(items, capacity);

        System.out.println("Greedy by Max Value: " + greedyValue);
        System.out.println("Dynamic Programming Value: " + dpValue);
    }

    // Sorting Methods

    public static int greedyByRatio(Item[] items, int capacity) {
        Item[] sorted = Arrays.copyOf(items, items.length);
        Arrays.sort(sorted, (a, b) -> b.value * a.weight - a.value * b.weight); // Avoid decimals

        int totalValue = 0, currentWeight = 0;
        for (Item item : sorted) {
            if (currentWeight + item.weight <= capacity) {
                currentWeight += item.weight;
                totalValue += item.value;
            }
        }
        return totalValue;
    }

    public static int greedyByValue(Item[] items, int capacity) {
        Item[] sorted = Arrays.copyOf(items, items.length);
        Arrays.sort(sorted, Comparator.comparingInt(i -> -i.value)); // Descending value

        int totalValue = 0, currentWeight = 0;
        for (Item item : sorted) {
            if (currentWeight + item.weight <= capacity) {
                currentWeight += item.weight;
                totalValue += item.value;
            }
        }
        return totalValue;
    }

    // Dynamic Programming

    public static int knapsackDP(Item[] items, int capacity) {
        int n = items.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            int wt = items[i - 1].weight;
            int val = items[i - 1].value;

            for (int w = 0; w <= capacity; w++) {
                if (wt <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - wt] + val);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return dp[n][capacity];
    }

    // Item Gen

    public static Item[] generateRandomItems(int count, int maxWeight, int maxValue) {
        Random rand = new Random();
        Item[] items = new Item[count];

        for (int i = 0; i < count; i++) {
            int weight = rand.nextInt(maxWeight) + 1; // 1 to maxWeight
            int value = rand.nextInt(maxValue) + 1;   // 1 to maxValue
            items[i] = new Item(weight, value);
        }

        return items;
    }

    // Display

    public static void displayItems(Item[] items) {
        System.out.println("Items:");
        for (int i = 0; i < items.length; i++) {
            System.out.println("Item " + (i + 1) + ": " + items[i]);
        }
    }
}
