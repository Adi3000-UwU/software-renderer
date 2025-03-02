package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector3;

import java.util.ArrayList;

public class Mesh {
    
    public ArrayList<Vector3> vertices = new ArrayList<>();
    public ArrayList<Face> faces = new ArrayList<>();
    
    public Vector3 position;
    public Quaternion rotation;
    
    public Mesh() {
        this(new Vector3(0, 0, 0), new Quaternion());
    }
    public Mesh(Vector3 position, Quaternion rotation) {
        this.position = position;
        this.rotation = rotation;
    }
    
    public void addVertex(Vector3 vertex) {
        vertices.add(vertex);
    }
    
    public void addFace(int[] vertexIndices) {
        faces.add(new Face(vertexIndices));
    }
    
}
