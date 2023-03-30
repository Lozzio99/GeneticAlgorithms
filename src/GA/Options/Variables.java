package GA.Options;

import GA.Entities.GeneInit;

import java.util.Random;

import static GA.Options.Crossover.CrossoverMethod.TWO_POINT;
import static GA.Options.Mutation.MutationMethod.SWAP;
import static GA.Options.Selection.SelectionMethod.TOURNAMENT;

public class Variables {
    public static int[] genes;
    public static int genomeLength;
    public static final double mutationRate = 0.23;
    public static final double pressure = 2.0;

    public static final int populationSize = 5000;
    public static final int eliteCount = 100;
    public static int generations = 500;

    public static Mutation mutationMethod = Mutation.getMethod(SWAP);
    public static Crossover crossoverMethod = Crossover.getMethod(TWO_POINT);
    public static Selection selectionMethod = Selection.getMethod(TOURNAMENT);

    public static final Random random = new Random(1 << 7 << 1999);
    public static GeneInit geneInitializer;


}
