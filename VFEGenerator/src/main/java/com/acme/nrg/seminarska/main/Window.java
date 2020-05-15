/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.nrg.seminarska.main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author anei
 */
public class Window extends JFrame implements ChangeListener, ActionListener {
    
    private String fileName;
    
    public Window() {
        this.initComponents();
    }
    
    private void initComponents() {
        this.fileName = "";
        
        this.setTitle("Procedural VFE generator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setResizable(false);
        
        chooser = new JFileChooser();
        
        JPanel mainPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        mainPanel.setSize(this.getSize());
        
        mainPanel.add(new JLabel("Width (x):"));
        widthSpinner = new JSpinner(new SpinnerNumberModel(200, 2, Integer.MAX_VALUE, 1));
        mainPanel.add(widthSpinner);
        
        mainPanel.add(new JLabel("Height (y):"));
        heightSpinner = new JSpinner(new SpinnerNumberModel(20, 2, Integer.MAX_VALUE, 1));
        heightSpinner.addChangeListener(this);
        mainPanel.add(heightSpinner);
        
        mainPanel.add(new JLabel("Depth (z):"));
        depthSpinner = new JSpinner(new SpinnerNumberModel(200, 2, Integer.MAX_VALUE, 1));
        mainPanel.add(depthSpinner);
        
        mainPanel.add(new JLabel("Fluid height:"));
        Integer maxHeight = (Integer) heightSpinner.getValue();
        fluidHeightSpinner = new JSpinner(new SpinnerNumberModel(10, 1, maxHeight.intValue() - 1, 1));
        mainPanel.add(fluidHeightSpinner);
        
        mainPanel.add(new JLabel("Seed:"));
        seedSpinner = new JSpinner(new SpinnerNumberModel(25, 1, Integer.MAX_VALUE, 1));
        mainPanel.add(seedSpinner);
        
        mainPanel.add(new JLabel("Viscosity:"));
        viscSpinner = new JSpinner(new SpinnerNumberModel(0.8, 0, Double.MAX_VALUE, 0.01));
        mainPanel.add(viscSpinner);
        
        mainPanel.add(new JLabel("Diffusion:"));
        diffSpinner = new JSpinner(new SpinnerNumberModel(0.1, 0, Double.MAX_VALUE, 0.01));
        mainPanel.add(diffSpinner);
        
        mainPanel.add(new JLabel("Time:"));
        timeField = new JTextField("0.00005");
        timeField.setHorizontalAlignment(JTextField.RIGHT);
        timeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
                    timeField.setEditable(true);
                } else {
                    if (ke.getKeyChar() == '.' && timeField.getText().length() > 0)
                        timeField.setEditable(true);
                    else if (ke.getKeyChar() == '\b')
                        timeField.setEditable(true);
                    else
                        timeField.setEditable(false);
                }
            }
        });
        mainPanel.add(timeField);
        
        mainPanel.add(new JLabel("Iterations:"));
        iterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        mainPanel.add(iterSpinner);
        
        
        mainPanel.add(new JLabel("Output file:"));
        fileButton = new JButton("Select file");
        fileButton.addActionListener(this);
        mainPanel.add(fileButton);
        
        statusLabel = new JLabel("Status: ");
        mainPanel.add(statusLabel);
        
        generateButton = new JButton("Generate");
        generateButton.addActionListener(this);
        mainPanel.add(generateButton);
        
        this.add(mainPanel);
    }
    
    public void finish() {
        statusLabel.setText("Status: finished.");
    }
    
    public static void main(String args[]) {
        try {
            
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Window().setVisible(true);
        });
    }
    
    /*
        Variable declaration
    */
    JLabel statusLabel;
    JSpinner widthSpinner, heightSpinner, depthSpinner, fluidHeightSpinner, seedSpinner, viscSpinner, diffSpinner, iterSpinner;
    JTextField timeField;
    JButton generateButton, fileButton;
    JFileChooser chooser;

    @Override
    public void stateChanged(ChangeEvent e) {
        Integer heightValue = (Integer) heightSpinner.getValue();
        Integer fluidValue = (Integer) fluidHeightSpinner.getValue();
        
        if (heightValue < fluidValue) {
            fluidHeightSpinner.setModel(new SpinnerNumberModel(heightValue.intValue() / 2, 1, heightValue.intValue() - 1, 1));
        } else {
            fluidHeightSpinner.setModel(new SpinnerNumberModel(fluidValue.intValue(), 1, heightValue.intValue() - 1, 1));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            if (!this.fileName.equals("")) {
                Integer width = (Integer) widthSpinner.getValue();
                Integer height = (Integer) heightSpinner.getValue();
                Integer depth = (Integer) depthSpinner.getValue();
                Integer fluidHeight = (Integer) fluidHeightSpinner.getValue();
                Integer seed = (Integer) seedSpinner.getValue();
                Double viscosity = (Double) viscSpinner.getValue();
                Double diffusion = (Double) diffSpinner.getValue();
                float time = Float.parseFloat(timeField.getText());
                Integer iterations = (Integer) iterSpinner.getValue();

                statusLabel.setText("Status: generating VFE...");

                Program p = new Program(this, this.fileName, width, height, depth, fluidHeight, seed, viscosity.floatValue(), diffusion.floatValue(), time, iterations);
                p.start();
            } else {
                statusLabel.setText("Status: no output file selected!");
            }
        } else {
            int val = chooser.showSaveDialog(this);
            
            if (val == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                this.fileName = file.getAbsolutePath();
                
                if (!fileName.endsWith(".raw")) {
                    statusLabel.setText("Status: file must be of .raw format.");
                    this.fileName = "";
                } else {
                    statusLabel.setText("Status: selected " + file.getName());
                }
            }
        }
    }
}
