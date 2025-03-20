package com.adi3000.projectile_simulator.main;

import com.adi3000.projectile_simulator.math.EulerAngle;
import com.adi3000.projectile_simulator.math.Vector3;
import com.adi3000.projectile_simulator.preferences.KeyConfig;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    private static KeyHandler instance;
    
    private KeyHandler() {
        keyConfig = KeyConfig.load();
    }
    
    public KeyConfig keyConfig;
    
    
    public Vector3 cameraMovement = new Vector3();
    public EulerAngle cameraRotation = new EulerAngle();
    public int fovChange = 0;
    public boolean sprint = false;
    
    @Override
    public void keyTyped(KeyEvent e) {
    
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == keyConfig.key_forward) {
            cameraMovement.z = 1;
        } else if (e.getKeyCode() == keyConfig.key_backward) {
            cameraMovement.z = -1;
        } else if (e.getKeyCode() == keyConfig.key_left) {
            cameraMovement.x = -1;
        } else if (e.getKeyCode() == keyConfig.key_right) {
            cameraMovement.x = 1;
        } else if (e.getKeyCode() == keyConfig.key_up) {
            cameraMovement.y = 1;
        } else if (e.getKeyCode() == keyConfig.key_down) {
            cameraMovement.y = -1;
        } else if (e.getKeyCode() == keyConfig.key_look_left) {
            cameraRotation.y = -1;
        } else if (e.getKeyCode() == keyConfig.key_look_right) {
            cameraRotation.y = 1;
        } else if (e.getKeyCode() == keyConfig.key_look_up) {
            cameraRotation.x = -1;
        } else if (e.getKeyCode() == keyConfig.key_look_down) {
            cameraRotation.x = 1;
        } else if (e.getKeyCode() == keyConfig.key_fov_increase) {
            fovChange = 1;
        } else if (e.getKeyCode() == keyConfig.key_fov_decrease) {
            fovChange = -1;
        } else if (e.getKeyCode() == keyConfig.key_sprint) {
            sprint = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == keyConfig.key_forward || e.getKeyCode() == keyConfig.key_backward) {
            cameraMovement.z = 0;
        } else if (e.getKeyCode() == keyConfig.key_left || e.getKeyCode() == keyConfig.key_right) {
            cameraMovement.x = 0;
        } else if (e.getKeyCode() == keyConfig.key_up || e.getKeyCode() == keyConfig.key_down) {
            cameraMovement.y = 0;
        } else if (e.getKeyCode() == keyConfig.key_look_left || e.getKeyCode() == keyConfig.key_look_right) {
            cameraRotation.y = 0;
        } else if (e.getKeyCode() == keyConfig.key_look_up || e.getKeyCode() == keyConfig.key_look_down) {
            cameraRotation.x = 0;
        } else if (e.getKeyCode() == keyConfig.key_fov_increase || e.getKeyCode() == keyConfig.key_fov_decrease) {
            fovChange = 0;
        } else if (e.getKeyCode() == keyConfig.key_sprint) {
            sprint = false;
        }
    }
    
    
    public static KeyHandler getInstance() {
        if (instance == null) {
            instance = new KeyHandler();
        }
        return instance;
    }
}
