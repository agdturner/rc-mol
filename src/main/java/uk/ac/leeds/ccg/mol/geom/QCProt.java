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
 */
package uk.ac.leeds.ccg.mol.geom;

import ch.obermuhlner.math.big.BigRational;
import uk.ac.leeds.ccg.math.matrices.Math_Matrix_Double;

/**
 *
 * @author Andy Turner
 */
public class QCProt {

    /**
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
     *  * Redistributions of source code must retain the above copyright notice,
     * this list of conditions and the following disclaimer. * Redistributions
     * in binary form must reproduce the above copyright notice, this list of
     * conditions and the following disclaimer in the documentation and/or other
     * materials provided with the distribution. * Neither the name of the
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
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Sample code to use the routine for fast RMSD & rotational matrix
         * calculation. Note that we superposition frag_b onto frag_a. For the
         * example provided below, the minimum least-squares RMSD for the two
         * 7-atom fragments should be 0.719106 A.
         *
         * The rotation quaterion should be:
         *
         * -8.620063e-01 3.435505e-01 1.242953e-01 -3.513814e-01
         *
         * And the corresponding 3x3 rotation matrix is:
         *
         * [ 0.72216358 0.69118937 -0.02714790 ] [ -0.52038257 0.51700833
         * -0.67963547 ] [ -0.45572112 0.50493528 0.73304748 ]
         *
         * Compile instruction:
         *
         * make
         *
         * How to incorporate the code into your own package.
         *
         * 1. copy the qcprot.h and qcprot.c into the source directory 2. change
         * your own code to call the fast rotational routine and include the
         * qcprot.h 3. change your make file to include qcprot.c
         */
        int len = 7;
        double[][] frag_a = new double[3][len];
        frag_a[0][0] = -2.803;
        frag_a[1][0] = -15.373;
        frag_a[2][0] = 24.556;
        frag_a[0][1] = 0.893;
        frag_a[1][1] = -16.062;
        frag_a[2][1] = 25.147;
        frag_a[0][2] = 1.368;
        frag_a[1][2] = -12.371;
        frag_a[2][2] = 25.885;
        frag_a[0][3] = -1.651;
        frag_a[1][3] = -12.153;
        frag_a[2][3] = 28.177;
        frag_a[0][4] = -0.440;
        frag_a[1][4] = -15.218;
        frag_a[2][4] = 30.068;
        frag_a[0][5] = 2.551;
        frag_a[1][5] = -13.273;
        frag_a[2][5] = 31.372;
        frag_a[0][6] = 0.105;
        frag_a[1][6] = -11.330;
        frag_a[2][6] = 33.567;
        double[][] frag_b = new double[3][len];
        frag_b[0][0] = -14.739;
        frag_b[1][0] = -18.673;
        frag_b[2][0] = 15.040;
        frag_b[0][1] = -12.473;
        frag_b[1][1] = -15.810;
        frag_b[2][1] = 16.074;
        frag_b[0][2] = -14.802;
        frag_b[1][2] = -13.307;
        frag_b[2][2] = 14.408;
        frag_b[0][3] = -17.782;
        frag_b[1][3] = -14.852;
        frag_b[2][3] = 16.171;
        frag_b[0][4] = -16.124;
        frag_b[1][4] = -14.617;
        frag_b[2][4] = 19.584;
        frag_b[0][5] = -15.029;
        frag_b[1][5] = -11.037;
        frag_b[2][5] = 18.902;
        frag_b[0][6] = -18.577;
        frag_b[1][6] = -10.001;
        frag_b[2][6] = 17.996;
        Math_Matrix_Double mfrag_a = new Math_Matrix_Double(frag_a);

        double[] weight = new double[len];
        for (int i = 0; i < len; i++) {
            weight[i] = i + 1d;
        }

        System.out.println("Coords before centering");
        System.out.println("frag_a");
        print(frag_a, len);
        System.out.println("frag_b");
        print(frag_b, len);

        frag_a = centre(frag_a, len);
        frag_b = centre(frag_b, len);
        System.out.println("Coords after centering");
        System.out.println("frag_a");
        print(frag_a, len);
        System.out.println("frag_b");
        print(frag_b, len);

