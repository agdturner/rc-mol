/*
 * Copyright 2025 University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This code is based on
 * https://web.archive.org/web/20240203104803/https://theobald.brandeis.edu/qcp/main.c
 * Douglas L. Theobald (2005) "Rapid calculation of RMSD using a
 * quaternion-based characteristic polynomial." Acta Crystallographica A
 * 61(4):478-480.
 *
 * Pu Liu, Dmitris K. Agrafiotis, and Douglas L. Theobald (2009) "Fast
 * determination of the optimal rotational matrix for macromolecular
 * superpositions." Journal of Computational Chemistry 31(7):1561-1563.
 *
 * Copyright (c) 2009-2016 Pu Liu and Douglas L. Theobald All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. Redistributions
 * in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. Neither the name of the
 * <ORGANIZATION> nor the names of its contributors may be used to endorse
 * or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package uk.ac.leeds.ccg.mol.geom;

import java.util.TreeSet;
import uk.ac.leeds.ccg.mol.data.cif.CIF;

/**
 *
 * @author Andy Turner
 */
public class QCProt {

    /**
     * The quaternion components.
     */
    public double q1, q2, q3, q4;

    /**
     * For storing the root mean squared error.
     */
    public double rmsd;

    /**
     * For storing the weighted root mean squared error.
     */
    public double wrmsd;

    /**
     * For storing the rotation matrix.
     */
    public double[] rotmat;

    /**
     * For storing the weights.
     */
    double[] weight;

    /**
     * The coordinates to orientate to.
     */
    double[][] coords1;

    /**
     * For storing the rotated coordinates.
     */
    double[][] coords2;

    /**
     * For storing the rotated coordinates.
     */
    double[][] coords2Rotated;

    /**
     * For storing the number of coordinates.
     */
    int len;

    /**
     * For storing the number of coordinates as a double.
     */
    double n;
    
    /**
     * Create a new instance. 
     *
     * @param cif1 The CIF with coordinates to fit to.
     * @param cif2 The CIF with coordinates to be fitted.
     * @param atomTypes If null then all atom types are used. 
     */
    public QCProt(CIF cif1, CIF cif2, TreeSet<String> atomTypes) {
        this(cif1.getCoords(atomTypes), cif2.getCoords(atomTypes));
    }
    
    /**
     * Create a new instance. The respective x, y and z coordinates for the
     * point are in: coords1[0] and coords2[0], coords1[1] and coords2[1], and
     * coords1[2] and coords1[3]. The order of these must be consistent between
     * the two sets of points, but these can be in any order. are the x, y and z
     * coordinates for each point respectively.
     *
     * @param coords1 The centralised coordinates to be fitted to.
     * @param coords2 The centralised coordinates to be fitted.
     */
    public QCProt(double[][] coords1, double[][] coords2) {
        rotmat = new double[9];
        this.coords1 = coords1;
        this.coords2 = coords2;
        this.len = Math.min(coords1[0].length, coords2[0].length);
        double wsum = len;

        double[] A = new double[9];
        /* calculate the (weighted) inner product of two structures */
        double E0 = innerProduct(A);

        /* calculate the RMSD & rotational matrix */
        fastCalcRMSDAndRotation(A, E0, wsum, -1, -1d);
    }

    public double[][] getRotatedCoordinates() {
        /* apply rotation matrix */
        coords2Rotated = new double[3][len];
        for (int i = 0; i < len; i++) {
            coords2Rotated[0][i] = rotmat[0] * coords2[0][i] + rotmat[1] * coords2[1][i] + rotmat[2] * coords2[2][i];
            coords2Rotated[1][i] = rotmat[3] * coords2[0][i] + rotmat[4] * coords2[1][i] + rotmat[5] * coords2[2][i];
            coords2Rotated[2][i] = rotmat[6] * coords2[0][i] + rotmat[7] * coords2[1][i] + rotmat[8] * coords2[2][i];
        }

        /* calculate euclidean distance */
        double euc_dist = 0d;
        double weuc_dist = 0d;
        double wtsum = 0d;
        for (int i = 0; i < len; i++) {
            wtsum += weight[i];
            double tmp = Math.pow(coords1[0][i] - coords2Rotated[0][i], 2)
                    + Math.pow(coords1[1][i] - coords2Rotated[1][i], 2)
                    + Math.pow(coords1[2][i] - coords2Rotated[2][i], 2);
            weuc_dist += weight[i] * tmp;
            euc_dist += tmp;
        }

        wrmsd = Math.sqrt(euc_dist / wtsum);
        //System.out.println("Explicit Weighted RMSD calculated from transformed coords: " + wrmsd);
        rmsd = Math.sqrt(euc_dist / (double) len);
        //System.out.println("Explicit RMSD calculated from transformed coords: " + rmsd);
        return coords2Rotated;
    }

