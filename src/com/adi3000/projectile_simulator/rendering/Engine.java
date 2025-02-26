package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.main.Game;
import com.adi3000.projectile_simulator.math.Matrix;
import com.adi3000.projectile_simulator.math.Vector3;
import com.adi3000.projectile_simulator.math.Vector4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

public class Engine {
    
    private double zFar = 100;
    private double zNear = 0.1;
    private double fov = 80;
    
    private Mesh mesh = new Mesh();
    
    private Matrix perspectiveProjection = new Matrix(new double[][] {
            {1 / (Math.tan(Math.toRadians(fov) / 2) * Game.ASPECT_RATIO), 0, 0, 0},
            {0, 1 / Math.tan(Math.toRadians(fov) / 2), 0, 0},
            {0, 0, (-zFar - zNear) / (zNear - zFar), (zNear * zFar * 2) / (zNear - zFar)},
            {0, 0, 1, 0}
    });
    
    public Engine() {
        mesh.addVertex(new Vector3(-0.6, 0.6, 1));
        mesh.addVertex(new Vector3(0.6, 0.6, 1));
        mesh.addVertex(new Vector3(-0.6, -0.6, 1));
        mesh.addVertex(new Vector3(0.6, -0.6, 1));
        
        mesh.addFace(new int[] {0, 1, 2});
        mesh.addFace(new int[] {3, 2, 1});
    }
    
    public void render(BufferedImage image) {
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        ArrayList<Vector3> vertices = new ArrayList<>();
        
        for (Vector3 vertex : mesh.vertices) {
            Vector4 clip = perspectiveProjection.transform(new Vector4(vertex, 1));
            
            Vector3 ndc = clip.div(clip.w).toVector3();
            
            double x = (ndc.x + 1) * Game.WIDTH / 2;
            double y = (-ndc.y + 1) * Game.HEIGHT / 2;
            
            vertices.add(new Vector3(x, y, 0));
            
        }
        
        for (Face face : mesh.faces) {
            ArrayList<Vector3> faceVertices = face.getFaceVertices(vertices);
            Rectangle boundingBox = face.getTriangleBoundingBox(faceVertices);
            
            for (int i = boundingBox.x; i < boundingBox.x + boundingBox.width; i++) {
                for (int j = boundingBox.y; j < boundingBox.y + boundingBox.height; j++) {
                    
                    if (face.isPointInTriangle(faceVertices, new Vector3(i, j, 0)) && (i >= 0 && i < Game.WIDTH && j >= 0 && j < Game.HEIGHT)) {
                        Vector3 barycentricCoords = face.getBarycentricCoords(faceVertices, new Vector3(i, j, 0));
                        pixels[i + j * Game.WIDTH] = new Color((int) (barycentricCoords.x * 255), (int) (barycentricCoords.y * 255), (int) (barycentricCoords.z * 255)).getRGB();
//                        pixels[i + j * Game.WIDTH] = 0xFF000000;
                    }
                }
            }
        }
    }
    
}
