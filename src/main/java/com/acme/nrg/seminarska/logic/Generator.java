/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acme.nrg.seminarska.logic;

import com.acme.nrg.seminarska.data.Volume;
import com.jayfella.fastnoise.FastNoise;
import java.util.Random;

/**
 *
 * @author anei
 */
public class Generator {
    
    
    public static void generate(Volume volume, int height) {
        Random r = new Random();
        FastNoise noise = new FastNoise(r.nextInt());
        noise.SetNoiseType(FastNoise.NoiseType.Perlin);
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < volume.getY(); y++) {
                for (int z = 0; z < volume.getZ(); z++) {
                    float noiseValue = (noise.GetNoise(x, y, z) + 1.0f) / 2.0f * Volume.MAX_VALUE;
                    volume.grid[x][y][z] = Volume.MAX_VALUE / 2.0f;
                    volume.gridPrev[x][y][z] = noiseValue;
                }
            }
        }
        
//        FastNoise noiseX = new FastNoise(r.nextInt());
//        noiseX.SetNoiseType(FastNoise.NoiseType.Perlin);
//        
//        FastNoise noiseY = new FastNoise(r.nextInt());
//        noiseY.SetNoiseType(FastNoise.NoiseType.Perlin);
//        
//        FastNoise noiseZ = new FastNoise(r.nextInt());
//        noiseZ.SetNoiseType(FastNoise.NoiseType.Perlin);
        
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < volume.getY(); y++) {
                for (int z = 0; z < volume.getZ(); z++) {
//                    float noiseValue = noiseX.GetNoise(x, y, z) / 2.0f;  // (noise.GetNoise(x, y, z) + 1.0f) / 2.0f
//                    float noiseValue = (noiseX.GetNoise(x, y, z) + 1.0f) / 2.0f;
                    float noiseValue = 1 - 2 * r.nextFloat();
//                    volume.velocityX[x][y][z] = noiseValue;
                    volume.velocityXPrev[x][y][z] = noiseValue;
                    
//                    noiseValue = noiseY.GetNoise(x, y, z) / 2.0f;  // (noise.GetNoise(x, y, z) + 1.0f) / 2.0f
//                    noiseValue = (noiseY.GetNoise(x, y, z) + 1.0f) / 2.0f;
                    noiseValue = 1 - 2 * r.nextFloat();
//                    volume.velocityY[x][y][z] = noiseValue;
                    volume.velocityYPrev[x][y][z] = noiseValue;

//                    noiseValue = noiseZ.GetNoise(x, y, z) / 2.0f;  // (noise.GetNoise(x, y, z) + 1.0f) / 2.0f
//                    noiseValue = (noiseZ.GetNoise(x, y, z) + 1.0f) / 2.0f;
                    noiseValue = 1 - 2 * r.nextFloat();
//                    volume.velocityZ[x][y][z] = noiseValue;
                    volume.velocityZPrev[x][y][z] = noiseValue;
                }
            }
        }
    }
    
    public static void addSource(Volume volume, float time, boolean calcPressure) {
        for (int x = 0; x < volume.getX(); x++) {
            for (int y = 0; y < volume.getY(); y++) {
                for (int z = 0; z < volume.getZ(); z++) {
                    if (calcPressure) {
                        volume.grid[x][y][z] += time * volume.gridPrev[x][y][z];
                    } else {
                        volume.velocityX[x][y][z] += time * volume.velocityXPrev[x][y][z];
                        volume.velocityY[x][y][z] += time * volume.velocityYPrev[x][y][z];
                        volume.velocityZ[x][y][z] += time * volume.velocityZPrev[x][y][z];
                    }
                }
            }
        }
    }
    
    public static void spread(Volume volume, float factor, float time, boolean calcPressure) {
        float speed = factor * time * volume.numPoints();
        
        for (int i = 0; i < 20; i++) {
            for (int x = 1; x < volume.getX() - 1; x++) {
                for (int y = 1; y < volume.getY() - 1; y++) {
                    for (int z = 1; z < volume.getZ() - 1; z++) {
                        if (calcPressure) {
                            volume.grid[x][y][z] = (volume.gridPrev[x][y][z] + speed * (volume.grid[x-1][y][z] + volume.grid[x+1][y][z] +
                                                                                        volume.grid[x][y-1][z] + volume.grid[x][y+1][z] +
                                                                                        volume.grid[x][y][z-1] + volume.grid[x][y][z+1])) / (1 + 6 * speed);
                        } else {
                            volume.velocityX[x][y][z] = (volume.velocityXPrev[x][y][z] + speed * (volume.velocityX[x-1][y][z] + volume.velocityX[x+1][y][z] +
                                                                                                  volume.velocityX[x][y-1][z] + volume.velocityX[x][y+1][z] +
                                                                                                  volume.velocityX[x][y][z-1] + volume.velocityX[x][y][z+1])) / (1 + 6 * speed);
                            volume.velocityY[x][y][z] = (volume.velocityYPrev[x][y][z] + speed * (volume.velocityY[x-1][y][z] + volume.velocityY[x+1][y][z] +
                                                                                                  volume.velocityY[x][y-1][z] + volume.velocityY[x][y+1][z] +
                                                                                                  volume.velocityY[x][y][z-1] + volume.velocityY[x][y][z+1])) / (1 + 6 * speed);
                            volume.velocityZ[x][y][z] = (volume.velocityZPrev[x][y][z] + speed * (volume.velocityZ[x-1][y][z] + volume.velocityZ[x+1][y][z] +
                                                                                                  volume.velocityZ[x][y-1][z] + volume.velocityZ[x][y+1][z] +
                                                                                                  volume.velocityZ[x][y][z-1] + volume.velocityZ[x][y][z+1])) / (1 + 6 * speed);
                        }
                    }
                }
            }
            
//            bound(volume, calcPressure);
            if (calcPressure) {
                bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, volume.grid);
            } else {
                bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.X, volume.velocityX);
                bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Y, volume.velocityY);
                bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Z, volume.velocityZ);
            }
        }
    }
    
    private static void bound(int X, int Y, int Z, Volume.Boundary boundary, float grid[][][]) {
        // X side planes
        for (int y = 1; y < Y - 1; y++) {
            for (int z = 1; z < Z - 1; z++) {
                grid[0][y][z] = (boundary == Volume.Boundary.X) ? -grid[1][y][z] : grid[1][y][z];
                grid[X - 1][y][z] = (boundary == Volume.Boundary.X) ? -grid[X - 2][y][z] : grid[X - 2][y][z];
            }
        }
        
        // Y side planes
        for (int x = 1; x < X - 1; x++) {
            for (int z = 1; z < Z - 1; z++) {
                grid[x][0][z] = (boundary == Volume.Boundary.Y) ? -grid[x][1][z] : grid[x][1][z];
                grid[x][Y - 1][z] = (boundary == Volume.Boundary.Y) ? -grid[x][Y - 2][z] : grid[x][Y - 2][z];
            }
        }
        
        // Z side planes
        for (int x = 1; x < X - 1; x++) {
            for (int y = 1; y < Y - 1; y++) {
                grid[x][y][0] = (boundary == Volume.Boundary.Z) ? -grid[x][y][1] : grid[x][y][1];
                grid[x][y][Z - 1] = (boundary == Volume.Boundary.Z) ? -grid[x][y][Z - 2] : grid[x][y][Z - 2];
            }
        }
        
        // corner points
        grid[0][0][0] = (grid[1][0][0] + grid[0][1][0] + grid[0][0][1]) / 2;
        grid[X - 1][0][0] = (grid[X - 2][0][0] + grid[X - 1][1][0] + grid[X - 1][0][1]) / 2;
        grid[0][Y - 1][0] = (grid[1][Y - 1][0] + grid[0][Y - 2][0] + grid[0][Y - 1][1]) / 2;
        grid[0][0][Z - 1] = (grid[1][0][Z - 1] + grid[0][1][Z - 1] + grid[0][0][Z - 2]) / 2;
        grid[X - 1][Y - 1][0] = (grid[X - 2][Y - 1][0] + grid[X - 1][Y - 2][0] + grid[X - 1][Y - 1][1]) / 2;
        grid[X - 1][0][Z - 1] = (grid[X - 2][0][Z - 1] + grid[X - 1][1][Z - 1] + grid[X - 1][0][Z - 2]) / 2;
        grid[0][Y - 1][Z - 1] = (grid[1][Y - 1][Z - 1] + grid[0][Y - 2][Z - 1] + grid[0][Y - 1][Z - 2]) / 2;
        grid[X - 1][Y - 1][Z - 1] = (grid[X - 2][Y - 1][Z - 1] + grid[X - 1][Y - 2][Z - 1] + grid[X - 1][Y - 1][Z - 2]) / 2;
    }
    
    public static void move(Volume volume, float time, boolean calcPressure) {
        float t = time * volume.getY();
        float valX, valY, valZ, x0, x1, y0, y1, z0, z1;
        int xn, xn1, yn, yn1, zn, zn1;
        
        for (int x = 1; x < volume.getX() - 1; x++) {
            for (int y = 1; y < volume.getY() - 1; y++) {
                for (int z = 1; z < volume.getZ() - 1; z++) {
                    if (calcPressure) {
                        valX = x - t * volume.velocityX[x][y][z];
                        valY = y - t * volume.velocityY[x][y][z];
                        valZ = z - t * volume.velocityZ[x][y][z];
                    } else {
                        valX = x - t * volume.velocityXPrev[x][y][z];
                        valY = y - t * volume.velocityYPrev[x][y][z];
                        valZ = z - t * volume.velocityZPrev[x][y][z];
                    }
                    
                    
                    if (valX < 0.5f)
                        valX = 0.5f;
                    else if (valX > (float) volume.getX() + 0.5f)
                        valX = (float) volume.getX() + 0.5f;
                    
                    xn = (int) valX;
                    xn1 = xn + 1;
                    
                    if (valY < 0.5f)
                        valY = 0.5f;
                    else if (valY > (float) volume.getY() + 0.5f)
                        valY = (float) volume.getY() + 0.5f;
                    
                    yn = (int) valY;
                    yn1 = yn + 1;
                    
                    if (valZ < 0.5f)
                        valZ = 0.5f;
                    else if (valZ > (float) volume.getZ() + 0.5f)
                        valZ = (float) volume.getZ() + 0.5f;
                    
                    zn = (int) valZ;
                    zn1 = zn + 1;
                    
                    
                    x1 = valX - xn;
                    x0 = 1 - x1;
                    y1 = valY - yn;
                    y0 = 1 - y1;
                    z1 = valZ - zn;
                    z0 = 1 - z1;
                    
                    if (calcPressure) {
                        volume.grid[x][y][z] = (int)(x0 * (y0 * (z0 * volume.gridPrev[xn][yn][zn] +
                                                                 z1 * volume.gridPrev[xn][yn][zn1]) +
                                                           y1 * (z0 * volume.gridPrev[xn][yn1][zn] +
                                                                 z1 * volume.gridPrev[xn][yn1][zn1])) +
                                                     x1 * (y0 * (z0 * volume.gridPrev[xn1][yn][zn] +
                                                                 z1 * volume.gridPrev[xn1][yn][zn1]) +
                                                           y1 * (z0 * volume.gridPrev[xn1][yn1][zn] +
                                                                 z1 * volume.gridPrev[xn1][yn1][zn1])));
                    } else {
                        volume.velocityX[x][y][z] = (int)(x0 * (y0 * (z0 * volume.velocityXPrev[xn][yn][zn] +
                                                                      z1 * volume.velocityXPrev[xn][yn][zn1]) +
                                                                y1 * (z0 * volume.velocityXPrev[xn][yn1][zn] +
                                                                      z1 * volume.velocityXPrev[xn][yn1][zn1])) +
                                                          x1 * (y0 * (z0 * volume.velocityXPrev[xn1][yn][zn] +
                                                                      z1 * volume.velocityXPrev[xn1][yn][zn1]) +
                                                                y1 * (z0 * volume.velocityXPrev[xn1][yn1][zn] +
                                                                      z1 * volume.velocityXPrev[xn1][yn1][zn1])));
                        
                        volume.velocityY[x][y][z] = (int)(x0 * (y0 * (z0 * volume.velocityYPrev[xn][yn][zn] +
                                                                      z1 * volume.velocityYPrev[xn][yn][zn1]) +
                                                                y1 * (z0 * volume.velocityYPrev[xn][yn1][zn] +
                                                                      z1 * volume.velocityYPrev[xn][yn1][zn1])) +
                                                          x1 * (y0 * (z0 * volume.velocityYPrev[xn1][yn][zn] +
                                                                      z1 * volume.velocityYPrev[xn1][yn][zn1]) +
                                                                y1 * (z0 * volume.velocityYPrev[xn1][yn1][zn] +
                                                                      z1 * volume.velocityYPrev[xn1][yn1][zn1])));
                        
                        volume.velocityZ[x][y][z] = (int)(x0 * (y0 * (z0 * volume.velocityZPrev[xn][yn][zn] +
                                                                      z1 * volume.velocityZPrev[xn][yn][zn1]) +
                                                                y1 * (z0 * volume.velocityZPrev[xn][yn1][zn] +
                                                                      z1 * volume.velocityZPrev[xn][yn1][zn1])) +
                                                          x1 * (y0 * (z0 * volume.velocityZPrev[xn1][yn][zn] +
                                                                      z1 * volume.velocityZPrev[xn1][yn][zn1]) +
                                                                y1 * (z0 * volume.velocityZPrev[xn1][yn1][zn] +
                                                                      z1 * volume.velocityZPrev[xn1][yn1][zn1])));
                    }
                }
            }
        }
        
//        bound(volume, calcPressure);
        if (calcPressure) {
            bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, volume.grid);
        } else {
            bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.X, volume.velocityX);
            bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Y, volume.velocityY);
            bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Z, volume.velocityZ);
        }
    }
    
