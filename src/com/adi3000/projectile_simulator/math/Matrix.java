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
    
    public Matrix invert() {
        double[] result = new double[16];
        
        double num1 = values[10] * values[15] - values[11] * values[14];
        double num2 = values[9] * values[15] - values[11] * values[13];
        double num3 = values[9] * values[14] - values[10] * values[13];
        double num4 = values[8] * values[15] - values[11] * values[12];
        double num5 = values[8] * values[14] - values[10] * values[12];
        double num6 = values[8] * values[13] - values[9] * values[12];
        
        double temp1 = values[5] * num1 - values[6] * num2 + values[7] * num3;
        double temp2 = -(values[4] * num1 - values[6] * num4 + values[7] * num5);
        double temp3 = values[4] * num2 - values[5] * num4 + values[7] * num6;
        double temp4 = -(values[4] * num3 - values[5] * num5 + values[6] * num6);
        double special = 1.0 / (values[0] * temp1 + values[1] * temp2 + values[2] * temp3 + values[3] * temp4);
        
        result[0] = temp1 * special;
        result[4] = temp2 * special;
        result[8] = temp3 * special;
        result[12] = temp4 * special;
        result[1] = -(values[1] * num1 - values[2] * num2 + values[3] * num3) * special;
        result[5] = (values[0] * num1 - values[2] * num4 + values[3] * num5) * special;
        result[9] = -(values[0] * num2 - values[1] * num4 + values[3] * num6) * special;
        result[13] = (values[0] * num3 - values[1] * num5 + values[2] * num6) * special;
        double num7 = values[6] * values[15] - values[7] * values[14];
        double num8 = values[5] * values[15] - values[7] * values[13];
        double num9 = values[5] * values[14] - values[6] * values[13];
        double num10 = values[4] * values[15] - values[7] * values[12];
        double num11 = values[4] * values[14] - values[6] * values[12];
        double num12 = values[4] * values[13] - values[5] * values[12];
        result[2] = (values[1] * num7 - values[2] * num8 + values[3] * num9) * special;
        result[6] = -(values[0] * num7 - values[2] * num10 + values[3] * num11) * special;
        result[10] = (values[0] * num8 - values[1] * num10 + values[3] * num12) * special;
        result[14] = -(values[0] * num9 - values[1] * num11 + values[2] * num12) * special;
        double num13 = values[6] * values[11] - values[7] * values[10];
        double num14 = values[5] * values[11] - values[7] * values[9];
        double num15 = values[5] * values[10] - values[6] * values[9];
        double num16 = values[4] * values[11] - values[7] * values[8];
        double num17 = values[4] * values[10] - values[6] * values[8];
        double num18 = values[4] * values[9] - values[5] * values[8];
        result[3] = -(values[1] * num13 - values[2] * num14 + values[3] * num15) * special;
        result[7] = (values[0] * num13 - values[2] * num16 + values[3] * num17) * special;
        result[11] = -(values[0] * num14 - values[1] * num16 + values[3] * num18) * special;
        result[15] = (values[0] * num15 - values[1] * num17 + values[2] * num18) * special;
        
        values = result;
        return this;
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
