package com.adi3000.projectile_simulator.math;

@SuppressWarnings("unused")
public class EulerAngle {
    
    public double x;
    public double y;
    public double z;
    
    public EulerAngle(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public EulerAngle() {
        this(0, 0, 0);
    }
    public EulerAngle(EulerAngle angle) {
        this(angle.x, angle.y, angle.z);
    }
    public EulerAngle(Quaternion q) {
        this(q.toEulerAngle());
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EulerAngle eulerAngle = (EulerAngle) o;
        return Double.compare(x, eulerAngle.x) == 0 && Double.compare(y, eulerAngle.y) == 0 && Double.compare(z, eulerAngle.z) == 0;
    }
    @Override
    public String toString() {
        return "EulerAngle{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
    
    public Quaternion toQuaternion() {
        return new Quaternion(
                Math.sin(x / 2) * Math.cos(y / 2) * Math.cos(z / 2) - Math.cos(x / 2) * Math.sin(y / 2) * Math.sin(z / 2),
                Math.cos(x / 2) * Math.sin(y / 2) * Math.cos(z / 2) + Math.sin(x / 2) * Math.cos(y / 2) * Math.sin(z / 2),
                Math.cos(x / 2) * Math.cos(y / 2) * Math.sin(z / 2) - Math.sin(x / 2) * Math.sin(y / 2) * Math.cos(z / 2),
                Math.cos(x / 2) * Math.cos(y / 2) * Math.cos(z / 2) + Math.sin(x / 2) * Math.sin(y / 2) * Math.sin(z / 2)
        );
    }
    
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void set(EulerAngle e) {
        set(e.x, e.y, e.z);
    }
    public void set(Quaternion q) {
        set(q.toEulerAngle());
    }
    
    public EulerAngle toDegree() {
        return new EulerAngle(Math.toDegrees(x), Math.toDegrees(y), Math.toDegrees(z));
    }
    public EulerAngle toRad() {
        return new EulerAngle(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
    }
    
    public EulerAngle add(EulerAngle e) {
        return add(this, e);
    }
    public static EulerAngle add(EulerAngle e1, EulerAngle e2) {
        return new EulerAngle(e1.x + e2.x, e1.y + e2.y, e1.z + e2.z);
    }
    public EulerAngle sub(EulerAngle e) {
        return sub(this, e);
    }
    public static EulerAngle sub(EulerAngle e1, EulerAngle e2) {
        return new EulerAngle(e1.x - e2.x, e1.y - e2.y, e1.z - e2.z);
    }
    public EulerAngle mult(double scalar) {
        return mult(this, scalar);
    }
    public static EulerAngle mult(EulerAngle e, double scalar) {
        return new EulerAngle(e.x * scalar, e.y * scalar, e.z * scalar);
    }
    
    public EulerAngle increment(EulerAngle e) {
        set(add(e));
        return this;
    }
}
