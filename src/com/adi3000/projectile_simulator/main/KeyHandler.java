package com.adi3000.projectile_simulator.main;

import com.adi3000.projectile_simulator.math.Quaternion;
import com.adi3000.projectile_simulator.math.Vector3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    @Override
    public void keyTyped(KeyEvent e) {
    
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            Game.engine.camera.moveCamera(new Vector3(0, 0, 0.1));
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            Game.engine.camera.moveCamera(new Vector3(0, 0, -0.1));
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            Game.engine.camera.moveCamera(new Vector3(-0.1, 0, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            Game.engine.camera.moveCamera(new Vector3(0.1, 0, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Game.engine.camera.moveCamera(new Vector3(0, 0.1, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            Game.engine.camera.moveCamera(new Vector3(0, -0.1, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            Game.engine.camera.rotation.rotate(Quaternion.fromEulerAngleDegree(0, -1, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            Game.engine.camera.rotation.rotate(Quaternion.fromEulerAngleDegree(0, 1, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            Game.engine.camera.rotation.rotate(Quaternion.fromEulerAngleDegree(-1, 0, 0));
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            Game.engine.camera.rotation.rotate(Quaternion.fromEulerAngleDegree(1, 0, 0));
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    
    }
}
