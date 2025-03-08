package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector2;
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
    
    public void moveCamera(Vector3 v) {
        Vector3 eulerAngle = rotation.toEulerAngle();
        
        Vector2 plane = new Vector2(v.x, v.z);
        plane.rotate(-eulerAngle.y);
        
        position = position.add(new Vector3(plane.x, v.y, plane.y));
    }
    
}
