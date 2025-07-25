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
import java.util.Random;
import java.util.List;
import java.util.Random;

public class Individual {
    private String genes;
    private int fitness;

    public Individual(int length) {
        genes = generateRandomGenes(length);
        calculateFitness();
    }

    public Individual(String genes) {
        this.genes = genes;
        calculateFitness();
    }

    private String generateRandomGenes(int length) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(rand.nextBoolean() ? '1' : '0');
        }
        return sb.toString();
    }

    public void mutate(double mutationRate) {
        StringBuilder sb = new StringBuilder(genes);
        Random rand = new Random();
        for (int i = 0; i < sb.length(); i++) {
            if (rand.nextDouble() < mutationRate) {
                sb.setCharAt(i, sb.charAt(i) == '1' ? '0' : '1');
            }
        }
        genes = sb.toString();
        calculateFitness();
    }

    private void calculateFitness() {
        fitness = 0;
        for (char c : genes.toCharArray()) {
            if (c == '1') fitness++;
        }
    }

    public String getGenes() {
        return genes;
    }

    public int getFitness() {
        return fitness;
    }
}