    /**
     * fastCalcRMSDAndRotation
     *
     * @param A
     * @param E0
     * @param wsum
     * @param len
     * @param minScore
     * @return
     */
    protected int fastCalcRMSDAndRotation(double[] A, double E0, double wsum, double len, double minScore) {
        double Sxx, Sxy, Sxz, Syx, Syy, Syz, Szx, Szy, Szz;
        double Szz2, Syy2, Sxx2, Sxy2, Syz2, Sxz2, Syx2, Szy2, Szx2,
                SyzSzymSyySzz2, Sxx2Syy2Szz2Syz2Szy2, Sxy2Sxz2Syx2Szx2,
                SxzpSzx, SyzpSzy, SxypSyx, SyzmSzy,
                SxzmSzx, SxymSyx, SxxpSyy, SxxmSyy;
        double[] C = new double[4];
        int i;
        double mxEigenV;
        double oldg;
        double b, a, delta, qsqr;
        double normq;
        double a11, a12, a13, a14, a21, a22, a23, a24;
        double a31, a32, a33, a34, a41, a42, a43, a44;
        double a2, x2, y2, z2;
        double xy, az, zx, ay, yz, ax;
        double a3344_4334, a3244_4234, a3243_4233, a3143_4133, a3144_4134, a3142_4132;
        double evecprec = 1e-6;
        double evalprec = 1e-11;

        Sxx = A[0];
        Sxy = A[1];
        Sxz = A[2];
        Syx = A[3];
        Syy = A[4];
        Syz = A[5];
        Szx = A[6];
        Szy = A[7];
        Szz = A[8];

        Sxx2 = Sxx * Sxx;
        Syy2 = Syy * Syy;
        Szz2 = Szz * Szz;

        Sxy2 = Sxy * Sxy;
        Syz2 = Syz * Syz;
        Sxz2 = Sxz * Sxz;

        Syx2 = Syx * Syx;
        Szy2 = Szy * Szy;
        Szx2 = Szx * Szx;

        SyzSzymSyySzz2 = 2.0 * (Syz * Szy - Syy * Szz);
        Sxx2Syy2Szz2Syz2Szy2 = Syy2 + Szz2 - Sxx2 + Syz2 + Szy2;

        C[2] = -2.0 * (Sxx2 + Syy2 + Szz2 + Sxy2 + Syx2 + Sxz2 + Szx2 + Syz2 + Szy2);
        C[1] = 8.0 * (Sxx * Syz * Szy + Syy * Szx * Sxz + Szz * Sxy * Syx - Sxx * Syy * Szz - Syz * Szx * Sxy - Szy * Syx * Sxz);

        SxzpSzx = Sxz + Szx;
        SyzpSzy = Syz + Szy;
        SxypSyx = Sxy + Syx;
        SyzmSzy = Syz - Szy;
        SxzmSzx = Sxz - Szx;
        SxymSyx = Sxy - Syx;
        SxxpSyy = Sxx + Syy;
        SxxmSyy = Sxx - Syy;
        Sxy2Sxz2Syx2Szx2 = Sxy2 + Sxz2 - Syx2 - Szx2;

        C[0] = Sxy2Sxz2Syx2Szx2 * Sxy2Sxz2Syx2Szx2
                + (Sxx2Syy2Szz2Syz2Szy2 + SyzSzymSyySzz2) * (Sxx2Syy2Szz2Syz2Szy2 - SyzSzymSyySzz2)
                + (-(SxzpSzx) * (SyzmSzy) + (SxymSyx) * (SxxmSyy - Szz)) * (-(SxzmSzx) * (SyzpSzy) + (SxymSyx) * (SxxmSyy + Szz))
                + (-(SxzpSzx) * (SyzpSzy) - (SxypSyx) * (SxxpSyy - Szz)) * (-(SxzmSzx) * (SyzmSzy) - (SxypSyx) * (SxxpSyy + Szz))
                + (+(SxypSyx) * (SyzpSzy) + (SxzpSzx) * (SxxmSyy + Szz)) * (-(SxymSyx) * (SyzmSzy) + (SxzpSzx) * (SxxpSyy + Szz))
                + (+(SxypSyx) * (SyzmSzy) + (SxzmSzx) * (SxxmSyy - Szz)) * (-(SxymSyx) * (SyzpSzy) + (SxzmSzx) * (SxxpSyy - Szz));

        /* Newton-Raphson */
        mxEigenV = E0;
        for (i = 0; i < 50; i++) {
            oldg = mxEigenV;
            x2 = mxEigenV * mxEigenV;
            b = (x2 + C[2]) * mxEigenV;
            a = b + C[1];
            delta = ((a * mxEigenV + C[0]) / (2.0 * x2 * mxEigenV + b + a));
            mxEigenV -= delta;
            //rmsd = Math.sqrt(Math.abs(2d * (E0 - mxEigenV) / len));
            //System.out.println("Iteration " + i + " rmsd " + rmsd);
            if (Math.abs(mxEigenV - oldg) < Math.abs(evalprec * mxEigenV)) {
                break;
            }
        }

        if (i == 50) {
            System.out.println("More than " + i + " iterations needed!");
        }
        /* the Math.abs() is to guard against extremely small, but *negative* numbers due to floating point error */
        double score = Math.sqrt(Math.abs(2d * (E0 - mxEigenV) / len));

        //System.out.println("rmsd " + rmsd);
        if (minScore > 0) {
            if (score < minScore) {
                return -1; // Don't bother with rotation.
            }
        }
        a11 = SxxpSyy + Szz - mxEigenV;
        a12 = SyzmSzy;
        a13 = -SxzmSzx;
        a14 = SxymSyx;
        a21 = SyzmSzy;
        a22 = SxxmSyy - Szz - mxEigenV;
        a23 = SxypSyx;
        a24 = SxzpSzx;
        a31 = a13;
        a32 = a23;
        a33 = Syy - Sxx - Szz - mxEigenV;
        a34 = SyzpSzy;
        a41 = a14;
        a42 = a24;
        a43 = a34;
        a44 = Szz - SxxpSyy - mxEigenV;
        a3344_4334 = a33 * a44 - a43 * a34;
        a3244_4234 = a32 * a44 - a42 * a34;
        a3243_4233 = a32 * a43 - a42 * a33;
        a3143_4133 = a31 * a43 - a41 * a33;
        a3144_4134 = a31 * a44 - a41 * a34;
        a3142_4132 = a31 * a42 - a41 * a32;
        q1 = a22 * a3344_4334 - a23 * a3244_4234 + a24 * a3243_4233;
        q2 = -a21 * a3344_4334 + a23 * a3144_4134 - a24 * a3143_4133;
        q3 = a21 * a3244_4234 - a22 * a3144_4134 + a24 * a3142_4132;
        q4 = -a21 * a3243_4233 + a22 * a3143_4133 - a23 * a3142_4132;

        qsqr = q1 * q1 + q2 * q2 + q3 * q3 + q4 * q4;

        /**
         * The following code tries to calculate another column in the adjoint
         * matrix when the norm of the current column is too small. Usually this
         * block will never be activated. To be absolutely safe this should be
         * uncommented, but it is most likely unnecessary.
         */
        if (qsqr < evecprec) {
            q1 = a12 * a3344_4334 - a13 * a3244_4234 + a14 * a3243_4233;
            q2 = -a11 * a3344_4334 + a13 * a3144_4134 - a14 * a3143_4133;
            q3 = a11 * a3244_4234 - a12 * a3144_4134 + a14 * a3142_4132;
            q4 = -a11 * a3243_4233 + a12 * a3143_4133 - a13 * a3142_4132;
            qsqr = q1 * q1 + q2 * q2 + q3 * q3 + q4 * q4;

            if (qsqr < evecprec) {
                double a1324_1423 = a13 * a24 - a14 * a23, a1224_1422 = a12 * a24 - a14 * a22;
                double a1223_1322 = a12 * a23 - a13 * a22, a1124_1421 = a11 * a24 - a14 * a21;
                double a1123_1321 = a11 * a23 - a13 * a21, a1122_1221 = a11 * a22 - a12 * a21;

                q1 = a42 * a1324_1423 - a43 * a1224_1422 + a44 * a1223_1322;
                q2 = -a41 * a1324_1423 + a43 * a1124_1421 - a44 * a1123_1321;
                q3 = a41 * a1224_1422 - a42 * a1124_1421 + a44 * a1122_1221;
                q4 = -a41 * a1223_1322 + a42 * a1123_1321 - a43 * a1122_1221;
                qsqr = q1 * q1 + q2 * q2 + q3 * q3 + q4 * q4;

                if (qsqr < evecprec) {
                    q1 = a32 * a1324_1423 - a33 * a1224_1422 + a34 * a1223_1322;
                    q2 = -a31 * a1324_1423 + a33 * a1124_1421 - a34 * a1123_1321;
                    q3 = a31 * a1224_1422 - a32 * a1124_1421 + a34 * a1122_1221;
                    q4 = -a31 * a1223_1322 + a32 * a1123_1321 - a33 * a1122_1221;
                    qsqr = q1 * q1 + q2 * q2 + q3 * q3 + q4 * q4;

                    if (qsqr < evecprec) {
                        /* if qsqr is still too small, return the identity matrix. */
                        rotmat[0] = rotmat[4] = rotmat[8] = 1.0;
                        rotmat[1] = rotmat[2] = rotmat[3] = rotmat[5] = rotmat[6] = rotmat[7] = 0.0;

                        return 0;
                    }
                }
            }
        }

        normq = Math.sqrt(qsqr);
        q1 /= normq;
        q2 /= normq;
        q3 /= normq;
        q4 /= normq;

        a2 = q1 * q1;
        x2 = q2 * q2;
        y2 = q3 * q3;
        z2 = q4 * q4;

        xy = q2 * q3;
        az = q1 * q4;
        zx = q4 * q2;
        ay = q1 * q3;
        yz = q3 * q4;
        ax = q1 * q2;

        rotmat[0] = a2 + x2 - y2 - z2;
        rotmat[1] = 2 * (xy + az);
        rotmat[2] = 2 * (zx - ay);
        rotmat[3] = 2 * (xy - az);
        rotmat[4] = a2 - x2 + y2 - z2;
        rotmat[5] = 2 * (yz + ax);
        rotmat[6] = 2 * (zx + ay);
        rotmat[7] = 2 * (yz - ax);
        rotmat[8] = a2 - x2 - y2 + z2;

        return 1;
    }

