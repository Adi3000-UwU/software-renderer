package com.adi3000.projectile_simulator.preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class KeyConfig {
    
    private KeyConfig() {}
    
    private static final Path filePath = Paths.get(Settings.dir + "/keyConfig.json");
    
    
    public int key_forward = KeyEvent.VK_W;
    public int key_backward = KeyEvent.VK_S;
    public int key_left = KeyEvent.VK_A;
    public int key_right = KeyEvent.VK_D;
    public int key_up = KeyEvent.VK_SPACE;
    public int key_down = KeyEvent.VK_SHIFT;
    
    public int key_look_left = KeyEvent.VK_LEFT;
    public int key_look_right = KeyEvent.VK_RIGHT;
    public int key_look_up = KeyEvent.VK_UP;
    public int key_look_down = KeyEvent.VK_DOWN;
    
    
    public static KeyConfig load() {
        File file = new File(filePath.toString());
        
        KeyConfig keyConfig;
        
        if (file.exists()) {
            try (Reader reader = Files.newBufferedReader(filePath)) {
                Gson gson = new Gson();
                keyConfig = gson.fromJson(reader, KeyConfig.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            keyConfig = new KeyConfig();
        }
        
        keyConfig.save();
        return keyConfig;
    }
    
    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        File file = new File(filePath.toString());
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
