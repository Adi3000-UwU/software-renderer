package com.adi3000.software_renderer.math;

@SuppressWarnings("unused")
public class Vector2 {
    
    public double x;
    public double y;
    
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Vector2() {
        this(0,0);
    }
    public Vector2(Vector2 v) {
        this(v.x, v.y);
    }
    public Vector2(Vector3 v) {
        this(v.x, v.y);
    }
    public Vector2(double[] components) {
        this(components[0], components[1]);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return x == vector2.x && y == vector2.y;
    }
    @Override
    public String toString() {
        return "Vector2{" + "x=" + x + ", y=" + y + '}';
    }
    public double[] toArray() {
        return new double[] {x, y};
    }
    public Vector3 toVector3() {
        return new Vector3(x, y, 0);
    }
    public Vector4 toVector4() {
        return new Vector4(x, y, 0, 1);
    }
    
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void set(Vector2 v) {
        set(v.x, v.y);
    }
    public void set(Vector3 v) {
        set(v.x, v.y);
    }
    public void set(double[] components) {
        set(components[0], components[1]);
    }
    public void setMagnitude(double mag) {
        set(getNormilized().mult(mag));
    }
    
    public Vector2 add(Vector2 v) {
        return add(this, v);
    }
    public static Vector2 add(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }
    public Vector2 sub(Vector2 v) {
        return sub(this, v);
    }
    public static Vector2 sub(Vector2 v1, Vector2 v2) {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }
    public Vector2 mult(double scalar) {
        return mult(this, scalar);
    }
    public static Vector2 mult(Vector2 v, double scalar) {
        return new Vector2(v.x * scalar, v.y * scalar);
    }
    public Vector2 div(double scalar) {
        return div(this, scalar);
    }
    public static Vector2 div(Vector2 v, double scalar) {
        return new Vector2(v.x / scalar, v.y / scalar);
    }
    
    public double mag() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
    public Vector2 normilize() {
        set(getNormilized());
        return this;
    }
    public Vector2 getNormilized() {
        double mag_inv = 1.0 / mag();
        return new Vector2(x * mag_inv, y * mag_inv);
    }
    
    public double dot(Vector2 v) {
        return dot(this, v);
    }
    public static double dot(Vector2 v1, Vector2 v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }
    public Vector3 cross(Vector2 v) {
        return cross(this, v);
    }
    public static Vector3 cross(Vector2 v1, Vector2 v2) {
        return Vector3.cross(v1.toVector3(), v2.toVector3());
    }
    
    public double distance(Vector2 v) {
        return distance(this, v);
    }
    public static double distance(Vector2 v1, Vector2 v2) {
        return Math.sqrt(Math.pow(v2.x - v1.x, 2) + Math.pow(v2.y - v1.y, 2));
    }
    
    public double angle(Vector2 v) {
        return angle(this, v);
    }
    public static double angle(Vector2 v1, Vector2 v2) {
        return Math.acos(v1.dot(v2) / (v1.mag() * v2.mag()));
    }
    public double heading() {
        return angle(new Vector2(1, 0));
    }
    public Vector2 rotate(double angle) {
        double tempX = Math.cos(angle) * x - Math.sin(angle) * y;
        double tempY = Math.sin(angle) * x + Math.cos(angle) * y;
        set(tempX, tempY);
        return this;
    }
    
}