    /**
     * For calculating the innerProduct.
     *
     * @param A
     * @return
     */
    protected double innerProduct(double[] A) {
        double x1, x2, y1, y2, z1, z2;
        double[] fx1 = coords1[0];
        double[] fy1 = coords1[1];
        double[] fz1 = coords1[2];
        double[] fx2 = coords2[0];
        double[] fy2 = coords2[1];
        double[] fz2 = coords2[2];
        double G1 = 0d, G2 = 0d;

        A[0] = A[1] = A[2] = A[3] = A[4] = A[5] = A[6] = A[7] = A[8] = 0d;

        if (weight != null) {
            for (int i = 0; i < len; i++) {
                x1 = weight[i] * fx1[i];
                y1 = weight[i] * fy1[i];
                z1 = weight[i] * fz1[i];

                G1 += x1 * fx1[i] + y1 * fy1[i] + z1 * fz1[i];

                x2 = fx2[i];
                y2 = fy2[i];
                z2 = fz2[i];

                G2 += weight[i] * (x2 * x2 + y2 * y2 + z2 * z2);

                A[0] += (x1 * x2);
                A[1] += (x1 * y2);
                A[2] += (x1 * z2);

                A[3] += (y1 * x2);
                A[4] += (y1 * y2);
                A[5] += (y1 * z2);

                A[6] += (z1 * x2);
                A[7] += (z1 * y2);
                A[8] += (z1 * z2);
            }
        } else {
            for (int i = 0; i < len; i++) {
                x1 = fx1[i];
                y1 = fy1[i];
                z1 = fz1[i];

                G1 += x1 * x1 + y1 * y1 + z1 * z1;

                x2 = fx2[i];
                y2 = fy2[i];
                z2 = fz2[i];

                G2 += (x2 * x2 + y2 * y2 + z2 * z2);

                A[0] += (x1 * x2);
                A[1] += (x1 * y2);
                A[2] += (x1 * z2);

                A[3] += (y1 * x2);
                A[4] += (y1 * y2);
                A[5] += (y1 * z2);

                A[6] += (z1 * x2);
                A[7] += (z1 * y2);
                A[8] += (z1 * z2);
            }
            weight = new double[len];
            for (int i = 0; i < len; i++) {
                weight[i] = i + 1d;
            }

        }
        return (G1 + G2) / 2d;
    }

