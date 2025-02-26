package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.math.Vector2;
import com.adi3000.projectile_simulator.math.Vector3;

import java.awt.*;
import java.util.ArrayList;

public class Face {
    
    private int[] vertexIndices;
    
    public Face(int[] vertexIndices) {
        this.vertexIndices = vertexIndices;
    }
    
    public ArrayList<Vector3> getFaceVertices(ArrayList<Vector3> meshVertices) {
        ArrayList<Vector3> faceVertices = new ArrayList<>();
        
        for (int vertexIndex : vertexIndices) {
            faceVertices.add(meshVertices.get(vertexIndex));
        }
        
        return faceVertices;
    }
    
    public Vector3 getBarycentricCoords(ArrayList<Vector3> triangleVertices, Vector3 point) {
        Vector2 v0 = triangleVertices.get(1).sub(triangleVertices.get(0)).toVector2();
        Vector2 v1 = triangleVertices.get(2).sub(triangleVertices.get(0)).toVector2();
        Vector2 v2 = point.sub(triangleVertices.get(0)).toVector2();
        
        double d00 = v0.dot(v0);
        double d01 = v0.dot(v1);
        double d11 = v1.dot(v1);
        double d20 = v2.dot(v0);
        double d21 = v2.dot(v1);
        double det = (d00 * d11 - d01 * d01);
        
        double v = (d11 * d20 - d01 * d21) / det;
        double w = (d00 * d21 - d01 * d20) / det;
        double u = 1.0f - v - w;
        
        return new Vector3(u, v, w);
    }
    
    public boolean isPointInTriangle(ArrayList<Vector3> triangleVertices, Vector3 point) {
        Vector3 barycentricCoords = getBarycentricCoords(triangleVertices, point);
        return barycentricCoords.x >= 0 && barycentricCoords.y >= 0 && barycentricCoords.z >= 0;
    }
    
    public Rectangle getTriangleBoundingBox(ArrayList<Vector3> triangleVertices) {
        int x = (int) Math.min(triangleVertices.get(0).x, Math.min(triangleVertices.get(1).x, triangleVertices.get(2).x));
        int y = (int) Math.min(triangleVertices.get(0).y, Math.min(triangleVertices.get(1).y, triangleVertices.get(2).y));
        int w = (int) Math.max(triangleVertices.get(0).x, Math.max(triangleVertices.get(1).x, triangleVertices.get(2).x)) - x;
        int h = (int) Math.max(triangleVertices.get(0).y, Math.max(triangleVertices.get(1).y, triangleVertices.get(2).y)) - y;
        
        return new Rectangle(x, y, w, h);
    }
    
}
