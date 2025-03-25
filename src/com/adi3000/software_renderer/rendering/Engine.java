package com.adi3000.software_renderer.rendering;

import com.adi3000.software_renderer.main.Game;
import com.adi3000.software_renderer.math.Matrix;
import com.adi3000.software_renderer.math.Vector2;
import com.adi3000.software_renderer.math.Vector3;
import com.adi3000.software_renderer.math.Vector4;

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
    
    public int displayMode = DISPLAY_UV;
    
    
    public Engine() {
        Arrays.fill(emptyZBuffer, 1);
        
    }
    
    
    public void render(BufferedImage image, Camera camera, ArrayList<Mesh> meshes) {
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        zbuffer = Arrays.copyOf(emptyZBuffer, pixels.length);
        
        Matrix viewMatrix = Matrix.getViewMatrix(camera.position, camera.rotation.toQuaternion());
        
        for (Mesh mesh : meshes) {
            screenVertices = new ArrayList<>();
            ndcVertices = new ArrayList<>();
            
            transformVertecies(mesh, camera, viewMatrix);
            drawFaces(mesh);
        }
        
    }
    
    private void transformVertecies(Mesh mesh, Camera camera, Matrix viewMatrix) {
        for (Vector3 vertex : mesh.vertices) {
            Vector3 worldPos = Matrix.getWorldMatrix(mesh.position, mesh.scale, mesh.rotation).transform(vertex);
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
            
            ArrayList<Vector2> faceUVs = face.getFaceUVs(mesh.meshUVs);
            
            Vector3 at = new Vector3(faceUVs.get(0), 1).div(faceVertices.get(0).z);
            Vector3 bt = new Vector3(faceUVs.get(1), 1).div(faceVertices.get(1).z);
            Vector3 ct = new Vector3(faceUVs.get(2), 1).div(faceVertices.get(2).z);
            
            int faceIndex = mesh.faces.indexOf(face);
            int faceHash = Integer.hashCode(faceIndex*12390944);
            int faceColor = faceHash ^ 0xff000000;
            
            // Quick fix, TODO Add frustum culling to fix objects apearing from behind the screen
//            if (faceVertices.get(0).z < -0 || faceVertices.get(1).z < 0 || faceVertices.get(2).z < 0) {
//                continue;
//            }
            
            if (faceNdc.get(1).sub(faceNdc.get(0)).cross(faceNdc.get(2).sub(faceNdc.get(0))).z >= 0) {
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
                        
                        
                        double wt = barycentricCoords.x * at.z + barycentricCoords.y * bt.z + barycentricCoords.z * ct.z;
                        double uvx = 1 - ((barycentricCoords.x * at.x + barycentricCoords.y * bt.x + barycentricCoords.z * ct.x) / wt);
                        double uvy = 1 - ((barycentricCoords.x * at.y + barycentricCoords.y * bt.y + barycentricCoords.z * ct.y) / wt);
                        
                        int tx = Math.min((int) (uvx * (mesh.texture.getWidth())), mesh.texture.getWidth() - 1);
                        int ty = Math.min((int) (uvy * (mesh.texture.getHeight())), mesh.texture.getHeight() - 1);
                        
                        int textureIndex = ty * mesh.texture.getWidth() + tx;
                        
                        switch (displayMode) {
                            case DISPLAY_NORMAL:
                                if ((mesh.pixels[textureIndex] & 0xff000000) >> 24 == 0) continue;
                                pixels[index] = mesh.pixels[textureIndex];
                                break;
                            case DISPLAY_UV:
                                if (uvx < 0 || uvy < 0) {
                                    pixels[index] = new Color(100, (int) Math.max(Math.min(-uvx / 2 * 255, 255), 0), (int) Math.max(Math.min(-uvy / 2 * 255, 255), 0)).getRGB();
                                } else if (uvx > 1 || uvy > 1) {
                                    pixels[index] = new Color((int) Math.max(Math.min(uvy / 2 * 255, 255), 100), 0, (int) Math.max(Math.min(uvx / 2 * 255, 255), 0)).getRGB();
                                } else {
                                    pixels[index] = new Color((int) Math.max(Math.min(uvx * 255, 255), 0), (int) Math.max(Math.min(uvy * 255, 255), 0), 0).getRGB();
                                }
                                break;
                            case DISPLAY_FACE:
                                pixels[index] = new Color((int) (barycentricCoords.x * 255), (int) (barycentricCoords.y * 255), (int) (barycentricCoords.z * 255)).getRGB();
                                break;
                            case DISPLAY_FACE_ID:
                                pixels[index] = faceColor;
                                break;
                        }
                        
                        zbuffer[index] = newZ;
                        
                    }
                }
            }
        }
    }
    
    public static final int DISPLAY_NORMAL = 0;
    public static final int DISPLAY_UV = 1;
    public static final int DISPLAY_FACE = 2;
    public static final int DISPLAY_FACE_ID = 3;
    
}
