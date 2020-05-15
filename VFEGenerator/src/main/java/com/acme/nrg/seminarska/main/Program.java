/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.nrg.seminarska.main;

import com.acme.nrg.seminarska.data.Volume;
import com.acme.nrg.seminarska.logic.Generator;
import com.acme.nrg.seminarska.writer.VolFileWriter;

/**
 *
 * @author anei
 */
public class Program implements Runnable {
    
    private Thread thread;
    
    private Window window;
    private int x, y, z, height, seed, iterations;
    private float viscosity, diffusion, time;
    private String fileName;
    
    public Program(Window window, String fileName, int y, int x, int z, int height, int seed, float viscosity, float diffusion, float time, int iterations) {
        this.window = window;
        this.fileName = fileName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.height = height;
        this.seed = seed;
        this.viscosity = viscosity;
        this.diffusion = diffusion;
        this.time = time;
        this.iterations = iterations;
    }
    
    private void generate() {
        Volume volume = new Volume(this.x, this.y, this.z);
        Generator.generate(volume, this.height, this.seed);
        
        Generator.addSource(volume, this.time, true);
        Generator.addSource(volume, this.time, false);
        
        for (int i = 0; i < this.iterations; i++) {
            processVelocity(volume, this.viscosity, this.time);
            processPressure(volume, this.diffusion, this.time);
        }
        
        VolFileWriter writer = new VolFileWriter(fileName);
        writer.writeGrid(volume);
        
        window.finish();
    }
    
    private void processPressure(Volume volume, float diffusionFactor, float time) {
        volume.swapGrids();
        Generator.spread(volume, diffusionFactor, time, true);
        volume.swapGrids();
        Generator.move(volume, time, true);
    }
    
    private void processVelocity(Volume volume, float viscosity, float time) {
        volume.swapVelocity();
        Generator.spread(volume, viscosity, time, false);
        Generator.project(volume);
        volume.swapVelocity();
        Generator.move(volume, time, false);
        Generator.project(volume);
    }

    @Override
    public void run() {
        this.generate();
    }
    
    public void start() {
        if (this.thread == null) {
            this.thread = new Thread(this, "VFEProgram");
            this.thread.start();
        }
    }
}
