import java.util.*;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private int geneLength;
    private List<Individual> population;
    private int generation;
    private Individual best;

    private Queue<List<Individual>> recentHistory;
    private final int MAX_HISTORY = 10;

    public GeneticAlgorithm(int populationSize, double mutationRate, int geneLength) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.geneLength = geneLength;
        this.population = new ArrayList<>();
        this.generation = 0;

        this.recentHistory = new LinkedList<>();
        initializePopulation();
        updateBest();
        saveToHistory();
    }

    private void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            population.add(new Individual(geneLength));
        }
    }

    public void evolve() {
        List<Individual> newPopulation = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Individual parent1 = selectParent();
            Individual parent2 = selectParent();
            Individual child = crossover(parent1, parent2);
            child.mutate(mutationRate);
            newPopulation.add(child);
        }
        population = newPopulation;
        generation++;
        updateBest();
        saveToHistory();
    }

    private void saveToHistory() {
        if (recentHistory.size() >= MAX_HISTORY) {
            recentHistory.poll();  // Remove oldest generation
        }
        recentHistory.offer(new ArrayList<>(population)); // Add current generation
    }

    private Individual selectParent() {
        Random rand = new Random();
        Individual a = population.get(rand.nextInt(populationSize));
        Individual b = population.get(rand.nextInt(populationSize));
        return a.getFitness() > b.getFitness() ? a : b;
    }

    private Individual crossover(Individual p1, Individual p2) {
        int point = new Random().nextInt(geneLength);
        String part1 = p1.getGenes().substring(0, point);
        String part2 = p2.getGenes().substring(point);
        return new Individual(part1 + part2);
    }

    private void updateBest() {
        best = Collections.max(population, Comparator.comparingInt(Individual::getFitness));
    }

    public Queue<List<Individual>> getRecentHistory() {
        return recentHistory;
    }

    public Individual getBestIndividual() {
        return best;
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public int getGeneration() {
        return generation;
    }
}
