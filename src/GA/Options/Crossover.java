package GA.Options;

import GA.Entities.Individual;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static GA.Options.Variables.genomeLength;
import static GA.Options.Variables.random;

public abstract class Crossover {
    public abstract Individual crossover(Individual a, Individual b);

    public static class SinglePointCrossover extends Crossover {

        @Override
        public Individual crossover(Individual a, Individual b) {
            Random random = new Random();
            int crossoverPoint = random.nextInt(genomeLength);
            Individual child = new Individual(genomeLength, a.fitnessEvaluator());
            for (int i = 0; i < genomeLength; i++) {
                if (i < crossoverPoint) {
                    child.getChromosome()[i] = a.getChromosome()[i];
                } else {
                    child.getChromosome()[i] = b.getChromosome()[i];
                }
            }
            return child;
        }
    }

    public static class TwoPointCrossover extends Crossover {

        @Override
        public Individual crossover(Individual a, Individual b) {
            Random random = new Random();
            int crossoverPoint1 = random.nextInt(genomeLength);
            int crossoverPoint2 = random.nextInt(genomeLength);
            if (crossoverPoint1 > crossoverPoint2) {
                int temp = crossoverPoint1;
                crossoverPoint1 = crossoverPoint2;
                crossoverPoint2 = temp;
            }
            Individual child = new Individual(genomeLength, a.fitnessEvaluator());
            for (int i = 0; i < genomeLength; i++) {
                if (i < crossoverPoint1 || i > crossoverPoint2) {
                    child.getChromosome()[i] = a.getChromosome()[i];
                } else {
                    child.getChromosome()[i] = b.getChromosome()[i];
                }
            }
            return child;
        }
    }

    public static class UniformCrossover extends Crossover {

        @Override
        public Individual crossover(Individual a, Individual b) {
            Random random = new Random();
            Individual child = new Individual(genomeLength, a.fitnessEvaluator());
            for (int i = 0; i < genomeLength; i++) {
                if (random.nextDouble() < 0.5) {
                    child.getChromosome()[i] = a.getChromosome()[i];
                } else {
                    child.getChromosome()[i] = b.getChromosome()[i];
                }
            }
            return child;
        }
    }

    public static class TSPTwoPointCrossover extends Crossover {
        @Override
        public Individual crossover(Individual parent1, Individual parent2) {
            int numCities = parent1.getChromosome().length;
            int startIndex = (int) (random.nextDouble() * numCities);
            int endIndex = (int) (random.nextDouble() * (numCities - startIndex)) + startIndex;

            Set<Integer> visitedCities = new HashSet<>();
            Individual child = new Individual(numCities, parent1.fitnessEvaluator());

            for (int i = startIndex; i < endIndex; i++) {
                int city = parent1.getChromosome()[i];
                child.getChromosome()[i] = city;
                visitedCities.add(city);
            }

            int j = endIndex;
            for (int i = 0; i < numCities; i++) {
                int city = parent2.getChromosome()[i];
                if (!visitedCities.contains(city)) {
                    child.getChromosome()[j] = city;
                    j = (j + 1) % numCities;
                }
            }

            return child;
        }
    }
    public static class TSPSinglePointCrossover extends Crossover {
        @Override
        public Individual crossover(Individual parent1, Individual parent2) {
            int numCities = parent1.getChromosome().length;
            int crossoverPoint = random.nextInt(numCities);

            Set<Integer> visitedCities = new HashSet<>();
            Individual child = new Individual(numCities, parent1.fitnessEvaluator());

            for (int i = 0; i < crossoverPoint; i++) {
                int city = parent1.getChromosome()[i];
                child.getChromosome()[i] = city;
                visitedCities.add(city);
            }

            int j = crossoverPoint;
            for (int i = 0; i < numCities; i++) {
                int city = parent2.getChromosome()[i];
                if (!visitedCities.contains(city)) {
                    child.getChromosome()[j] = city;
                    j = (j + 1) % numCities;
                }
            }

            return child;
        }
    }


    public enum CrossoverMethod {
        SINGLE_POINT,
        TWO_POINT,
        UNIFORM,
        TSP_TWO_POINT,
        TSP_SINGLE_POINT
    }

    public static Crossover getMethod (CrossoverMethod method) {
        return switch (method) {
            case SINGLE_POINT -> new SinglePointCrossover();
            case TWO_POINT -> new TwoPointCrossover();
            case UNIFORM -> new UniformCrossover();
            case TSP_TWO_POINT -> new TSPTwoPointCrossover();
            case TSP_SINGLE_POINT -> new TSPSinglePointCrossover();
        };
    }
}
