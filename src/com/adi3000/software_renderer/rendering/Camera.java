package com.adi3000.software_renderer.rendering;

import com.adi3000.software_renderer.main.KeyHandler;
import com.adi3000.software_renderer.math.EulerAngle;
import com.adi3000.software_renderer.math.Vector2;
import com.adi3000.software_renderer.math.Vector3;

public class Camera {
    
    public Vector3 position;
    public EulerAngle rotation;
    
    public double zFar = 100;
    public double zNear = 0.1;
    public double fov = 80;
    
    private double cameraMoveSpeed = 0.1;
    private double cameraRotationSpeed = 1 / fov;
    private double fovChangeSpeed = 1;
    private double sprintSpeedBoost = 4;
    
    public Camera() {
        this(new Vector3(), new EulerAngle());
    }
    public Camera(Vector3 position, EulerAngle rotation) {
        this.position = position;
        this.rotation = rotation;
    }
    
    public void tick() {
        KeyHandler keyHandler = KeyHandler.getInstance();
        
        fov += keyHandler.fovChange * fovChangeSpeed;
        rotateCamera(keyHandler.cameraRotation.mult(cameraRotationSpeed * fov).toRad());
        moveCamera(keyHandler.cameraMovement.mult(cameraMoveSpeed * (keyHandler.sprint ? sprintSpeedBoost : 1)));
    }
    
    public void moveCamera(Vector3 v) {
        Vector2 plane = new Vector2(v.x, v.z);
        plane.rotate(-rotation.y);
        
        position = position.add(new Vector3(plane.x, v.y, plane.y));
    }
    
    public void rotateCamera(EulerAngle angle) {
        rotation.increment(angle);
        rotation.x = Math.max(-Math.PI / 2, Math.min(rotation.x, Math.PI / 2));
    }
    
}
