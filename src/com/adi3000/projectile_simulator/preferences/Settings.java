package com.adi3000.projectile_simulator.preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {
    
    private Settings() {}
    
    static final String dir = System.getenv("appdata") + "/ProjectileSimulator";
    private static final Path filePath = Paths.get(dir + "/settings.json");
    
    
    public int screenWidth = 1280;
    public int screenHeight = 720;
    public double screenPercentage = 0.667;
    
    
    public static Settings load() {
        File file = new File(filePath.toString());
        
        Settings settings;
        
        if (file.exists()) {
            try (Reader reader = Files.newBufferedReader(filePath)) {
                Gson gson = new Gson();
                settings = gson.fromJson(reader, Settings.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            settings = new Settings();
        }
        
        settings.save();
        return settings;
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