//    public static void project(Volume volume) {
//        float h = 1.0f / volume.getY();
//        float div[][][] = new float[volume.getX()][volume.getY()][volume.getZ()];
//        float p[][][] = new float[volume.getX()][volume.getY()][volume.getZ()];
//        
//        for (int i = 1; i < volume.getX() - 1; i++) {
//            for (int j = 1; j < volume.getY() - 1; j++) {
//                for (int k = 1; k < volume.getZ() - 1; k++) {
//                    div[i][j][k] = -0.5f * h * (volume.velocityX[i+1][j][k] - volume.velocityX[i-1][j][k] +
//                                                volume.velocityY[i][j+1][k] - volume.velocityY[i][j-1][k] +
//                                                volume.velocityZ[i][j][k+1] - volume.velocityZ[i][j][k-1]);
//                }
//            }
//        }
//        
//        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, div);
//        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, p);
//        
//        for (int i = 0; i < 20; i++) {
//            for (int x = 1; x < volume.getX() - 1; x++) {
//                for (int y = 1; y < volume.getY() - 1; y++) {
//                    for (int z = 1; z < volume.getZ() - 1; z++) {
//                        p[x][y][z] = (div[x][y][z] + p[x-1][y][z] + p[x+1][y][z] +
//                                                     p[x][y-1][z] + p[x][y+1][z] +
//                                                     p[x][y][z-1] + p[x][y][z+1]) / 6;
//                    }
//                }
//            }
//            
//            bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, p);
//        }
//        
//        for (int x = 1; x < volume.getX() - 1; x++) {
//            for (int y = 1; y < volume.getY() - 1; y++) {
//                for (int z = 1; z < volume.getZ() - 1; z++) {
//                    volume.velocityX[x][y][z] -= 0.5f * (p[x+1][y][z] - p[x-1][y][z]) / h;
//                    volume.velocityY[x][y][z] -= 0.5f * (p[x][y+1][z] - p[x][y-1][z]) / h;
//                    volume.velocityZ[x][y][z] -= 0.5f * (p[x][y][z+1] - p[x][y][z-1]) / h;
//                }
//            }
//        }
//        
//        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.X, volume.velocityX);
//        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Y, volume.velocityY);
//        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Z, volume.velocityZ);
//    }
    
    public static void project(Volume volume) {
        float h = 1.0f / volume.getY();
        
        for (int i = 1; i < volume.getX() - 1; i++) {
            for (int j = 1; j < volume.getY() - 1; j++) {
                for (int k = 1; k < volume.getZ() - 1; k++) {
                    volume.velocityXPrev[i][j][k] = 0.0f;
                    volume.velocityYPrev[i][j][k] = -0.5f * h * (volume.velocityX[i+1][j][k] - volume.velocityX[i-1][j][k] +
                                                                 volume.velocityY[i][j+1][k] - volume.velocityY[i][j-1][k] +
                                                                 volume.velocityZ[i][j][k+1] - volume.velocityZ[i][j][k-1]);
                    volume.velocityZPrev[i][j][k] = -0.5f * h * (volume.velocityX[i+1][j][k] - volume.velocityX[i-1][j][k] +
                                                                 volume.velocityY[i][j+1][k] - volume.velocityY[i][j-1][k] +
                                                                 volume.velocityZ[i][j][k+1] - volume.velocityZ[i][j][k-1]);
                }
            }
        }
        
        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, volume.velocityXPrev);
        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, volume.velocityYPrev);
        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, volume.velocityZPrev);
        
        for (int i = 0; i < 20; i++) {
            for (int x = 1; x < volume.getX() - 1; x++) {
                for (int y = 1; y < volume.getY() - 1; y++) {
                    for (int z = 1; z < volume.getZ() - 1; z++) {
                        volume.velocityXPrev[x][y][z] = (volume.velocityYPrev[x][y][z] + volume.velocityXPrev[x-1][y][z] + volume.velocityXPrev[x+1][y][z] +
                                                         volume.velocityXPrev[x][y-1][z] + volume.velocityXPrev[x][y+1][z] +
                                                         volume.velocityXPrev[x][y][z-1] + volume.velocityXPrev[x][y][z+1]) / 6;
                    }
                }
            }
            
            bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.NONE, volume.velocityXPrev);
        }
        
        for (int x = 1; x < volume.getX() - 1; x++) {
            for (int y = 1; y < volume.getY() - 1; y++) {
                for (int z = 1; z < volume.getZ() - 1; z++) {
                    volume.velocityX[x][y][z] -= 0.5f * (volume.velocityXPrev[x+1][y][z] - volume.velocityXPrev[x-1][y][z]) / h;
                    volume.velocityY[x][y][z] -= 0.5f * (volume.velocityXPrev[x][y+1][z] - volume.velocityXPrev[x][y-1][z]) / h;
                    volume.velocityZ[x][y][z] -= 0.5f * (volume.velocityXPrev[x][y][z+1] - volume.velocityXPrev[x][y][z-1]) / h;
                }
            }
        }
        
        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.X, volume.velocityX);
        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Y, volume.velocityY);
        bound(volume.getX(), volume.getY(), volume.getZ(), Volume.Boundary.Z, volume.velocityZ);
    }
    
}
