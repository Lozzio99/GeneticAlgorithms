package Problems;

import java.util.Arrays;
import java.util.stream.IntStream;

import static Problems.Knapsack.*;

public class KnapsackSolver {

    public static int[] solve() {

        int[][] dp = new int[elements + 1][capacity + 1];

        for (int i = 1; i <= elements; i++) {
            int weight = weights[i - 1];
            int value = values[i - 1];
            for (int j = 1; j <= capacity; j++) {
                if (weight > j) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weight] + value);
                }
            }
        }

        boolean[] selectedItems = new boolean[elements];
        int j = capacity;
        for (int i = elements; i > 0; i--) {
            if (dp[i][j] != dp[i - 1][j]) {
                selectedItems[i - 1] = true;
                j -= weights[i - 1];
            }
        }

        return IntStream.range(0, elements).map(i -> selectedItems[i] ? 1 : 0).toArray();
    }

    public static void main(String[] args) {
        Knapsack.initSack();
        Knapsack.printSack();
        solve();
    }
}

