package javaapplication17;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    private GeneticAlgorithm ga;
    private GeneticPanel geneticPanel;
    private Timer timer;
    private boolean running = true;

    private JButton btnStartPause, btnReset;
    private JButton btnSave;
    private JSlider speedSlider;

    private JTextField txtPopulation, txtMutation, txtGeneLength;

    public MainFrame() {
        super("Simulación Algoritmo Genético");

        // Parámetros iniciales
        int initialPop = 50;
        double initialMutation = 0.01;
        int initialGeneLength = 20;

        ga = new GeneticAlgorithm(initialPop, initialMutation, initialGeneLength);

        geneticPanel = new GeneticPanel(ga);
        add(geneticPanel, BorderLayout.CENTER);

        // Panel de controles
        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        createParameterControls(controls);

        btnStartPause = new JButton("Pausar");
        btnStartPause.addActionListener(e -> toggleRunning());
        controls.add(btnStartPause);

        btnReset = new JButton("Reiniciar");
        btnReset.addActionListener(e -> resetAlgorithm());
        controls.add(btnReset);

        btnSave = new JButton("Guardar Resultados");
        btnSave.addActionListener(e -> saveResults());
        controls.add(btnSave);

        controls.add(new JLabel("Velocidad (ms):"));
        speedSlider = new JSlider(10, 1000, 200);
        speedSlider.setMajorTickSpacing(200);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(e -> {
            timer.setDelay(speedSlider.getValue());
        });
        controls.add(speedSlider);

        add(controls, BorderLayout.SOUTH);

        timer = new Timer(speedSlider.getValue(), e -> {
            if (running) {
                ga.evolve();
                geneticPanel.repaint();
            }
        });
        timer.start();

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void toggleRunning() {
        running = !running;
        btnStartPause.setText(running ? "Pausar" : "Iniciar");
    }

    private void resetAlgorithm() {
        try {
            int pop = Integer.parseInt(txtPopulation.getText());
            double mut = Double.parseDouble(txtMutation.getText());
            int geneLen = Integer.parseInt(txtGeneLength.getText());

            timer.stop();
            ga = new GeneticAlgorithm(pop, mut, geneLen);
            geneticPanel.setGeneticAlgorithm(ga);
            geneticPanel.repaint();
            timer.start();
            running = true;
            btnStartPause.setText("Pausar");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveResults() {
        timer.stop();
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            ga.saveFitnessHistory(path);
            JOptionPane.showMessageDialog(this, "Datos guardados en:\n" + path);
        }
        if (running) timer.start();
    }

    private void createParameterControls(JPanel panel) {
        panel.add(new JLabel("Población:"));
        txtPopulation = new JTextField("50", 4);
        panel.add(txtPopulation);

        panel.add(new JLabel("Mutación:"));
        txtMutation = new JTextField("0.01", 4);
        panel.add(txtMutation);

        panel.add(new JLabel("Longitud genes:"));
        txtGeneLength = new JTextField("20", 4);
        panel.add(txtGeneLength);
    }
}
