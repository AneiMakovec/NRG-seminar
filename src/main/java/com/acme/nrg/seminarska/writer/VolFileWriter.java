/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.nrg.seminarska.writer;

import com.acme.nrg.seminarska.data.Volume;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author anei
 */
public class VolFileWriter {
    
    private DataOutputStream writer;
    
    public VolFileWriter(String fileName) {
        try {
            File file = new File(fileName);
            file.createNewFile();
            
            writer = new DataOutputStream(new FileOutputStream(file));
        } catch (IOException e) {
            writer = null;
        }
    }
    
    public void writeInt(int n) {
        if (writer != null) {
            try {
                writer.writeByte(n);
            } catch (IOException e) {}
        }
    }
    
    public void writeInteger(int n) {
        if (writer != null) {
            try {
                writer.writeInt(n);
            } catch (IOException e) {}
        }
    }
    
    public void writeGrid(Volume volume) {
        try {
//            writer.writeInt(volume.getX());
//            writer.writeInt(volume.getY());
//            writer.writeInt(volume.getZ());
            
//            for (int x = 0; x < volume.getX(); x++) {
//                for (int y = 0; y < volume.getY(); y++) {
//                    for (int z = 0; z < volume.getZ(); z++) {
//                        writer.writeByte((int)volume.grid[x][y][z]);
//                    }
//                }
//            }
            for (int z = 0; z < volume.getZ(); z++) {
                for (int x = 0; x < volume.getX(); x++) {
                    for (int y = 0; y < volume.getY(); y++) {
                        writer.writeByte((int)volume.grid[x][y][z]);
                    }
                }
            }
            
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR: Error while writing to VOL file.");
        }
    }
    
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {}
    }
}
