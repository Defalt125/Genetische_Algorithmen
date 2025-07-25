/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication17;

/**
 *
 * @author estudiante
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private int geneLength;
    private List<Individual> population;
    private int generation;
    private Individual best;
    private List<List<Individual>> history; 
    private Map<Integer, Double> avgFitnessPerGeneration;
    private PriorityQueue<Individual> topIndividuals;
    
    public GeneticAlgorithm(int populationSize, double mutationRate, int geneLength) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.geneLength = geneLength;
        this.population = new ArrayList<>();
        this.generation = 0;

        this.history = new ArrayList<>();
        this.avgFitnessPerGeneration = new HashMap<>();
        this.topIndividuals = new PriorityQueue<>(5, Comparator.comparingInt(Individual::getFitness));
        history = new ArrayList<>();
        history.add(new ArrayList<>(population)); 
        initPopulation();
        calculateAverageFitness();
        updateTopIndividuals();
    }

    private void initPopulation() {
        for (int i = 0; i < populationSize; i++) {
            population.add(new Individual(geneLength));
        }
        updateBest();
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
        
        history.add(new ArrayList<>(population));  // Guardar generación
        calculateAverageFitness();
        updateTopIndividuals();
    }

    private Individual selectParent() {
        Random rand = new Random();
        Individual ind1 = population.get(rand.nextInt(populationSize));
        Individual ind2 = population.get(rand.nextInt(populationSize));
        return ind1.getFitness() > ind2.getFitness() ? ind1 : ind2;
    }

    private Individual crossover(Individual parent1, Individual parent2) {
        Random rand = new Random();
        int crossoverPoint = rand.nextInt(geneLength);
        String part1 = parent1.getGenes().substring(0, crossoverPoint);
        String part2 = parent2.getGenes().substring(crossoverPoint);
        return new Individual(part1 + part2);
    }

    private void updateBest() {
        best = Collections.max(population, Comparator.comparingInt(Individual::getFitness));
    }

    private void calculateAverageFitness() {
        double total = 0;
        for (Individual ind : population) {
            total += ind.getFitness();
        }
        avgFitnessPerGeneration.put(generation, total / populationSize);
    }
    

    private void updateTopIndividuals() {
        for (Individual ind : population) {
            if (topIndividuals.size() < 5) {
                topIndividuals.offer(ind);
            } else if (ind.getFitness() > topIndividuals.peek().getFitness()) {
                topIndividuals.poll();
                topIndividuals.offer(ind);
            }
        }
    }


    public void saveFitnessHistory(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Generación,FitnessPromedio");
            for (Map.Entry<Integer, Double> entry : avgFitnessPerGeneration.entrySet()) {
                pw.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // getters para usar estas estructuras

    public List<List<Individual>> getHistory() {
        return history;
    }

    public Map<Integer, Double> getAvgFitnessPerGeneration() {
        return avgFitnessPerGeneration;
    }

    public List<Individual> getTopIndividuals() {
        return new ArrayList<>(topIndividuals);
    }

    public int getGeneration() {
        return generation;
    }

    public Individual getBestIndividual() {
        return best;
    }

    public List<Individual> getPopulation() {
        return population;
    }
}
