package com.adi3000.projectile_simulator.math;

@SuppressWarnings("unused")
public class Quaternion {
    
    public double x;
    public double y;
    public double z;
    public double w;
    
    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public Quaternion() {
        this(0, 0, 0, 1);
    }
    public Quaternion(Vector3 axis, double rotation) {
        this(axis.x, axis.y, axis.z, rotation);
    }
    
    public static Quaternion fromEulerAngle(double u, double v, double w) {
        return new Quaternion(
                Math.sin(u / 2) * Math.cos(v / 2) * Math.cos(w / 2) - Math.cos(u / 2) * Math.sin(v / 2) * Math.sin(w / 2),
                Math.cos(u / 2) * Math.sin(v / 2) * Math.cos(w / 2) + Math.sin(u / 2) * Math.cos(v / 2) * Math.sin(w / 2),
                Math.cos(u / 2) * Math.cos(v / 2) * Math.sin(w / 2) - Math.sin(u / 2) * Math.sin(v / 2) * Math.cos(w / 2),
                Math.cos(u / 2) * Math.cos(v / 2) * Math.cos(w / 2) + Math.sin(u / 2) * Math.sin(v / 2) * Math.sin(w / 2)
        );
    }
    public static Quaternion fromEulerAngleDegree(double u, double v, double w) {
        return fromEulerAngle(Math.toRadians(u), Math.toRadians(v), Math.toRadians(w));
    }
    
    public Vector3 toEulerAngle() {
        double pitch = Math.asin(2 * (w * y - x * z));
        
        // Handle gimbal lock
        if (pitch == -(Math.PI / 2)) {
            return new Vector3(0, pitch, 2 * Math.atan2(x, w));
        } else if (pitch == Math.PI / 2) {
            return new Vector3(0, pitch, -2 * Math.atan2(x, w));
        }
        
        double roll = Math.atan2(2 * (w * x + y * z), Math.pow(w, 2) - Math.pow(x, 2) - Math.pow(y, 2) + Math.pow(z, 2));
        double yaw = Math.atan2(2 * (w * z + x * y), Math.pow(w, 2) + Math.pow(x, 2) - Math.pow(y, 2) - Math.pow(z, 2));
        
        return new Vector3(roll, pitch, yaw);
    }
    
    public void set(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
    public void set(Quaternion q) {
        set(q.x, q.y, q.z, q.w);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Quaternion quaternion = (Quaternion) o;
        return Double.compare(x, quaternion.x) == 0 && Double.compare(y, quaternion.y) == 0 && Double.compare(z, quaternion.z) == 0 && Double.compare(w, quaternion.w) == 0;
    }
    @Override
    public String toString() {
        return "Quaternion{" + "x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + '}';
    }
    
    public static Quaternion mult(Quaternion r, Quaternion s) {
        return new Quaternion(
                r.w * s.x + r.x * s.w - r.y * s.z + r.z * s.y,
                r.w * s.y + r.x * s.z + r.y * s.w - r.z * s.x,
                r.w * s.z - r.x * s.y + r.y * s.x + r.z * s.w,
                r.w * s.w - r.x * s.x - r.y * s.y - r.z * s.z
        );
    }
    
    public void rotate(Quaternion rotation) {
        set(mult(this, rotation));
    }
    
}
