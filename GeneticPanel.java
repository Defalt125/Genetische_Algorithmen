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
import javax.swing.*;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class GeneticPanel extends JPanel {
    private GeneticAlgorithm ga;

    public GeneticPanel(GeneticAlgorithm ga) {
        this.ga = ga;
        setPreferredSize(new Dimension(800, 600));
    }

    public void setGeneticAlgorithm(GeneticAlgorithm ga) {
        this.ga = ga;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fondo blanco
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (ga == null || ga.getPopulation().isEmpty()) return;

        drawPopulationBars(g);
        drawAvgFitnessCurve(g);
        drawInfoText(g);
    }

    private void drawPopulationBars(Graphics g) {
        List<Individual> population = ga.getPopulation();
        int barWidth = getWidth() / population.size();
        int maxHeight = getHeight() / 3;
        int maxFitness = ga.getBestIndividual().getFitness();

        List<Individual> top3 = getTopIndividuals(3);
        Color[] colors = {Color.RED, Color.GREEN, Color.YELLOW};

        for (int i = 0; i < population.size(); i++) {
            Individual ind = population.get(i);
            int barHeight = (int) ((double) ind.getFitness() / maxFitness * maxHeight);

            // ¿Es uno de los top 3?
            int idx = top3.indexOf(ind);
            if (idx >= 0) {
                g.setColor(colors[idx]);
            } else {
                g.setColor(Color.BLUE);
            }
            g.fillRect(i * barWidth, getHeight() - barHeight - 50, barWidth - 2, barHeight);
        }

        g.setColor(Color.BLACK);
        g.drawString("Población actual (top 3 en rojo/verde/amarillo)", 10, getHeight() - maxHeight - 60);
    }


    private void drawAvgFitnessCurve(Graphics g) {
        Map<Integer, Double> avgFitness = ga.getAvgFitnessPerGeneration();
        if (avgFitness.isEmpty()) return;

        int margin = 50;
        int width = getWidth() - 2 * margin;
        int height = getHeight() / 3;
        int startY = 20;

        // Ejes
        g.setColor(Color.BLACK);
        g.drawRect(margin, startY, width, height);

        double maxAvgFitness = avgFitness.values().stream().max(Double::compareTo).orElse(1.0);
        int maxGen = ga.getGeneration();
        if (maxGen < 1) return;

        g.setColor(Color.RED);
        int prevX = margin;
        int prevY = startY + height;

        for (int gen = 0; gen <= maxGen; gen++) {
            Double val = avgFitness.get(gen);
            if (val == null) continue;

            int x = margin + (int) ((double) gen / maxGen * width);
            int y = startY + height - (int) (val / maxAvgFitness * height);

            if (gen > 0) {
                g.drawLine(prevX, prevY, x, y);
            }
            prevX = x;
            prevY = y;
        }

        g.setColor(Color.BLACK);
        g.drawString("Evolución fitness promedio (línea roja)", margin + 5, startY - 5);
    }
    private List<Individual> getTopIndividuals(int n) {
        List<Individual> sorted = new ArrayList<>(ga.getPopulation());
        sorted.sort(Comparator.comparingInt(Individual::getFitness).reversed());
        return sorted.subList(0, Math.min(n, sorted.size()));
    }
    private void drawInfoText(Graphics g) {
        g.setColor(Color.BLACK);

        int margin = 10;
        int spacing = 20;

        int yStart = 210; // ⬅️ Cambiado de 15 a 55 para que no choque con el gráfico

        g.drawString("Generación: " + ga.getGeneration(), margin, yStart);
        g.drawString("Mejor fitness: " + ga.getBestIndividual().getFitness(), margin, yStart + spacing);
        g.drawString("Mejor genes: " + ga.getBestIndividual().getGenes(), margin, yStart + spacing * 2);
    }

}

