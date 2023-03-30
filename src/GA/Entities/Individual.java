package GA.Entities;

import java.util.Arrays;
import java.util.stream.Collectors;

import static GA.Options.Variables.*;

public class Individual implements Comparable<Individual> {
    private int[] chromosome;
    private double fitness;
    private final Evaluable fitnessEvaluator;

    public Individual(int chromosomeLength, Evaluable fitnessEvaluator) {
        chromosome = new int[chromosomeLength];
        this.fitnessEvaluator = fitnessEvaluator;
    }

    public void generateChromosome() {
        this.chromosome = geneInitializer.generateGene();
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void calculateFitness() {
        this.setFitness(this.fitnessEvaluator.evaluateFitness(this.chromosome));
    }

    @Override
    public String toString() {
        return Arrays.toString(chromosome) + " (fitness = " + fitness + ")";
    }

    @Override
    public int compareTo(Individual i) {
        return Double.compare(this.getFitness(), i.getFitness());
    }

    public Evaluable fitnessEvaluator() {
        return this.fitnessEvaluator;
    }
}
