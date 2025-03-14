package com.adi3000.projectile_simulator.main;

import com.adi3000.projectile_simulator.math.EulerAngle;
import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector3;
import com.adi3000.projectile_simulator.rendering.Camera;
import com.adi3000.projectile_simulator.rendering.Engine;
import com.adi3000.projectile_simulator.rendering.Mesh;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameManager {
    
    private Engine engine;
    
    private Camera camera;
    private ArrayList<Mesh> meshes = new ArrayList<>();
    
    private double cameraMoveSpeed = 0.1;
    private double cameraRotationSpeed = 1;
    
    
    public GameManager() {
        engine = new Engine();
        
        camera = new Camera(new Vector3(0, 0, 0), new EulerAngle(0, 0, 0).toRad());
        
        meshes.add(createCube(new Vector3(0, 0, 4), new Quaternion(new EulerAngle(0, 0, 0).toRad())));
        meshes.add(createCube(new Vector3(3, 0, 6), new Quaternion(new EulerAngle(45.26, 0, 35.26).toRad())));
    }
    
    
    public void tick() {
        meshes.get(1).rotation.rotate(new Quaternion(new EulerAngle(0, 1.2, 0).toRad()));
        
        KeyHandler keyHandler = KeyHandler.getInstance();

        camera.rotation.increment(keyHandler.cameraRotation.mult(cameraRotationSpeed).toRad());
        camera.moveCamera(keyHandler.cameraMovement.mult(cameraMoveSpeed));
        
    }
    
    public void render(BufferedImage image) {
        engine.render(image, camera, meshes);
        
    }
    
    
    private Mesh createCube(Vector3 position, Quaternion rotation) {
        Mesh mesh = new Mesh(position, rotation);
        
        mesh.addVertex(new Vector3(-1, -1, -1));
        mesh.addVertex(new Vector3(1, -1, -1));
        mesh.addVertex(new Vector3(1, 1, -1));
        mesh.addVertex(new Vector3(-1, 1, -1));
        mesh.addVertex(new Vector3(-1, -1, 1));
        mesh.addVertex(new Vector3(1, -1, 1));
        mesh.addVertex(new Vector3(1, 1, 1));
        mesh.addVertex(new Vector3(-1, 1, 1));
        
        mesh.addFace(new int[] {0, 1, 3});
        mesh.addFace(new int[] {3, 1, 2});
        
        mesh.addFace(new int[] {1, 5, 2});
        mesh.addFace(new int[] {2, 5, 6});
        
        mesh.addFace(new int[] {5, 4, 6});
        mesh.addFace(new int[] {6, 4, 7});
        
        mesh.addFace(new int[] {4, 0, 7});
        mesh.addFace(new int[] {7, 0, 3});
        
        mesh.addFace(new int[] {3, 2, 7});
        mesh.addFace(new int[] {7, 2, 6});
        
        mesh.addFace(new int[] {4, 5, 0});
        mesh.addFace(new int[] {0, 5, 1});
        
        return mesh;
    }
}
