package Problems;

import GA.Entities.Evaluable;
import GA.Entities.GeneInit;
import GA.GeneticAlgorithm;
import GA.Options.Crossover;
import GA.Options.Mutation;
import GA.Options.Selection;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static GA.Options.Crossover.CrossoverMethod.*;
import static GA.Options.Mutation.MutationMethod.SWAP;
import static GA.Options.Selection.SelectionMethod.*;
import static GA.Options.Variables.*;

public class TextMatching {

    public static List<Character> alphabet = IntStream.rangeClosed(0, 122).mapToObj(var -> (char) var).collect(Collectors.toList());

    static String key = "Un giorn0 sono Uscit4 e ho Visto una Caverna!";
    static {
        genes = alphabet.stream().mapToInt(alphabet::indexOf).toArray();
        genomeLength = key.length();
        geneInitializer = () -> IntStream.range(0, genomeLength).map(i -> random.nextInt(alphabet.size())).toArray();


        selectionMethod = Selection.getMethod(RANK);
        crossoverMethod = Crossover.getMethod(UNIFORM);
        mutationMethod = Mutation.getMethod(SWAP);

        generations = 100;

    }
    static Evaluable keyMatch = (s) -> IntStream.range(0, s.length).
            mapToDouble(i -> (alphabet.get(s[i]).equals(key.charAt(i)) ? 1 : 0)).
            sum() / s.length;


    private static String convertChromosome(int[] chromosome){
        return Arrays.stream(chromosome).mapToObj(j -> String.valueOf(alphabet.get(j))).collect(Collectors.joining());
    }

    public static void main(String[] args) {

        GeneticAlgorithm knGA = new GeneticAlgorithm(populationSize, eliteCount, keyMatch);
        for (int i = 0; i< generations; i++) {
            knGA.evolve();
            System.out.println(
                    convertChromosome(knGA.getPopulation().getFittest().getChromosome()) + " , " +
                            knGA.getPopulation().getFittest().getFitness()
            );
        }

    }

}
