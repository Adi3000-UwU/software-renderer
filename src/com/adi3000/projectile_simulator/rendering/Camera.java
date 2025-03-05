package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector3;

public class Camera {
    
    public Vector3 position;
    public Quaternion rotation;
    
    public double zFar = 100;
    public double zNear = 0.1;
    public double fov = 80;
    
    public Camera() {
        this(new Vector3(0, 0, 0), new Quaternion());
    }
    public Camera(Vector3 position, Quaternion rotation) {
        this.position = position;
        this.rotation = rotation;
    }
    
}
