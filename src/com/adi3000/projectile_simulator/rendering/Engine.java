package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.main.Game;
import com.adi3000.projectile_simulator.math.Matrix;
import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector3;
import com.adi3000.projectile_simulator.math.Vector4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
    
    double[] emptyZBuffer = new double[Game.HEIGHT * Game.WIDTH];
    
    private Camera camera;
    
    
    private Mesh mesh;
    
    
    public Engine() {
        Arrays.fill(emptyZBuffer, 1);
        
        camera = new Camera(new Vector3(0, 0, 0), Quaternion.fromEulerAngleDegree(0, 0, 0));
        
        mesh = createCube(new Vector3(0, 0, 4), Quaternion.fromEulerAngleDegree(45.26, 0, 35.26));
        
    }
    
    public void tick() {
        mesh.rotation.rotate(Quaternion.fromEulerAngleDegree(0, 1.2, 0));
        
    }
    
    public void render(BufferedImage image) {
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        double[] zbuffer = Arrays.copyOf(emptyZBuffer, pixels.length);
        
        ArrayList<Vector3> screenVertices = new ArrayList<>();
        ArrayList<Vector3> ndcVertices = new ArrayList<>();
        
        for (Vector3 vertex : mesh.vertices) {
            Vector3 worldPos = Matrix.getWorldMatrix(mesh.position, mesh.rotation).transform(vertex);
            Vector3 viewPos = Matrix.getViewMatrix(camera.position, camera.rotation).transform(worldPos);
            Vector4 clip = Matrix.getProjectionMatrix(camera.fov, Game.ASPECT_RATIO, camera.zNear, camera.zFar).transform(viewPos.toVector4());
            
            Vector3 ndc = clip.div(clip.w).toVector3();
            
            double x = (ndc.x + 1) * Game.WIDTH / 2;
            double y = (-ndc.y + 1) * Game.HEIGHT / 2;
            
            ndcVertices.add(ndc);
            screenVertices.add(new Vector3(Math.ceil(x), Math.ceil(y), clip.w));
            
        }
        
        for (Face face : mesh.faces) {
            ArrayList<Vector3> faceVertices = face.getFaceVertices(screenVertices);
            ArrayList<Vector3> faceNdc = face.getFaceVertices(ndcVertices);
            Rectangle boundingBox = face.getTriangleBoundingBox(faceVertices);
            
            // Quick fix, TODO Add frustum culling to fix objects apearing from behind the screen
            if (faceVertices.get(0).z < -0 || faceVertices.get(1).z < 0 || faceVertices.get(2).z < 0) {
                continue;
            }
            
            if (faceNdc.get(1).sub(faceNdc.get(0)).cross(faceNdc.get(2).sub(faceNdc.get(0))).z < 0) {
                continue;
            }
            
            for (int i = Math.max(boundingBox.x, 0); i < Math.min(boundingBox.x + boundingBox.width, Game.WIDTH); i++) {
                for (int j = Math.max(boundingBox.y, 0); j < Math.min(boundingBox.y + boundingBox.height, Game.HEIGHT); j++) {
                    Vector3 barycentricCoords = face.getBarycentricCoords(faceVertices, new Vector3(i + 0.5, j + 0.5, 0));
                    
                    if (face.isPointInTriangle(barycentricCoords)) {
                        int index = i + j * Game.WIDTH;
                        
                        double newZ = barycentricCoords.x * faceNdc.get(0).z + barycentricCoords.y * faceNdc.get(1).z + barycentricCoords.z * faceNdc.get(2).z;
                        double oldZ = zbuffer[index];
                        
                        if (newZ > oldZ) {
                            continue;
                        }
                        zbuffer[index] = newZ;
                        
                        pixels[index] = new Color((int) (barycentricCoords.x * 255), (int) (barycentricCoords.y * 255), (int) (barycentricCoords.z * 255)).getRGB();
                    }
                }
            }
        }
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
