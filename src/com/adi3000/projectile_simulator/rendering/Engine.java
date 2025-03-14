package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.main.Game;
import com.adi3000.projectile_simulator.math.Matrix;
import com.adi3000.projectile_simulator.math.Vector3;
import com.adi3000.projectile_simulator.math.Vector4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Arrays;

public class Engine {
    
    private double[] emptyZBuffer = new double[Game.HEIGHT * Game.WIDTH];
    private double[] zbuffer;
    private int[] pixels;
    
    private ArrayList<Vector3> screenVertices;
    private ArrayList<Vector3> ndcVertices;
    
    
    public Engine() {
        Arrays.fill(emptyZBuffer, 1);
        
    }
    
    
    public void render(BufferedImage image, Camera camera, ArrayList<Mesh> meshes) {
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        zbuffer = Arrays.copyOf(emptyZBuffer, pixels.length);
        
        Matrix viewMatrix = Matrix.getViewMatrix(camera.position, camera.rotation.toQuaternion());
        
        for(Mesh mesh: meshes) {
            screenVertices = new ArrayList<>();
            ndcVertices = new ArrayList<>();
            
            transformVertecies(mesh, camera, viewMatrix);
            drawFaces(mesh);
        }
        
    }
    
    private void transformVertecies(Mesh mesh, Camera camera, Matrix viewMatrix) {
        for (Vector3 vertex : mesh.vertices) {
            Vector3 worldPos = Matrix.getWorldMatrix(mesh.position, mesh.rotation).transform(vertex);
            Vector3 viewPos = viewMatrix.transform(worldPos);
            Vector4 clip = Matrix.getProjectionMatrix(camera.fov, Game.ASPECT_RATIO, camera.zNear, camera.zFar).transform(viewPos.toVector4());
            
            Vector3 ndc = clip.div(clip.w).toVector3();
            
            double x = (ndc.x + 1) * Game.WIDTH / 2;
            double y = (-ndc.y + 1) * Game.HEIGHT / 2;
            
            ndcVertices.add(ndc);
            screenVertices.add(new Vector3(Math.ceil(x), Math.ceil(y), clip.w));
        }
    }
    
    private void drawFaces(Mesh mesh) {
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
    
}
