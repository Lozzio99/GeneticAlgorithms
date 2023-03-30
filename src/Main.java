import GA.Entities.Evaluable;
import GA.Entities.Individual;
import GA.GeneticAlgorithm;
import GA.Options.Crossover;
import GA.Options.Crossover.CrossoverMethod;
import GA.Options.Mutation;
import GA.Options.Mutation.MutationMethod;
import GA.Options.Selection.SelectionMethod;
import GA.Options.Variables;
import Problems.Knapsack;
import Problems.KnapsackSolver;
import Problems.NearestNeighborSolver;
import Problems.TSP;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

import static GA.Options.Crossover.CrossoverMethod.*;
import static GA.Options.Mutation.MutationMethod.BITFLIP;
import static GA.Options.Mutation.MutationMethod.SWAP;
import static GA.Options.Selection.SelectionMethod.*;
import static GA.Options.Selection.getMethod;
import static GA.Options.Variables.*;
import static Problems.Knapsack.elements;
import static Problems.TSP.numCities;
import static java.lang.Double.MIN_VALUE;

public class Main {

    public static void main(String[] args) {
        testMethodsTSP();
        testMethodsKnapsack();
    }

    static void testMethodsTSP() {
        numCities = 10;
        genomeLength = numCities;
        genes = IntStream.range(0, numCities).toArray();
        geneInitializer = TSP.getGeneInitializer();
        generations = 50;

        System.out.println("Initializing TSP problem framework ");
        TSP.generateRandomDistanceMatrix();
        TSP.printDistanceMatrix();

        Evaluable tspEvaluable = TSP.getFitnessEvaluation();



        var selectionMethods = new SelectionMethod[] {RANDOM1, RANDOM2, RANK, TOURNAMENT, ROULETTE_WHEEL};
        var crossoverMethods = new CrossoverMethod[] {TSP_SINGLE_POINT, TSP_TWO_POINT};
        var mutationMethods = new MutationMethod[] {SWAP};

        testMethods(tspEvaluable, selectionMethods, crossoverMethods, mutationMethods);
   }
    static void testMethodsKnapsack() {
        genes = new int[] {0,1};

        elements = 25;
        genomeLength = elements;
        geneInitializer = Knapsack.getGeneInitializer();
        generations = 50;

        System.out.println("Initializing Knapsack problem framework");
        Knapsack.initSack();
        Knapsack.printSack();
        Evaluable knapsackEvaluable = Knapsack.getFitnessEvaluator();

        var selectionMethods = new SelectionMethod[] {RANDOM1, RANDOM2, RANK, TOURNAMENT, ROULETTE_WHEEL};
        var crossoverMethods = new CrossoverMethod[] {SINGLE_POINT, TWO_POINT, UNIFORM};
        var mutationMethods = new MutationMethod[] {BITFLIP, SWAP};

        testMethods(knapsackEvaluable, selectionMethods, crossoverMethods, mutationMethods);
    }


