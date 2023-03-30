package Problems;

import GA.Entities.Evaluable;
import GA.Entities.GeneInit;
import GA.Options.Crossover;
import GA.Options.Mutation;
import GA.Options.Selection;

import java.util.Arrays;
import java.util.stream.IntStream;

import static GA.Options.Crossover.CrossoverMethod.TSP_TWO_POINT;
import static GA.Options.Mutation.MutationMethod.SWAP;
import static GA.Options.Selection.SelectionMethod.TOURNAMENT;
import static GA.Options.Variables.*;
import static java.util.Collections.shuffle;

public class TSP {
    public static int numCities = 30;

    public static int[][] distanceMatrix;
    public static void generateRandomDistanceMatrix() {
        distanceMatrix = new int[numCities][numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = i; j < numCities; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    if (distanceMatrix[i][j] != 0) continue;
                    double distance = random.nextInt(99) + 1;
                    distanceMatrix[i][j] = (int) distance;
                    distanceMatrix[j][i] = (int) distance;
                }
            }
        }
    }

    public static void printDistanceMatrix() {
        System.out.println("Num. cities: "+ numCities);
        Arrays.stream(distanceMatrix).forEachOrdered(ints -> {
            IntStream.range(0, distanceMatrix[0].length).forEachOrdered(j -> System.out.printf("%2d ", ints[j]));
            System.out.println();
        });
    }
    private static double calculateFitness(int[] tour) {
        double fitness = 0;

        for (int i = 0; i < numCities - 1; i++) {
            int city1 = tour[i];
            int city2 = tour[i + 1];
            fitness += distanceMatrix[city1][city2];
        }
        return 1 / fitness;
    }


    public static Evaluable getFitnessEvaluation()  {
        return TSP::calculateFitness;
    }

    public static GeneInit getGeneInitializer() {
        return () -> IntStream.range(0, numCities).unordered().toArray();
    }

    public static void initTSP() {
        genes = IntStream.range(0, numCities).toArray();

        genomeLength = numCities;
        geneInitializer = getGeneInitializer();

        System.out.println("Initializing TSP problem framework");
        generateRandomDistanceMatrix();
        printDistanceMatrix();


        selectionMethod = Selection.getMethod(TOURNAMENT);
        crossoverMethod = Crossover.getMethod(TSP_TWO_POINT);
        mutationMethod = Mutation.getMethod(SWAP);
    }

}