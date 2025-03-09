package com.adi3000.projectile_simulator.main;

import com.adi3000.projectile_simulator.math.Vector2;
import com.adi3000.projectile_simulator.math.Vector3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    private static KeyHandler instance;
    
    private KeyHandler() {}
    
    
    public Vector3 cameraMovement = new Vector3();
    public Vector2 cameraRotation = new Vector2();
    
    
    @Override
    public void keyTyped(KeyEvent e) {
    
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> cameraMovement.z = 1;
            case KeyEvent.VK_S -> cameraMovement.z = -1;
            case KeyEvent.VK_A -> cameraMovement.x = -1;
            case KeyEvent.VK_D -> cameraMovement.x = 1;
            case KeyEvent.VK_SPACE -> cameraMovement.y = 1;
            case KeyEvent.VK_SHIFT -> cameraMovement.y = -1;
            case KeyEvent.VK_LEFT -> cameraRotation.y = -1;
            case KeyEvent.VK_RIGHT -> cameraRotation.y = 1;
            case KeyEvent.VK_UP -> cameraRotation.x = -1;
            case KeyEvent.VK_DOWN -> cameraRotation.x = 1;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_S -> cameraMovement.z = 0;
            case KeyEvent.VK_A, KeyEvent.VK_D -> cameraMovement.x = 0;
            case KeyEvent.VK_SPACE, KeyEvent.VK_SHIFT -> cameraMovement.y = 0;
            case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT -> cameraRotation.y = 0;
            case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> cameraRotation.x = 0;
        }
    }
    
    
    public static KeyHandler getInstance() {
        if(instance == null) {
            instance = new KeyHandler();
        }
        return instance;
    }
}
