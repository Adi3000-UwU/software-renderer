package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.main.Game;
import com.adi3000.projectile_simulator.math.Matrix;
import com.adi3000.projectile_simulator.math.Vector3;
import com.adi3000.projectile_simulator.math.Vector4;

import java.awt.*;
import java.util.ArrayList;

public class Engine {
    
    private ArrayList<Vector3> vertices = new ArrayList<>();
    
    private double zFar = 100;
    private double zNear = 0.1;
    private double fov = 80;
    
    private Matrix perspectiveProjection = new Matrix(new double[][] {
            {1 / (Math.tan(Math.toRadians(fov) / 2) * Game.ASPECT_RATIO), 0, 0, 0},
            {0, 1 / Math.tan(Math.toRadians(fov) / 2), 0, 0},
            {0, 0, (-zFar - zNear) / (zNear - zFar), (zNear * zFar * 2) / (zNear - zFar)},
            {0, 0, 1, 0}
    });
    
    public Engine() {
        vertices.add(new Vector3(0, 0, 0.1));
        vertices.add(new Vector3(0, 1, 0.1));
        vertices.add(new Vector3(1, 0, 0.1));
        
    }
    
    public void render(Graphics2D g2d) {
        Polygon polygon = new Polygon();
        
        for (Vector3 vertex : vertices) {
            Vector4 clip = perspectiveProjection.transform(new Vector4(vertex, 1));
            
            Vector3 ndc = clip.div(clip.w).toVector3();
            
            int x = (int) ((ndc.x + 1) * Game.WIDTH / 2);
            int y = (int) ((-ndc.y + 1) * Game.HEIGHT / 2);
            
            
//            System.out.println();
//            System.out.println("vertex: " + vertex);
//            System.out.println("matrix: " + perspectiveProjection);
//            System.out.println("clip: " + clip);
//            System.out.println("ndc: " + ndc);
            
            
            polygon.addPoint(x, y);
            
            vertex.z += 0.001;
        }
        
        
        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(polygon);
    }
    
}
