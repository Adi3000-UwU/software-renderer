package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector2;
import com.adi3000.projectile_simulator.math.Vector3;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Mesh {
    
    public ArrayList<Vector3> vertices = new ArrayList<>();
    public ArrayList<Face> faces = new ArrayList<>();
    public ArrayList<Vector2> meshUVs = new ArrayList<>();
    
    private String texturePath;
    public BufferedImage texture;
    public int[] pixels;
    
    public Vector3 position;
    public Vector3 scale;
    public Quaternion rotation;
    
    public Mesh(String modelPath) {
        this(new Vector3(0, 0, 0), new Quaternion(), modelPath);
    }
    public Mesh(Vector3 position, Quaternion rotation, String modelPath) {
        this(position, new Vector3(1, 1, 1), rotation, modelPath);
    }
    public Mesh(Vector3 position, Vector3 scale, Quaternion rotation, String modelPath) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        
        loadModel("/Models/" + modelPath);
        
        texture = getTextureInIntFormat("/Textures/" + texturePath);
        pixels = ((DataBufferInt) texture.getRaster().getDataBuffer()).getData();
    }
    
    public void addVertex(Vector3 vertex) {
        vertices.add(vertex);
    }
    
    public void addUV(Vector2 uv) {
        meshUVs.add(uv);
    }
    
    public void addFace(int[] vertexIndices, int[] uvIndices) {
        faces.add(new Face(vertexIndices, uvIndices));
    }
    
    
    private BufferedImage getTextureInIntFormat(String texturePath) {
        try (ImageInputStream stream = ImageIO.createImageInputStream(Objects.requireNonNull(getClass().getResourceAsStream(texturePath)))) {
            // Find a suitable reader
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (!readers.hasNext()) {
                throw new IIOException("No reader for " + stream);
            }
            
            ImageReader reader = readers.next();
            try {
                reader.setInput(stream);
                
                // Query the reader for types and select the best match
                ImageTypeSpecifier intPackedType = getIntPackedType(reader);
                
                // Pass the type to the reader using read param
                ImageReadParam param = reader.getDefaultReadParam();
                assert intPackedType != null;
                param.setDestination(intPackedType.createBufferedImage(reader.getWidth(0), reader.getHeight(0)));
                
                // Finally read the image
                return reader.read(0, param);
            } finally {
                reader.dispose();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private ImageTypeSpecifier getIntPackedType(ImageReader reader) throws IOException {
        Iterator<ImageTypeSpecifier> types = reader.getImageTypes(0);
        
        while (types.hasNext()) {
            ImageTypeSpecifier spec = types.next();
            
            switch (spec.getBufferedImageType()) {
                case BufferedImage.TYPE_INT_RGB:
                case BufferedImage.TYPE_INT_ARGB:
                    return spec;
                default:
                    // continue searching
            }
        }
        return null;
    }
    
    private void loadModel(String modelPath) {
        List<String> lines;
        
        try {
            InputStream in = Objects.requireNonNull(getClass().getResourceAsStream(modelPath));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            lines = br.lines().toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        for (String line : lines) {
            if (line.startsWith("#")) continue;
            
            String[] splitLine = line.split(" ");
            
            switch (splitLine[0]) {
                case "v":
                    addVertex(new Vector3(Double.parseDouble(splitLine[1]), Double.parseDouble(splitLine[2]), Double.parseDouble(splitLine[3])));
                    break;
                case "vt":
                    addUV(new Vector2(Double.parseDouble(splitLine[1]), Double.parseDouble(splitLine[2])));
                    break;
                case "f":
                    int[] vertexIndices = new int[3];
                    int[] uvIndices = new int[3];
                    for (int i = 0; i < 3; i++) {
                        String[] splitVertex = splitLine[i + 1].split("/");
                        
                        vertexIndices[i] = Integer.parseInt(splitVertex[0]) - 1;
                        uvIndices[i] = Integer.parseInt(splitVertex[1]) - 1;
                    }
                    addFace(vertexIndices, uvIndices);
                    break;
                case "t":
                    texturePath = splitLine[1];
                    break;
                default:
                    break;
            }
        }
    }
}
