/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.nrg.seminarska.data;

import java.util.Random;

/**
 *
 * @author anei
 */
public class Volume {
    
    public static final float MAX_VALUE = 256.0f;
    public static enum Boundary {NONE, X, Y, Z}
    
    private final int X;
    private final int Y;
    private final int Z;
    
    public float gridPrev[][][];
    public float grid[][][];
    
    public float velocityXPrev[][][];
    public float velocityYPrev[][][];
    public float velocityZPrev[][][];
    
    public float velocityX[][][];
    public float velocityY[][][];
    public float velocityZ[][][];
    
    public Volume(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
        
        this.gridPrev = new float[x][y][z];
        this.grid = new float[x][y][z];
        
        this.velocityXPrev = new float[x][y][z];
        this.velocityYPrev = new float[x][y][z];
        this.velocityZPrev = new float[x][y][z];
        
        this.velocityX = new float[x][y][z];
        this.velocityY = new float[x][y][z];
        this.velocityZ = new float[x][y][z];
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getZ() {
        return Z;
    }
    
    public int numPoints() {
        return this.X * this.Y * this.Z;
    }
    
    public void swapGrids() {
        float temp[][][] = this.gridPrev;
        this.gridPrev = this.grid;
        this.grid = temp;
    }
    
    public void swapVelocity() {
        float temp[][][] = this.velocityXPrev;
        this.velocityXPrev = this.velocityX;
        this.velocityX = temp;
        
        temp = this.velocityYPrev;
        this.velocityYPrev = this.velocityY;
        this.velocityY = temp;
        
        temp = this.velocityZPrev;
        this.velocityZPrev = this.velocityZ;
        this.velocityZ = temp;
    }
}
