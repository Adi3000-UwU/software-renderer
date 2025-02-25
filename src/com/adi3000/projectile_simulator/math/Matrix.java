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
    public Vector4 transform(Vector3 v) {
        return transform(v.toVector4());
    }
    public Vector4 transform(Vector2 v) {
        return transform(v.toVector4());
    }
    
}
