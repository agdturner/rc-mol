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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import uk.ac.leeds.ccg.math.arithmetic.Math_Double;
import uk.ac.leeds.ccg.math.matrices.Math_Matrix_Double;
import static uk.ac.leeds.ccg.mol.geom.QCProt.calcRMSDRotationalMatrix;
import static uk.ac.leeds.ccg.mol.geom.QCProt.centre;
import static uk.ac.leeds.ccg.mol.geom.QCProt.print;

/**
 * QCProtTest.
 *
 * @author Andy Turner
 */
public class QCProtTest {

    public QCProtTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of calcRMSDRotationalMatrix method, of class QCProt.
     */
    @Test
    public void testCalcRMSDRotationalMatrix() {
        System.out.println("calcRMSDRotationalMatrix");
        /**
         * https://web.archive.org/web/20240203104803/https://theobald.brandeis.edu/qcp/main.c
         *
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

        QCProt.Res res = calcRMSDRotationalMatrix(frag_a, frag_b, len);

        double epsilon = 0.000001d;
        System.out.println("q1 = " + res.q1);
        // -0.8620062925928965
        double expectedQ1 = -0.8620063d;
        assertTrue(Math_Double.equals(expectedQ1, res.q1, epsilon));
        System.out.println("q2 = " + res.q2);
        // 0.3435504961738262
        double expectedQ2 = 0.3435505d;
        assertTrue(Math_Double.equals(expectedQ2, res.q2, epsilon));
        System.out.println("q3 = " + res.q3);
        double expectedQ3 = 0.1242953;
        assertTrue(Math_Double.equals(expectedQ3, res.q3, epsilon));
        System.out.println("q4 = " + res.q4);
        double expectedQ4 = -0.3513814;
        assertTrue(Math_Double.equals(expectedQ4, res.q4, epsilon));

        System.out.println("QCP Rotation matrix");
        double[][] expRotmat = new double[3][3];
        expRotmat[0][0] = 0.72216358d;
        expRotmat[0][1] = -0.52038257d;
        expRotmat[0][2] = -0.45572112d;
        expRotmat[1][0] = 0.69118937d;
        expRotmat[1][1] = 0.51700833d;
        expRotmat[1][2] = 0.50493528d;
        expRotmat[2][0] = -0.02714790d;
        expRotmat[2][1] = -0.67963547d;
        expRotmat[2][2] = 0.73304748d;
        print(res.rotmat);
        // 0.7221635837820651 0.6911893731786908 -0.02714790348982324
        // -0.5203825657891069 0.5170083254696894 -0.6796354733368274
        // -0.4557211246823982 0.5049352847641727 0.7330474846272469
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                System.out.print("expRotmat[i][j] " + expRotmat[i][j]);
                System.out.println(", res.rotmat[i + 3 * j] " + res.rotmat[i + 3 * j]);
                assertTrue(Math_Double.equals(expRotmat[i][j], res.rotmat[i + 3 * j], epsilon));
            }
        }

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
        double weuc_dist = 0d;
        double wtsum = 0d;
        for (int i = 0; i < len; i++) {
            wtsum += weight[i];
            double tmp = Math.pow(frag_a[0][i] - frag_b[0][i], 2)
                    + Math.pow(frag_a[1][i] - frag_b[1][i], 2)
                    + Math.pow(frag_a[2][i] - frag_b[2][i], 2);
            weuc_dist += weight[i] * tmp;
            euc_dist += tmp;
        }

        System.out.println("Coords 2 after rotation:");
        print(frag_b, len);

        System.out.println("Explicit Weighted RMSD calculated from transformed coords: " + Math.sqrt(euc_dist / wtsum));
        double rmsd =  Math.sqrt(euc_dist / (double) len);
        System.out.println("Explicit RMSD calculated from transformed coords: " + rmsd);
        // 0.7191064509622259
        double expectedRMSD = 0.719106d;
        assertTrue(Math_Double.equals(expectedRMSD, rmsd, epsilon));
        
    }

    

}
