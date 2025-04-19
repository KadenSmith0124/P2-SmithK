package P2SmithK;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

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
        
        runGraphingTests();
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
    
    public static void writeCSV(String fileName, String header, List<String[]> rows) {
    try (FileWriter writer = new FileWriter(fileName)) {
        writer.write(header + "\n");
        for (String[] row : rows) {
            writer.write(String.join(",", row) + "\n");
        }
        System.out.println("Saved: " + fileName);
    } catch (IOException e) {
        System.err.println("Error writing " + fileName + ": " + e.getMessage());
    }
}
    
public static void runGraphingTests() {
    int[] itemCounts = {10, 20, 50, 100, 200};
    int[] capacities = {20, 50, 100, 200, 300};
    int fixedCapacity = 100;
    int fixedItemCount = 100;
    Random rand = new Random();
    final int REPEAT = 100;

    // Store rows for CSV export
    List<String[]> qualityItemRows = new ArrayList<>();
    List<String[]> qualityCapRows = new ArrayList<>();
    List<String[]> timeItemRows = new ArrayList<>();
    List<String[]> timeCapRows = new ArrayList<>();

    // --- QUALITY VS ITEM COUNT ---
    for (int n : itemCounts) {
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            int w = rand.nextInt(50) + 1;
            int v = rand.nextInt(100) + 1;
            items[i] = new Item(w, v);
        }
        int dp = knapsackDP(items, fixedCapacity);
        int gv = greedyByValue(items, fixedCapacity);
        int gr = greedyByRatio(items, fixedCapacity);
        qualityItemRows.add(new String[]{String.valueOf(n), String.valueOf(dp), String.valueOf(gv), String.valueOf(gr)});
    }

    // --- QUALITY VS CAPACITY ---
    Item[] fixedItems = new Item[fixedItemCount];
    for (int i = 0; i < fixedItemCount; i++) {
        int w = rand.nextInt(50) + 1;
        int v = rand.nextInt(100) + 1;
        fixedItems[i] = new Item(w, v);
    }
    for (int cap : capacities) {
        int dp = knapsackDP(fixedItems, cap);
        int gv = greedyByValue(fixedItems, cap);
        int gr = greedyByRatio(fixedItems, cap);
        qualityCapRows.add(new String[]{String.valueOf(cap), String.valueOf(dp), String.valueOf(gv), String.valueOf(gr)});
    }

    // --- TIME VS ITEM COUNT (microseconds) ---
    for (int n : itemCounts) {
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            int w = rand.nextInt(50) + 1;
            int v = rand.nextInt(100) + 1;
            items[i] = new Item(w, v);
        }

        long dpTotal = 0, gvTotal = 0, grTotal = 0;

        for (int i = 0; i < REPEAT; i++) {
            long start = System.nanoTime();
            knapsackDP(items, fixedCapacity);
            dpTotal += (System.nanoTime() - start);

            start = System.nanoTime();
            greedyByValue(items, fixedCapacity);
            gvTotal += (System.nanoTime() - start);

            start = System.nanoTime();
            greedyByRatio(items, fixedCapacity);
            grTotal += (System.nanoTime() - start);
        }

        long dpTime = dpTotal / REPEAT / 1_000;
        long gvTime = gvTotal / REPEAT / 1_000;
        long grTime = grTotal / REPEAT / 1_000;

        timeItemRows.add(new String[]{String.valueOf(n), String.valueOf(dpTime), String.valueOf(gvTime), String.valueOf(grTime)});
    }

    // --- TIME VS CAPACITY (microseconds) ---
    for (int cap : capacities) {
        long dpTotal = 0, gvTotal = 0, grTotal = 0;

        for (int i = 0; i < REPEAT; i++) {
            long start = System.nanoTime();
            knapsackDP(fixedItems, cap);
            dpTotal += (System.nanoTime() - start);

            start = System.nanoTime();
            greedyByValue(fixedItems, cap);
            gvTotal += (System.nanoTime() - start);

            start = System.nanoTime();
            greedyByRatio(fixedItems, cap);
            grTotal += (System.nanoTime() - start);
        }

        long dpTime = dpTotal / REPEAT / 1_000;
        long gvTime = gvTotal / REPEAT / 1_000;
        long grTime = grTotal / REPEAT / 1_000;

        timeCapRows.add(new String[]{String.valueOf(cap), String.valueOf(dpTime), String.valueOf(gvTime), String.valueOf(grTime)});
    }

    // --- Write CSV files ---
    writeCSV("quality_vs_items.csv", "ItemCount,DP,GreedyValue,GreedyRatio", qualityItemRows);
    writeCSV("quality_vs_capacity.csv", "Capacity,DP,GreedyValue,GreedyRatio", qualityCapRows);
    writeCSV("time_vs_items.csv", "ItemCount,DP_Time,GreedyValue_Time,GreedyRatio_Time", timeItemRows);
    writeCSV("time_vs_capacity.csv", "Capacity,DP_Time,GreedyValue_Time,GreedyRatio_Time", timeCapRows);
}


}
