package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.math.Vector3;

import java.util.ArrayList;

public class Mesh {

    public ArrayList<Vector3> vertices = new ArrayList<>();
    public ArrayList<Face> faces = new ArrayList<>();
    
    public void addVertex(Vector3 vertex) {
        vertices.add(vertex);
    }
    
    public void addFace(int[] vertexIndices) {
        faces.add(new Face(vertexIndices));
    }
    
}
