package GA.Options;

import GA.Entities.Individual;
import GA.Entities.Population;

import java.util.Arrays;

import static GA.Options.Variables.*;

public abstract class Selection {
    public abstract Individual select(Population population);

    public static class TournamentSelection extends Selection {
        private static final int TOURNAMENT_SIZE = 10;

        @Override
        public Individual select(Population population) {
            Population tournament = new Population(TOURNAMENT_SIZE, genomeLength, population.fitnessEvaluator());
            for (int i = 0; i < TOURNAMENT_SIZE; i++) {
                int randomIndex = random.nextInt(population.getIndividuals().length);
                tournament.setIndividual(i, population.getIndividual(randomIndex));
            }
            return tournament.getFittest();
        }
    }
    public static class RouletteWheelSelection extends Selection {

        @Override
        public Individual select(Population population) {
            double totalFitness = population.getTotalFitness();
            double rouletteWheelPosition = random.nextDouble() * totalFitness;
            double fitnessSoFar = 0;
            for (int i = 0; i < population.getIndividuals().length; i++) {
                fitnessSoFar += population.getIndividual(i).getFitness();
                if (fitnessSoFar >= rouletteWheelPosition) {
                    return population.getIndividual(i);
                }
            }
            throw new IllegalStateException();
        }
    }
    public static class RankSelection extends Selection {
        @Override
        public Individual select(Population population) {
            Arrays.sort(population.getIndividuals());

            // Calculate rank probabilities
            int size = populationSize;
            double[] rankProbs = new double[size];
            double rankSum = 0.0;
            for (int i = 0; i < size; i++) {
                rankProbs[i] = (2.0 - pressure) / size + 2.0 * i * (pressure - 1.0) / (size * (size - 1));
                rankSum += rankProbs[i];
            }

            for (int i = 0; i < size; i++) {
                rankProbs[i] /= rankSum;
            }

            // Select parents based on rank probabilities
            double r = random.nextDouble();
            double sum = 0.0;
            for (int i = 0; i < size; i++) {
                sum += rankProbs[i];
                if (r <= sum) {
                    return population.getIndividual(i);
                }
            }
            throw new IllegalStateException();
        }
    }
    public static class Random1Selection extends Selection {
        @Override
        public Individual select(Population population) {
            return population.getIndividual(random.nextInt(population.getIndividuals().length));
        }
    }
    public static class Random2Selection extends Selection {

        @Override
        public Individual select(Population population) {
            double totalFitness = population.getTotalFitness();
            double r = random.nextDouble() * totalFitness;
            double sum = 0;
            for (int i = 0; i < population.getIndividuals().length; i++) {
                Individual individual = population.getIndividual(i);
                sum += individual.getFitness();
                if (sum > r) return individual;
            }
            return population.getIndividuals()[population.getIndividuals().length - 1];
        }
    }

    public enum SelectionMethod {
        TOURNAMENT,
        ROULETTE_WHEEL,
        RANK,
        RANDOM1,
        RANDOM2
    }

    public static Selection getMethod(SelectionMethod method) {
        return switch (method) {
            case TOURNAMENT -> new TournamentSelection();
            case ROULETTE_WHEEL -> new RouletteWheelSelection();
            case RANK -> new RankSelection();
            case RANDOM1 -> new Random1Selection();
            case RANDOM2 -> new Random2Selection();
        };
    }
}