    static void testMethods(Evaluable evaluable, SelectionMethod[] selectionMethods, CrossoverMethod[] crossoverMethods, MutationMethod[] mutationMethods) {
        int TEST_SIZE = 100;

        MutationMethod bestMutation = null;
        CrossoverMethod bestCrossover = null;
        SelectionMethod bestSelection = null;

        double maxFitnessTotal = MIN_VALUE;
        double minGenTotal = generations;

        for (MutationMethod mutationMethod : mutationMethods) {
            Variables.mutationMethod = Mutation.getMethod(mutationMethod);

            bestCrossover = null;
            double maxFitnessCrossover = MIN_VALUE;
            double minGenCrossover = generations;

            for (CrossoverMethod crossoverMethod : crossoverMethods) {
                Variables.crossoverMethod = Crossover.getMethod(crossoverMethod);

                bestSelection = null;
                double maxFitnessSelection = MIN_VALUE;
                double minGenSelection = generations;

                for (SelectionMethod selectionMethod : selectionMethods) {
                    var st = Instant.now();
                    Variables.selectionMethod = getMethod(selectionMethod);
                    double avgFitnessSelection = 0;
                    double avgGenSelection = 0;

                    for (int i = 0; i < TEST_SIZE; i++) {
                        var solution = getSolution(evaluable);
                        avgGenSelection += solution.getKey();
                        avgFitnessSelection += evaluable.evaluateFitness(solution.getValue().getChromosome());
                    }

                    double fitnessSelection = avgFitnessSelection / TEST_SIZE;
                    double genSelection = avgGenSelection / TEST_SIZE;

                    if (fitnessSelection > maxFitnessSelection) {
                        maxFitnessSelection = fitnessSelection;
                        bestSelection = selectionMethod;
                        if (genSelection < minGenSelection) {
                            minGenSelection = genSelection;
                        }
                    }

                    if (fitnessSelection == maxFitnessSelection && genSelection < minGenSelection) {
                        bestSelection = selectionMethod;
                        minGenSelection = genSelection;
                    }
                    double ms = Duration.between(st, Instant.now()).toMillis() / (double) TEST_SIZE;
                    System.out.println("[ "+ mutationMethod + ", " + crossoverMethod + ", " + selectionMethod + " ] : " +
                            fitnessSelection + " fit., " + genSelection + " generations , " + ms + " ms");
                }

                assert bestSelection != null;
                System.out.println("best: " + bestSelection.name() + " ( " + maxFitnessSelection + ", " + minGenSelection + " )");

                if (maxFitnessSelection > maxFitnessCrossover) {
                    maxFitnessCrossover = maxFitnessSelection;
                    bestCrossover = crossoverMethod;
                    if (minGenSelection < minGenCrossover) {
                        minGenCrossover = minGenSelection;
                    }
                }
                if (maxFitnessSelection == maxFitnessCrossover && minGenSelection < minGenCrossover) {
                    bestCrossover = crossoverMethod;
                    minGenCrossover = minGenSelection;
                }

            }

            assert bestCrossover != null;
            System.out.println("best: [ " + bestCrossover.name() +", "+ bestSelection.name() + "] : ( " +
                    maxFitnessCrossover + ", " + minGenCrossover + " )");


            if (maxFitnessCrossover > maxFitnessTotal) {
                maxFitnessTotal = maxFitnessCrossover;
                bestMutation = mutationMethod;
                if (minGenCrossover < minGenTotal) {
                    minGenTotal = minGenCrossover;
                }
            }
            if (maxFitnessCrossover == maxFitnessTotal && minGenCrossover < minGenTotal) {
                bestMutation = mutationMethod;
                minGenTotal = minGenCrossover;
            }

        }

        assert bestMutation != null;
        System.out.println("best: [ "+ bestMutation.name() +", "+ bestCrossover.name() +", "+ bestSelection.name() + "] : ( " +
                maxFitnessTotal + ", " + minGenTotal + " )");

    }

    static void demoTSP() {
        TSP.initTSP();
        Evaluable tspEvaluable = TSP.getFitnessEvaluation();
        System.out.println("GA Solution");
        runGA(tspEvaluable);

        int[] solution = NearestNeighborSolver.solve();
        double fitness =  TSP.getFitnessEvaluation().evaluateFitness(solution);
        System.out.println("NN Solution");
        System.out.println(Arrays.toString(solution) +" (fitness = " + fitness + ")");
    }

    static void demoKnapsack() {
        Knapsack.initKnapsack();
        Evaluable knEvaluable = Knapsack.getFitnessEvaluator();
        runGA(knEvaluable);

        int[] solution = KnapsackSolver.solve();
        double fitness = Knapsack.getFitnessEvaluator().evaluateFitness(solution);
        System.out.println(Arrays.toString(solution) +" (fitness = " + fitness + ")");
    }

    public static void runGA(Evaluable knEvaluable) {
        GeneticAlgorithm knGA = new GeneticAlgorithm(populationSize, eliteCount, knEvaluable);
        for (int i = 0; i< generations; i++)
            knGA.evolve();

        var bestIndividual = knGA.getPopulation().getFittest();
        System.out.println(bestIndividual);
    }

    private static Map.Entry<Integer, Individual> getSolution(Evaluable evaluable) {
        GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, eliteCount, evaluable);
        for (int i = 0; i< generations; i++) ga.evolve();
        return Map.entry(ga.bestGeneration, ga.getPopulation().getFittest());
    }


}
