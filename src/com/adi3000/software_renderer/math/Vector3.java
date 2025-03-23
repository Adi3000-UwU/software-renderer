package com.adi3000.software_renderer.math;

@SuppressWarnings("unused")
public class Vector3 {
    
    public double x;
    public double y;
    public double z;
    
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3() {
        this(0, 0, 0);
    }
    public Vector3(Vector3 v) {
        this(v.x, v.y, v.z);
    }
    public Vector3(Vector4 v) {
        this(v.x, v.y, v.z);
    }
    public Vector3(Vector2 v) {
        this(v.x, v.y, 0);
    }
    public Vector3(Vector2 v, double scalar) {
        this(v.x, v.y, scalar);
    }
    public Vector3(double[] components) {
        this(components[0], components[1], components[2]);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return Double.compare(x, vector3.x) == 0 && Double.compare(y, vector3.y) == 0 && Double.compare(z, vector3.z) == 0;
    }
    @Override
    public String toString() {
        return "Vector3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
    public double[] toArray() {
        return new double[] {x, y, z};
    }
    public Vector2 toVector2() {
        return new Vector2(x, y);
    }
    public Vector4 toVector4() {
        return new Vector4(x, y, z, 1);
    }
    
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void set(Vector4 v) {
        set(v.x, v.y, v.z);
    }
    public void set(Vector3 v) {
        set(v.x, v.y, v.z);
    }
    public void set(Vector2 v, double scalar) {
        set(v.x, v.y, scalar);
    }
    public void set(double[] components) {
        set(components[0], components[1], components[2]);
    }
    public void setMagnitude(double mag) {
        set(getNormilized().mult(mag));
    }
    
    public Vector3 add(Vector3 v) {
        return add(this, v);
    }
    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
    public Vector3 sub(Vector3 v) {
        return sub(this, v);
    }
    public static Vector3 sub(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
    public Vector3 mult(double scalar) {
        return mult(this, scalar);
    }
    public static Vector3 mult(Vector3 v, double scalar) {
        return new Vector3(v.x * scalar, v.y * scalar, v.z * scalar);
    }
    public Vector3 div(double scalar) {
        return div(this, scalar);
    }
    public static Vector3 div(Vector3 v, double scalar) {
        return new Vector3(v.x / scalar, v.y / scalar, v.z / scalar);
    }
    
    public double mag() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }
    public Vector3 normilize() {
        set(getNormilized());
        return this;
    }
    public Vector3 getNormilized() {
        double mag_inv = 1.0 / mag();
        return new Vector3(x * mag_inv, y * mag_inv, z * mag_inv);
    }
    
    public double dot(Vector3 v) {
        return dot(this, v);
    }
    public static double dot(Vector3 v1, Vector3 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }
    public Vector3 cross(Vector3 v) {
        return cross(this, v);
    }
    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        double x = v1.y * v2.z - v1.z * v2.y;
        double y = v1.z * v2.x - v1.x * v2.z;
        double z = v1.x * v2.y - v1.y * v2.x;
        
        return new Vector3(x, y, z);
    }
    
    public double distance(Vector3 v) {
        return distance(this, v);
    }
    public static double distance(Vector3 v1, Vector3 v2) {
        return Math.sqrt(Math.pow(v2.x - v1.x, 2) + Math.pow(v2.y - v1.y, 2) + Math.pow(v2.z - v1.z, 2));
    }
    
}
