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
public class Program {
    
    public static void main(String[] args) {
        Volume volume = new Volume(20, 200, 200);
        Generator.generate(volume, 10);
        
        // good params - viscosity: 0.8f; diffusion: 0.1f; time: 0.00005f
        for (int i = 0; i < 1; i++) {
            processVelocity(volume, 0.8f, 0.00005f);
            processPressure(volume, 0.1f, 0.00005f);
        }
        
        VolFileWriter writer = new VolFileWriter("Test.vol");
        writer.writeGrid(volume);
    }
    
    private static void processPressure(Volume volume, float diffusionFactor, float time) {
        Generator.addSource(volume, time, true);
        volume.swapGrids();
        Generator.spread(volume, diffusionFactor, time, true);
        volume.swapGrids();
        Generator.move(volume, time, true);
    }
    
    private static void processVelocity(Volume volume, float viscosity, float time) {
        Generator.addSource(volume, time, false);
        volume.swapVelocity();
        Generator.spread(volume, viscosity, time, false);
        Generator.project(volume);
        volume.swapVelocity();
        Generator.move(volume, time, false);
        Generator.project(volume);
    }
}