    /**
     * For calculating centralised coordinates.
     *
     * @param coords The coordinates to centre.
     * @param len The number of coordinates.
     * @return The centralised coordinates.
     */
    public static double[][] centre(double[][] coords, int len) {
        double[][] r = new double[coords.length][len];
        double[] ave = new double[3];
        for (int i = 0; i < 3; i++) {
            ave[i] = 0d;
            for (int j = 0; j < len; j++) {
                ave[i] = ave[i] + coords[i][j];
            }
            ave[i] = ave[i] / (double) len;
            System.out.println("ave[" + i + "] " + ave[i]);
            for (int j = 0; j < len; j++) {
                r[i][j] = coords[i][j] - ave[i];
            }
        }
        return r;
    }

    /**
     * For printing the 3x3 matrix to std_out.
     *
     * @param matrix The matrix to print.
     */
    public static void print(double[] matrix) {
        for (int i = 0; i < 3; i++) {
            // 0, 3, 6; 1, 4, 7; 2, 5, 8
            System.out.println(matrix[3 * i] + " " + matrix[3 * i + 1] + " " + matrix[3 * i + 2]);
        }
    }

    /**
     * For printing coords to std_out.
     *
     * @param coords The coordinates to print.
     * @param len The number of coordinates.
     */
    public static void print(double[][] coords, int len) {
        for (int i = 0; i < len; i++) {
            System.out.println(coords[0][i] + " " + coords[1][i] + " " + coords[2][i]);
        }
    }
}
