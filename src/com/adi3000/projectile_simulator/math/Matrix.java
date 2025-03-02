package com.adi3000.projectile_simulator.math;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public class Matrix {
    
    double[] values;
    
    public Matrix() {
        this.values = new double[16];
    }
    public Matrix(double[] values) {
        this.values = values;
    }
    public Matrix(double[][] values) {
        this();
        
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(values[i], 0, this.values, i * 4, values[i].length);
        }
    }
    
    @Override
    public String toString() {
        return "Matrix{" + "values=" + Arrays.toString(values) + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        return Objects.deepEquals(values, matrix.values);
    }
    
    
    public Matrix mult(Matrix m) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] += this.values[row * 4 + i] * m.values[i * 4 + col];
                }
            }
        }
        return new Matrix(result);
    }
    
    public Vector4 transform(Vector4 v) {
        return new Vector4(
                v.x * values[0] + v.y * values[1] + v.z * values[2] + v.w * values[3],
                v.x * values[4] + v.y * values[5] + v.z * values[6] + v.w * values[7],
                v.x * values[8] + v.y * values[9] + v.z * values[10] + v.w * values[11],
                v.x * values[12] + v.y * values[13] + v.z * values[14] + v.w * values[15]
        );
    }
    public Vector3 transform(Vector3 v) {
        return transform(v.toVector4()).toVector3();
    }
    public Vector2 transform(Vector2 v) {
        return transform(v.toVector4()).toVector2();
    }
    
    
    public static Matrix getWorldMatrix(Vector3 position, Quaternion rotation) {
        double a = 1 - (2 * Math.pow(rotation.y, 2) + 2 * Math.pow(rotation.z, 2));
        double b = 2 * rotation.x * rotation.y - 2 * rotation.w * rotation.z;
        double c = 2 * rotation.x * rotation.z + 2 * rotation.w * rotation.y;
        double d = 2 * rotation.x * rotation.y + 2 * rotation.w * rotation.z;
        double e = 1 - (2 * Math.pow(rotation.x, 2) + 2 * Math.pow(rotation.z, 2));
        double f = 2 * rotation.y * rotation.z - 2 * rotation.w * rotation.x;
        double g = 2 * rotation.x * rotation.z - 2 * rotation.w * rotation.y;
        double h = 2 * rotation.y * rotation.z + 2 * rotation.w * rotation.x;
        double i = 1 - (2 * Math.pow(rotation.x, 2) + 2 * Math.pow(rotation.y, 2));
        
        return new Matrix(new double[][] {
                {a, b, c, position.x},
                {d, e, f, position.y},
                {g, h, i, position.z},
                {0, 0, 0, 1}
        });
    }
    
    public static Matrix getProjectionMatrix(double fov, double aspectRatio, double zNear, double zFar) {
        double a = 1 / (Math.tan(Math.toRadians(fov) / 2) * aspectRatio);
        double b = 1 / Math.tan(Math.toRadians(fov) / 2);
        double c = (-zFar - zNear) / (zNear - zFar);
        double d = (zNear * zFar * 2) / (zNear - zFar);
        
        return new Matrix(new double[][] {
                {a, 0, 0, 0},
                {0, b, 0, 0},
                {0, 0, c, d},
                {0, 0, 1, 0}
        });
    }
    
}