        Res res = calcRMSDRotationalMatrix(frag_a, frag_b, len);

        System.out.println("RMSD " + res.rmsd);
        System.out.println("q1 = " + res.q1);
        System.out.println("q2 = " + res.q2);
        System.out.println("q3 = " + res.q3);
        System.out.println("q4 = " + res.q4);

        System.out.println("QCP Rotation matrix");
        print(res.rotmat);

        /* apply rotation matrix */
        double x, y, z;
        for (int i = 0; i < len; i++) {
            x = res.rotmat[0] * frag_b[0][i] + res.rotmat[1] * frag_b[1][i] + res.rotmat[2] * frag_b[2][i];
            y = res.rotmat[3] * frag_b[0][i] + res.rotmat[4] * frag_b[1][i] + res.rotmat[5] * frag_b[2][i];
            z = res.rotmat[6] * frag_b[0][i] + res.rotmat[7] * frag_b[1][i] + res.rotmat[8] * frag_b[2][i];

            frag_b[0][i] = x;
            frag_b[1][i] = y;
            frag_b[2][i] = z;
        }

        /* calculate euclidean distance */
        double euc_dist = 0d;
        double wtsum = 0d;
        for (int i = 0; i < len; i++) {
            wtsum += weight[i];
            double tmp = Math.pow(frag_a[0][i] - frag_b[0][i], 2)
                    + Math.pow(frag_a[1][i] - frag_b[1][i], 2)
                    + Math.pow(frag_a[2][i] - frag_b[2][i], 2);
            euc_dist += weight[i] * tmp;
        }

        System.out.println("Coords 2 after rotation:");
        print(frag_b, len);

