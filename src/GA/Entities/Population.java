package GA.Entities;

import java.util.Arrays;
import java.util.Comparator;

public class Population {

    private final Individual[] individuals;
    private final Evaluable fitnessEvaluator;

    public Population(int size, int chromosomeLength, Evaluable fitnessEvaluator) {
        this.fitnessEvaluator = fitnessEvaluator;
        this.individuals = new Individual[size];
        for (int i = 0; i < size; i++) {
            this.individuals[i] = new Individual(chromosomeLength, fitnessEvaluator);
        }
        this.initialize();
    }

    public void initialize() {
        for (Individual individual : individuals) {
            individual.generateChromosome();
        }
    }

    public Individual getFittest() {
        return Arrays.stream(individuals).
                max(Comparator.comparingDouble(Individual::getFitness)).
                orElseThrow();
    }

    public void calculateFitness() {
        Arrays.stream(individuals).forEach(Individual::calculateFitness);
    }

    public double getTotalFitness() {
        return Arrays.stream(individuals).mapToDouble(Individual::getFitness).sum();
    }
    public void setIndividual(int index, Individual individual) {
        individuals[index] = individual;
    }

    public Individual getIndividual(int index) {
        return individuals[index];
    }
    public Individual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(int idx, Individual[] elites) {
        for (int i = idx, j = 0; i<idx+elites.length; i++, j++) {
            this.individuals[i] = elites[j];
        }
    }

    public Evaluable fitnessEvaluator() {
        return this.fitnessEvaluator;
    }
}
