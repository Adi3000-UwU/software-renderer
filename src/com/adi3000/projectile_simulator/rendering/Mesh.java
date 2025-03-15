package com.adi3000.projectile_simulator.rendering;

import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector2;
import com.adi3000.projectile_simulator.math.Vector3;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Mesh {
    
    public ArrayList<Vector3> vertices = new ArrayList<>();
    public ArrayList<Face> faces = new ArrayList<>();
    public ArrayList<Vector2> meshUVs = new ArrayList<>();
    
    public BufferedImage texture;
    public int[] pixels;
    
    public Vector3 position;
    public Quaternion rotation;
    
    public Mesh(String texturePath) {
        this(new Vector3(0, 0, 0), new Quaternion(), texturePath);
    }
    public Mesh(Vector3 position, Quaternion rotation, String texturePath) {
        this.position = position;
        this.rotation = rotation;
        
        texture = getTextureInIntFormat(texturePath);
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
    
}