        System.out.println("Explicit RMSD calculated from transformed coords: " + Math.sqrt(euc_dist / wtsum));

    }

    protected static Res calcRMSDRotationalMatrix(
            double[][] coords1, double[][] coords2, int len) {
        Res res = new Res(len);
        double wsum;
        if (res.weight == null) {
            wsum = len;
        } else {
            wsum = 0.0;
            for (int i = 0; i < len; i++) {
                wsum += res.weight[i];
            }
        }

        double[] A = new double[9];
        /* calculate the (weighted) inner product of two structures */
        double E0 = innerProduct(A, coords1, coords2, len, res);

        /* calculate the RMSD & rotational matrix */
        fastCalcRMSDAndRotation(A, E0, wsum, -1, res, -1d);

        return res;
    }

    protected static int fastCalcRMSDAndRotation(double[] A, double E0, double wsum, double len, Res res, double minScore) {
        double Sxx, Sxy, Sxz, Syx, Syy, Syz, Szx, Szy, Szz;
        double Szz2, Syy2, Sxx2, Sxy2, Syz2, Sxz2, Syx2, Szy2, Szx2,
                SyzSzymSyySzz2, Sxx2Syy2Szz2Syz2Szy2, Sxy2Sxz2Syx2Szx2,
                SxzpSzx, SyzpSzy, SxypSyx, SyzmSzy,
                SxzmSzx, SxymSyx, SxxpSyy, SxxmSyy;
        double[] C = new double[4];
        int i;
        double mxEigenV;
        double oldg = 0.0;
        double b, a, delta, rms, qsqr;
        double q1, q2, q3, q4;
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
        for (i = 0; i < 50; ++i) {
            oldg = mxEigenV;
            x2 = mxEigenV * mxEigenV;
            b = (x2 + C[2]) * mxEigenV;
            a = b + C[1];
            delta = ((a * mxEigenV + C[0]) / (2.0 * x2 * mxEigenV + b + a));
            mxEigenV -= delta;
            /* printf("\n diff[%3d]: %16g %16g %16g", i, mxEigenV - oldg, evalprec*mxEigenV, mxEigenV); */
            if (Math.abs(mxEigenV - oldg) < Math.abs(evalprec * mxEigenV)) {
                break;
            }
        }

        if (i == 50) {
            //System.out.println(stderr,"\nMore than %d iterations needed!\n", i);
        }
        /* the Math.abs() is to guard against extremely small, but *negative* numbers due to floating point error */
        res.rmsd = Math.sqrt(Math.abs(2d * (E0 - mxEigenV) / len));

        if (minScore > 0) {
            if (res.rmsd < minScore) {
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

        /* The following code tries to calculate another column in the adjoint matrix when the norm of the
   current column is too small.
   Usually this block will never be activated.  To be absolutely safe this should be
   uncommented, but it is most likely unnecessary.
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
                        res.rotmat[0] = res.rotmat[4] = res.rotmat[8] = 1.0;
                        res.rotmat[1] = res.rotmat[2] = res.rotmat[3] = res.rotmat[5] = res.rotmat[6] = res.rotmat[7] = 0.0;

                        res.q1 = q1;
                        res.q2 = q2;
                        res.q3 = q3;
                        res.q4 = q4;

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

        res.rotmat[0] = a2 + x2 - y2 - z2;
        res.rotmat[1] = 2 * (xy + az);
        res.rotmat[2] = 2 * (zx - ay);
        res.rotmat[3] = 2 * (xy - az);
        res.rotmat[4] = a2 - x2 + y2 - z2;
        res.rotmat[5] = 2 * (yz + ax);
        res.rotmat[6] = 2 * (zx + ay);
        res.rotmat[7] = 2 * (yz - ax);
        res.rotmat[8] = a2 - x2 - y2 + z2;

        res.q1 = q1;
        res.q2 = q2;
        res.q3 = q3;
        res.q4 = q4;

        return 1;
    }

    protected static double innerProduct(double[] A, double[][] coords1, double[][] coords2, int len, Res res) {
        double x1, x2, y1, y2, z1, z2;
        double[] fx1 = coords1[0];
        double[] fy1 = coords1[1];
        double[] fz1 = coords1[2];
        double[] fx2 = coords2[0];
        double[] fy2 = coords2[1];
        double[] fz2 = coords2[2];
        double G1 = 0d, G2 = 0d;

        A[0] = A[1] = A[2] = A[3] = A[4] = A[5] = A[6] = A[7] = A[8] = 0d;

        if (res.weight != null) {
            for (int i = 0; i < len; i++) {
                x1 = res.weight[i] * fx1[i];
                y1 = res.weight[i] * fy1[i];
                z1 = res.weight[i] * fz1[i];

                G1 += x1 * fx1[i] + y1 * fy1[i] + z1 * fz1[i];

                x2 = fx2[i];
                y2 = fy2[i];
                z2 = fz2[i];

                G2 += res.weight[i] * (x2 * x2 + y2 * y2 + z2 * z2);

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
        }
        return (G1 + G2) / 2d;
    }

    public static class Res {

        double q1, q2, q3, q4;
        double rmsd;
        double[] rotmat;
        double[] weight;

        public Res(int len) {
            rotmat = new double[9];
        }
    }

    protected static double[][] centre(double[][] coords, int len) {
        double[][] result = new double[coords.length][len];
        double[] ave = new double[3];
        for (int i = 0; i < 3; i++) {
            ave[i] = 0d;
            for (int j = 0; j < len; j++) {
                ave[i] = ave[i] + coords[i][j];
            }
            ave[i] = ave[i] / (double) len;
            System.out.println("ave[" + i + "] " + ave[i]);
            for (int j = 0; j < len; j++) {
                result[i][j] = coords[i][j] - ave[i];
            }
        }
        return result;
    }

    protected static void print(double[] matrix) {
        for (int i = 0; i < 3; i++) {
            // 0, 3, 6; 1, 4, 7; 2, 5, 8
            System.out.println(matrix[3 * i] + " " + matrix[3 * i + 1] + " " + matrix[3 * i + 2]);
        }
    }

    protected static void print(double[][] coords, int len) {
        for (int i = 0; i < len; i++) {
            System.out.println(coords[0][i] + " " + coords[1][i] + " " + coords[2][i]);
        }
    }
}
