package Problems;

import java.util.HashSet;
import java.util.Set;

import static GA.Options.Variables.genes;
import static Problems.TSP.distanceMatrix;

public class NearestNeighborSolver {

    public static int[] solve() {
        double maxFitness = Double.MIN_VALUE;
        int[] bestSolution = new int[]{};
        for (int city : genes) {
            int[] solution = solve(city);
            double fitness =  TSP.getFitnessEvaluation().evaluateFitness(solution);
            if (fitness > maxFitness) {
                maxFitness = fitness;
                bestSolution = solution;
            }
        }
        return bestSolution;
    }


    public static int[] solve(int startCity) {
        int numCities = TSP.numCities;
        Set<Integer> visitedCities = new HashSet<>();
        visitedCities.add(startCity);

        int[] solution = new int[numCities];
        solution[0] = startCity;

        int currentCity = startCity;
        for (int i = 1; i < numCities; i++) {
            double minDistance = Double.MAX_VALUE;
            int nextCity = -1;
            for (int j = 0; j < numCities; j++) {
                if (!visitedCities.contains(j)) {
                    double distance = distanceMatrix[currentCity][j];
                    if (distance < minDistance) {
                        minDistance = distance;
                        nextCity = j;
                    }
                }
            }
            visitedCities.add(nextCity);
            solution[i] = nextCity;
            currentCity = nextCity;
        }

        return solution;
    }
}

