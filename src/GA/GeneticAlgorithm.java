package GA;

import GA.Entities.Evaluable;
import GA.Entities.Individual;
import GA.Entities.Population;

import static GA.Options.Variables.*;

public class GeneticAlgorithm {
    private final int populationSize;
    private final int elitismCount;
    private Population population;
    private double maxFitness;
    public int generation, bestGeneration;

    public GeneticAlgorithm(int populationSize, int elitismCount, Evaluable fitnessEvaluator) {
        this.populationSize = populationSize;
        this.elitismCount = elitismCount;
        this.population = new Population(populationSize, genomeLength, fitnessEvaluator);
        this.population.calculateFitness();
        this.generation = this.bestGeneration = 0;
    }

    public void evolve() {
        double currentFitness = this.population.getFittest().getFitness();
        if (currentFitness > this.maxFitness) {
            this.maxFitness = currentFitness;
            this.bestGeneration = this.generation;
        }

        Population nextGeneration = new Population(populationSize,genomeLength, population.fitnessEvaluator());

        // Elitism: Keep the best individuals from the current population
        Individual[] elites = new Individual[elitismCount];
        for (int i = 0; i < elitismCount; i++) {
            elites[i] = population.getFittest();
        }

        nextGeneration.setIndividuals(0, elites);

        for (int i = elitismCount; i < populationSize; i++) {
            // Selection: Choose parents for the next generation
            Individual parent1 = selectionMethod.select(population);
            Individual parent2 = selectionMethod.select(population);
            // Crossover: Combine parents genomes into new individual
            nextGeneration.setIndividual(i, crossoverMethod.crossover(parent1,parent2));
        }

        // Mutation: Apply mutation to the new generation
        for (int i = elitismCount; i < populationSize; i++) {
            mutationMethod.mutate(nextGeneration.getIndividual(i));
        }

        this.population = nextGeneration;
        this.population.calculateFitness();
        this.generation++;
    }

    public Population getPopulation() {
        return population;
    }
}