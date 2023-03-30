package GA.Options;

import GA.Entities.Individual;

import static GA.Options.Variables.*;

public abstract class Mutation {
        public abstract void mutate(Individual individual);

    public static class BitFlipMutation extends Mutation {

        public void mutate(Individual individual) {
            for (int i = 0; i < individual.getChromosome().length; i++) {
                if (random.nextDouble() < mutationRate) {
                    individual.getChromosome()[i] = (individual.getChromosome()[i] + 1) % 2;
                }
            }
        }
    }
    public static class SwapMutation extends Mutation {
        public void mutate(Individual individual) {
            int gene1 = (int) (Math.random() * individual.getChromosome().length);
            int gene2 = (int) (Math.random() * individual.getChromosome().length);
            if (random.nextDouble() < mutationRate) {
                int temp = individual.getChromosome()[gene1];
                individual.getChromosome()[gene1] = individual.getChromosome()[gene2];
                individual.getChromosome()[gene2] = temp;
            }
        }
    }
    public static class RandomMutation extends Mutation {
        public void mutate(Individual individual) {
            for (int i = 0; i< genomeLength; i++) {
                if (random.nextDouble() < mutationRate) {
                    individual.getChromosome()[i] = genes[random.nextInt(genomeLength)];
                }
            }
        }
    }

    public enum MutationMethod {
        BITFLIP,
        SWAP,
        RANDOM
    }

    public static Mutation getMethod(MutationMethod method) {
        return switch (method) {
            case BITFLIP -> new BitFlipMutation();
            case SWAP -> new SwapMutation();
            case RANDOM -> new RandomMutation();
        };
    }

}



