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
        double[][] coords1 = new double[3][len];
        coords1[0][0] = -2.803;
        coords1[1][0] = -15.373;
        coords1[2][0] = 24.556;
        coords1[0][1] = 0.893;
        coords1[1][1] = -16.062;
        coords1[2][1] = 25.147;
        coords1[0][2] = 1.368;
        coords1[1][2] = -12.371;
        coords1[2][2] = 25.885;
        coords1[0][3] = -1.651;
        coords1[1][3] = -12.153;
        coords1[2][3] = 28.177;
        coords1[0][4] = -0.440;
        coords1[1][4] = -15.218;
        coords1[2][4] = 30.068;
        coords1[0][5] = 2.551;
        coords1[1][5] = -13.273;
        coords1[2][5] = 31.372;
        coords1[0][6] = 0.105;
        coords1[1][6] = -11.330;
        coords1[2][6] = 33.567;
        double[][] coords2 = new double[3][len];
        coords2[0][0] = -14.739;
        coords2[1][0] = -18.673;
        coords2[2][0] = 15.040;
        coords2[0][1] = -12.473;
        coords2[1][1] = -15.810;
        coords2[2][1] = 16.074;
        coords2[0][2] = -14.802;
        coords2[1][2] = -13.307;
        coords2[2][2] = 14.408;
        coords2[0][3] = -17.782;
        coords2[1][3] = -14.852;
        coords2[2][3] = 16.171;
        coords2[0][4] = -16.124;
        coords2[1][4] = -14.617;
        coords2[2][4] = 19.584;
        coords2[0][5] = -15.029;
        coords2[1][5] = -11.037;
        coords2[2][5] = 18.902;
        coords2[0][6] = -18.577;
        coords2[1][6] = -10.001;
        coords2[2][6] = 17.996;

        System.out.println("Coords before centering");
        System.out.println("coords1");
        print(coords1, len);
        System.out.println("coords2");
        print(coords2, len);

        coords1 = centre(coords1, len);
        coords2 = centre(coords2, len);
        System.out.println("Coords after centering");
        System.out.println("coords1");
        print(coords1, len);
        System.out.println("coords2");
        print(coords2, len);

        QCProt res = new QCProt(coords1, coords2);
        
        res.getRotatedCoordinates();

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

        System.out.println("Coords 2 after rotation:");
        print(res.coords2Rotated, len);

        System.out.println("Explicit Weighted RMSD calculated from transformed coords: " + res.wrmsd);
        System.out.println("Explicit RMSD calculated from transformed coords: " + res.rmsd);
        // 0.7191064509622259
        double expectedRMSD = 0.719106d;
        assertTrue(Math_Double.equals(expectedRMSD, res.rmsd, epsilon));
        
    }

    

}
