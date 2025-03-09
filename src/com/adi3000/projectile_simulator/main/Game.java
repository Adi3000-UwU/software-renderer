package com.adi3000.projectile_simulator.main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Game extends JPanel implements Runnable {
    
    // Dimensions
    public static final int WIDTH = 854;
    public static final int HEIGHT = 480;
    public static final double ASPECT_RATIO = (double) WIDTH /HEIGHT;
    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;
    
    // Game thread
    private Thread thread;
    private boolean running;
    public static int FPS;
    public static int TPS;
    public static final int TARGET_TPS = 60;
    
    // Image
    private BufferedImage image;
    private Graphics2D g2d;
    
    public static GameManager gameManager;
    
    public Game() {
        super();
        
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        
        setFocusable(true);
        requestFocus();
    }
    
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            
            addKeyListener(KeyHandler.getInstance());
            
            thread.start();
        }
    }
    
    private void init() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = image.createGraphics();
        running = true;
        
        gameManager = new GameManager();
    }
    
    public void run() {
        init();
        
        long lastTime = System.nanoTime();
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        int ticks = 0;
        
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / (1000000000d / TARGET_TPS);
            lastTime = now;
            
            while (delta >= 1) {
                tick();
                delta--;
                ticks++;
            }
            
            render();
            renderToScreen();
            frames++;
            
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                FPS = frames;
                frames = 0;
                TPS = ticks;
                ticks = 0;
            }
        }
    }
    
    private void tick() {
        gameManager.tick();
    }
    
    private void render() {
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        
        gameManager.render(image);
        
        drawFPS();
    }
    
    private void renderToScreen() {
        Graphics g = getGraphics();
        g.drawImage(image, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        g.dispose();
    }
    
    
    private void drawFPS() {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, 35, 35);
        g2d.setColor(Color.WHITE);
        g2d.drawString(String.valueOf(FPS), 5, 15);
        g2d.drawString(String.valueOf(TPS), 5, 30);
    }
}
