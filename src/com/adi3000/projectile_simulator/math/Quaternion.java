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
    public Quaternion(Quaternion q) {
        this(q.x, q.y, q.z, q.w);
    }
    public Quaternion(EulerAngle angle) {
        this(angle.toQuaternion());
    }
    
    public EulerAngle toEulerAngle() {
        double pitch = Math.asin(Math.max(Math.min(2 * (w * y - x * z), 1), -1));
        
        // Handle gimbal lock
        if (Math.abs(pitch - (Math.PI / 2)) <= 0.000001) {
            return new EulerAngle(2 * Math.atan2(x, w), pitch, 0);
        } else if (Math.abs(pitch + (Math.PI / 2)) <= 0.000001) {
            return new EulerAngle(-2 * Math.atan2(x, w), pitch, 0);
        }
        
        double roll = Math.atan2(2 * (w * x + y * z), Math.pow(w, 2) - Math.pow(x, 2) - Math.pow(y, 2) + Math.pow(z, 2));
        double yaw = Math.atan2(2 * (w * z + x * y), Math.pow(w, 2) + Math.pow(x, 2) - Math.pow(y, 2) - Math.pow(z, 2));
        
        if (y > w) {
            return new EulerAngle(Math.PI - Math.abs(roll), Math.PI - pitch, Math.PI - Math.abs(yaw));
        }
        return new EulerAngle(roll, pitch, yaw);
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
    public void set(EulerAngle angle) {
        set(angle.toQuaternion());
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
    
    public void incrementAngle(EulerAngle changeAngle) {
        EulerAngle currentAngle = toEulerAngle();
        set(currentAngle.add(changeAngle));
    }
    
}
