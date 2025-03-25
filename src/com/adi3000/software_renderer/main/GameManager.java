package com.adi3000.software_renderer.main;

import com.adi3000.software_renderer.math.EulerAngle;
import com.adi3000.software_renderer.math.Quaternion;
import com.adi3000.software_renderer.math.Vector3;
import com.adi3000.software_renderer.rendering.Camera;
import com.adi3000.software_renderer.rendering.Engine;
import com.adi3000.software_renderer.rendering.Mesh;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GameManager {
    
    public Engine engine;
    
    private Camera camera;
    private ArrayList<Mesh> meshes = new ArrayList<>();
    
    
    public GameManager() {
        engine = new Engine();
        
        camera = new Camera(new Vector3(0, 0, 0), new EulerAngle(0, 0, 0).toRad());
        
        
        meshes.add(new Mesh(new Vector3(0, 0, 4), new Quaternion(new EulerAngle(0, 0, 0).toRad()), "cube.obj"));
//        meshes.add(new Mesh(new Vector3(3, 0, 6), new Quaternion(new EulerAngle(45.26, 0, 35.26).toRad()), "cube.obj"));

//        meshes.add(new Mesh(new Vector3(-3, 0, 7), new Quaternion(new EulerAngle(0, 0, 0).toRad()), "plane.obj", "amongus.png"));
    }
    
    
    public void tick() {
//        meshes.get(1).rotation.rotate(new Quaternion(new EulerAngle(0, 1.2, 0).toRad()));
//        meshes.get(2).rotation.rotate(new Quaternion(new EulerAngle(0, 1.2, 0).toRad()));
        
        camera.tick();
    }
    
    public void render(BufferedImage image) {
        engine.render(image, camera, meshes);
        
    }
    
}
