/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.nrg.seminarska.data;

/**
 *
 * @author anei
 */
public class Velocity {
    
    public final float direction[];
    
    public Velocity(float x, float y, float z) {
        this.direction = new float[3];
        this.direction[0] = x;
        this.direction[1] = y;
        this.direction[2] = z;
    }
}
