package Problems;

import GA.Entities.Evaluable;
import GA.Entities.GeneInit;
import GA.Options.Crossover;
import GA.Options.Mutation;
import GA.Options.Selection;

import java.util.Arrays;
import java.util.stream.IntStream;

import static GA.Options.Crossover.CrossoverMethod.UNIFORM;
import static GA.Options.Mutation.MutationMethod.BITFLIP;
import static GA.Options.Selection.SelectionMethod.RANK;
import static GA.Options.Variables.*;

public class Knapsack {

    public static int elements;
    public static int[] weights;
    public static int[] values;
    public static int capacity;


    public static void initSack() {
        weights = new int[elements];
        values = new int[elements];
        IntStream.range(0, elements).forEach(i -> {
            weights[i] = random.nextInt(29) + 1;
            values[i] = random.nextInt(19) + 1;
        });
        capacity = Arrays.stream(weights).sum() / 2;
    }
    private static double evaluateSack(int[] items) {
        int totalValue = 0;
        int totalWeight = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] == 1) {
                totalValue += values[i];
                totalWeight += weights[i];
            }
        }
        if (totalWeight > capacity) return 0.0;  // Penalize solutions that violate the weight constraint
        return totalValue;
    }

    public static Evaluable getFitnessEvaluator() {
        return Knapsack::evaluateSack;
    }

    public static GeneInit getGeneInitializer() {
        return () -> IntStream.range(0, elements).map(i -> genes[random.nextInt(genes.length)]).toArray();
    }

    public static void initKnapsack() {
        genes = new int[] {0,1};
        elements = 10;
        initSack();

        genomeLength = elements;
        geneInitializer = getGeneInitializer();

        System.out.println("Initializing Knapsack problem framework");
        printSack();

        selectionMethod = Selection.getMethod(RANK);
        crossoverMethod = Crossover.getMethod(UNIFORM);
        mutationMethod = Mutation.getMethod(BITFLIP);

    }

    public static void printSack() {
        System.out.println("Weights  : "+ Arrays.toString(weights));
        System.out.println("Values   : "+ Arrays.toString(values));
        System.out.println("Capacity : "+ capacity);
    }

}
