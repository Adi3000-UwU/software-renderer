package com.adi3000.projectile_simulator.math;

@SuppressWarnings("unused")
public class Vector4 {
    
    public double x;
    public double y;
    public double z;
    public double w;
    
    public Vector4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Vector4(Vector4 v) {
        this(v.x, v.y, v.z, v.w);
    }
    public Vector4(Vector3 v, double scalar) {
        this(v.x, v.y, v.z, scalar);
    }
    public Vector4(double[] components) {
        this(components[0], components[1], components[2], components[3]);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vector4 vector4 = (Vector4) o;
        return Double.compare(x, vector4.x) == 0 && Double.compare(y, vector4.y) == 0 && Double.compare(z, vector4.z) == 0 && Double.compare(w, vector4.w) == 0;
    }
    @Override
    public String toString() {
        return "Vector4{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
    }
    public double[] toArray() {
        return new double[] {x, y, z, w};
    }
    public Vector3 toVector3() {
        return new Vector3(x, y, z);
    }
    public Vector2 toVector2() {
        return new Vector2(x, y);
    }
    
    public void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public void set(Vector4 v) {
        set(v.x, v.y, v.z, v.w);
    }
    public void set(Vector3 v, double scalar) {
        set(v.x, v.y, v.z, scalar);
    }
    public void set(double[] components) {
        set(components[0], components[1], components[2], components[3]);
    }
    public void setMagnitude(double mag) {
        set(getNormilized().mult(mag));
    }
    
    public Vector4 add(Vector4 v) {
        return add(this, v);
    }
    public static Vector4 add(Vector4 v1, Vector4 v2) {
        return new Vector4(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z, v1.w + v2.w);
    }
    public Vector4 sub(Vector4 v) {
        return sub(this, v);
    }
    public static Vector4 sub(Vector4 v1, Vector4 v2) {
        return new Vector4(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z, v1.w - v2.w);
    }
    public Vector4 mult(double scalar) {
        return mult(this, scalar);
    }
    public static Vector4 mult(Vector4 v, double scalar) {
        return new Vector4(v.x * scalar, v.y * scalar, v.z * scalar, v.w * scalar);
    }
    public Vector4 div(double scalar) {
        return div(this, scalar);
    }
    public static Vector4 div(Vector4 v, double scalar) {
        return new Vector4(v.x / scalar, v.y / scalar, v.z / scalar, v.w / scalar);
    }
    
    public double mag() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }
    public Vector4 normilize() {
        set(getNormilized());
        return this;
    }
    public Vector4 getNormilized() {
        double mag_inv = 1.0 / mag();
        return new Vector4(x * mag_inv, y * mag_inv, z * mag_inv, w * mag_inv);
    }
    
    public double dot(Vector4 v) {
        return dot(this, v);
    }
    public static double dot(Vector4 v1, Vector4 v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w;
    }
    
}
