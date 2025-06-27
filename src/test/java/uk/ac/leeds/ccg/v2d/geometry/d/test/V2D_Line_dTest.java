/*
 * Copyright 2020 Andy Turner, University of Leeds.
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
package uk.ac.leeds.ccg.v2d.geometry.d.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import uk.ac.leeds.ccg.math.matrices.Math_Matrix_Double;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Geometry_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Line_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_LineSegment_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Point_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Vector_d;

/**
 * Test class for V2D_Line_d.
 *
 * @author Andy Turner
 * @version 1.0
 */
public class V2D_Line_dTest extends V2D_Test_d {

    private static final long serialVersionUID = 1L;

    public V2D_Line_dTest() {
        super();
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
     * Test of toString method, of class V2D_Line_d.
     */
    @Test
    public void testToString_0args() {
        System.out.println("toString");
        V2D_Line_d instance = new V2D_Line_d(pP0P0, pP1P0);
        String expResult = """
                           V2D_Line_d
                           (
                            offset=V2D_Vector_d(dx=0.0, dy=0.0),
                            p=V2D_Point_d(offset=V2D_Vector_d(dx=0.0, dy=0.0), rel=V2D_Vector_d(dx=0.0, dy=0.0)),
                            v= V2D_Vector_d(dx=1.0, dy=0.0)
                           )""";
        String result = instance.toString();
        //System.out.println(result);
        assertTrue(expResult.equalsIgnoreCase(result));
    }

    /**
     * Test of intersects method, of class V2D_Line_d.
     */
    @Test
    public void testIsIntersectedBy_double_V2D_Point_d() {
        System.out.println("intersects");
        double epsilon = 0.0000000001d;
        V2D_Point_d pt = pP0P0;
        V2D_Line_d instance = new V2D_Line_d(pN1N1, pP1P1);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 2
        pt = new V2D_Point_d(env, P0_1E2, P0_1E2);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 3 works as the rounding puts pt on the line.
        pt = new V2D_Point_d(env, P0_1E12, P0_1E12);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 4 works as the rounding puts pt on the line.
        pt = new V2D_Point_d(env, N0_1E12, N0_1E12);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 5 works as the rounding puts pt on the line.
        double a = P0_1E2 + P1E12;
        pt = new V2D_Point_d(env, a, a);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 6 works as the rounding puts pt on the line.
        a = N0_1E2 + N1E12;
        pt = new V2D_Point_d(env, a, a);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 7
        instance = new V2D_Line_d(pP0N1, pP2P1);
        pt = new V2D_Point_d(env, -1d, -2d);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 8
        //epsilon = 0.00000000001d; // Too big!
        epsilon = 0.000000000001d;
        a = N0_1E2 + N1E12;
        pt = new V2D_Point_d(env, a, a);
        assertFalse(instance.intersects(epsilon, pt));
        pt = new V2D_Point_d(env, a + 1d, a);
        assertTrue(instance.intersects(epsilon, pt));
        // Test 9
        a = N0_1E12 + N1E12;
        pt = new V2D_Point_d(env, a, a);
        assertFalse(instance.intersects(epsilon, pt));
        pt = new V2D_Point_d(env, a + 1d, a);
        assertTrue(instance.intersects(epsilon, pt));
    }

    /**
     * Test of isParallel method, of class V2D_Line_d.
     */
    @Test
    public void testIsParallel_V2D_Line_d_double() {
        System.out.println("isParallel");
        double epsilon = 0.000000000001d;
        V2D_Line_d l = V2D_Line_d.X_AXIS;
        V2D_Line_d instance = V2D_Line_d.X_AXIS;
        assertTrue(instance.isParallel(l, epsilon));
        // Test 2
        instance = V2D_Line_d.Y_AXIS;
        assertFalse(instance.isParallel(l, epsilon));
        // Test 3
        instance = new V2D_Line_d(pP0P1, pP1P1);
        assertTrue(instance.isParallel(l, epsilon));
        // Test 4
        instance = new V2D_Line_d(pP0P1, pP1P1);
        assertTrue(instance.isParallel(l, epsilon));
        // Test 5
        instance = new V2D_Line_d(pP0P0, pP1P0);
        assertTrue(instance.isParallel(l, epsilon));
        // Test 6
        instance = new V2D_Line_d(pP1P0, pP0P0);
        assertTrue(instance.isParallel(l, epsilon));
        // Test 8
        instance = new V2D_Line_d(pP1P0, pP0P1);
        assertFalse(instance.isParallel(l, epsilon));
        // Test 9
        l = V2D_Line_d.Y_AXIS;
        instance = new V2D_Line_d(pP0P0, pP0P1);
        assertTrue(instance.isParallel(l, epsilon));
        // Test 9
        instance = new V2D_Line_d(pP1P0, pP1P1);
        assertTrue(instance.isParallel(l, epsilon));
        // Test 10
        instance = new V2D_Line_d(pN1P0, pN1P1);
        assertTrue(instance.isParallel(l, epsilon));
        // Test 12
        double a = P0_1E12 + P1E12;
        double b = N0_1E12 + N1E12;
        double a1 = P0_1E12 + P1E12 + 1d;
        double b1 = N0_1E12 + N1E12 + 1d;
        l = new V2D_Line_d(new V2D_Point_d(env, a, a), new V2D_Point_d(env, b, b));
        instance = new V2D_Line_d(new V2D_Point_d(env, a1, a), new V2D_Point_d(env, b1, b));
        epsilon = 0.00000001d;
        assertTrue(instance.isParallel(l, epsilon));
        // Test 13
        a = P0_1E12 + P1E12;
        b = N0_1E12 + N1E12;
        a1 = P0_1E12 + P1E12 + 10d;
        b1 = N0_1E12 + N1E12 + 10d;
        l = new V2D_Line_d(new V2D_Point_d(env, a, a), new V2D_Point_d(env, b, b));
        instance = new V2D_Line_d(new V2D_Point_d(env, a1, a),
                new V2D_Point_d(env, b1, b));
        assertTrue(instance.isParallel(l, epsilon));
    }

    /**
     * Test of getIntersect method, of class V2D_Line_d.
     */
    @Test
    public void testGetIntersection_V2D_Line_d_double() {
        System.out.println("getIntersection");
        double epsilon = 0.0000000001d;
        V2D_Line_d l;
        V2D_Line_d instance;
        V2D_Geometry_d expResult;
        V2D_Geometry_d result;
        // Test 1
        //l = new V2D_Line_d(N1N1, P1P1);
        l = new V2D_Line_d(pP1P1, pN1N1);
        //instance = new V2D_Line_d(N1P1, P1N1);
        instance = new V2D_Line_d(pP1N1, pN1P1);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 2
        l = new V2D_Line_d(pN1N1, pP1P1);
        instance = new V2D_Line_d(pP1P1, pP0P2);
        expResult = pP1P1;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 3
        expResult = pP0P0;
        instance = new V2D_Line_d(pP1N1, pN1P1);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 4
        l = new V2D_Line_d(pN1N1, pP1P1);
        instance = new V2D_Line_d(env, new V2D_Vector_d(3d, 2d), new V2D_Vector_d(5d, 2d));
        expResult = pP2P2;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 5
        l = new V2D_Line_d(pN1N1, pP1P1);
        instance = new V2D_Line_d(pP1N1, pN1P1);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 6
        l = new V2D_Line_d(pP0P0, pP1P1);
        instance = new V2D_Line_d(pP1N1, pN1P1);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 7
        l = new V2D_Line_d(pN1N1, pP0P0);
        instance = new V2D_Line_d(pP1N1, pN1P1);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 8
        l = new V2D_Line_d(pN2N2, pN1N1);
        instance = new V2D_Line_d(pP1N1, pP0P0);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 9
        l = new V2D_Line_d(pN2N2, pN1N1);
        instance = new V2D_Line_d(pP0P0, pN1P1);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 10 to 12
        // v.dx = 0, v.dy != 0
        // Test 10
        l = new V2D_Line_d(pN1N1, pP1P1);
        expResult = pP0P0;
        instance = new V2D_Line_d(pP0P0, pP0P1);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 11
        l = new V2D_Line_d(pP0N1, pP2P1);
        expResult = pP1P0;
        instance = new V2D_Line_d(pP1P0, pP1P1);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 12
        l = new V2D_Line_d(env, P0N1, new V2D_Vector_d(2d, 1d));
        expResult = pP1P0;
        instance = new V2D_Line_d(env, new V2D_Vector_d(1d, 0d),
                new V2D_Vector_d(1d, 1d));
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 12 to 14
        // v.dx != 0, v.dy = 0
        // Test 12
        l = new V2D_Line_d(pN1N1, pP1P1);
        expResult = pP0P0;
        instance = new V2D_Line_d(pP0P0, pP1P0);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 13
        l = new V2D_Line_d(pP0N1, pP2P1);
        expResult = pP1P0;
        instance = new V2D_Line_d(pP1P0, pP2P0);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 14
        l = new V2D_Line_d(pP0N1, pP2P1);
        expResult = pP1P0;
        instance = new V2D_Line_d(pP1P0, pP2P0);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 15
        l = new V2D_Line_d(pN1N1, pP1P1);
        expResult = pP0P0;
        instance = new V2D_Line_d(pP0P0, pP1P0);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 16
        l = new V2D_Line_d(pP0N1, pP2P1);
        expResult = pP1P0;
        instance = new V2D_Line_d(pP1P0, pP2P0);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 17
        l = new V2D_Line_d(env, P0P1, new V2D_Vector_d(2d, 3d));
        expResult = pP1P2;
        instance = new V2D_Line_d(pP1P2, pP2P2);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result));
        // Test 18
        epsilon = 1d / 10000000d;
        l = new V2D_Line_d(pP0P0, pP1P1);
        instance = new V2D_Line_d(pP0P0, pP1P1);
        expResult = new V2D_Line_d(pP0P0, pP1P1);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Line_d) expResult).equals((V2D_Line_d) result, epsilon));
        // Test 19
        instance = new V2D_Line_d(pP1P1, pP0P0);
        expResult = new V2D_Line_d(pP0P0, pP1P1);
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Line_d) expResult).equals((V2D_Line_d) result, epsilon));
        // Test 20
        //instance = new V2D_Line_d(P0P1, P0N1);
        instance = new V2D_Line_d(pP0N1, pP0P1);
        l = new V2D_Line_d(pP1P1, pP0P0);
        expResult = pP0P0;
        epsilon = 1d / 100000000000d;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result, epsilon));
        // Test 21
        instance = new V2D_Line_d(pN1P1, pP1N1);
        l = new V2D_Line_d(pP0P2, pP1P1);
        //expResult = null;
        result = instance.getIntersect(epsilon, l);
        //System.out.println(result);
        assertNull(result);
        // Test 22
        l = new V2D_Line_d(pN1N1, pP1P1);
        instance = new V2D_Line_d(pN1P1, pP1N1);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result, epsilon));
        // Test 23
        l = new V2D_Line_d(pN1N1, pP1P1);
        instance = new V2D_Line_d(pN1P1, pP1N1);
        expResult = pP0P0;
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result, epsilon));
        // Test 24
        l = new V2D_Line_d(new V2D_Point_d(env, -1d + (0.1d), -1d + (0.1d)),
                new V2D_Point_d(env, 1d + (0.1d), 1d + (0.1d)));
        instance = new V2D_Line_d(new V2D_Point_d(env, -1d + (0.1d), 1d + (0.1d)),
                new V2D_Point_d(env, 1d + (0.1d), -1d + (0.1d)));
        expResult = new V2D_Point_d(env, 0d + (0.1d), 0d + (0.1d));
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result, epsilon));
        // Test 25
        epsilon = 1d / 100000d;
        l = new V2D_Line_d(new V2D_Point_d(env, -100d, -100d),
                new V2D_Point_d(env, 100d, 100d));
        instance = new V2D_Line_d(new V2D_Point_d(env, -100d, -100d),
                new V2D_Point_d(env, 100d, 100d));
        expResult = new V2D_Line_d(new V2D_Point_d(env, -100d, -100d),
                new V2D_Point_d(env, 100d, 100d));
//        expResult = new V2D_Line_d(new V2D_Point_d(-100d, -100d),
//                new V2D_Point_d(100d, 100d));
        result = instance.getIntersect(epsilon, l);
        assertTrue(((V2D_Line_d) expResult).equals((V2D_Line_d) result, epsilon));
    }

    /**
     * Test of equals method, of class V2D_Line_d.
     */
    @Test
    public void testEquals_double_V2D_Line_d() {
        System.out.println("equals");
        double epsilon = 1d / 10000000d;
        V2D_Line_d l = new V2D_Line_d(pP0P0, pP1P1);
        V2D_Line_d instance = new V2D_Line_d(pP0P0, pP1P1);
        assertTrue(instance.equals(l, epsilon));
        // Test 2
        instance = new V2D_Line_d(pP1P1, pP0P0);
        assertTrue(instance.equals(l, epsilon));
        // Test 3
        l = V2D_Line_d.X_AXIS;
        instance = V2D_Line_d.X_AXIS;
        assertTrue(instance.equals(l, epsilon));
        // Test 4
        instance = V2D_Line_d.Y_AXIS;
        assertFalse(instance.equals(l, epsilon));
    }

    /**
     * Test of isParallelToX0 method, of class V2D_Line_d.
     */
    @Test
    public void testIsParallelToX0() {
        System.out.println("isParallelToX0");
        //double epsilon = 1d / 10000000d;
        V2D_Line_d instance = new V2D_Line_d(pP1P0, pP1P1);
        assertTrue(instance.isParallelToX0());
        // Test 2
        instance = new V2D_Line_d(pP0P0, pP0P1);
        assertTrue(instance.isParallelToX0());
        // Test 3
        instance = new V2D_Line_d(pP0P0, pP1P1);
        assertFalse(instance.isParallelToX0());
    }

    /**
     * Test of isParallelToY0 method, of class V2D_Line_d.
     */
    @Test
    public void testIsParallelToY0() {
        System.out.println("isParallelToY0");
        V2D_Line_d instance = new V2D_Line_d(pP0P1, pP1P1);
        assertTrue(instance.isParallelToY0());
        // Test 2
        instance = new V2D_Line_d(pP0P0, pP1P0);
        assertTrue(instance.isParallelToY0());
        // Test 3
        instance = new V2D_Line_d(pP0P0, pP1P1);
        assertFalse(instance.isParallelToY0());
    }

    /**
     * Test of getAsMatrix method, of class V2D_Line_d.
     */
    @Test
    public void testGetAsMatrix() {
        System.out.println("getAsMatrix");
        V2D_Line_d instance = V2D_Line_d.X_AXIS;
        double[][] m = new double[2][2];
        m[0][0] = 0d;
        m[0][1] = 0d;
        m[1][0] = 1d;
        m[1][1] = 0d;
        Math_Matrix_Double expResult = new Math_Matrix_Double(m);
        Math_Matrix_Double result = instance.getAsMatrix();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of getDistance method, of class V2D_Line_d.
     */
    @Test
    public void testGetDistance_V2D_Point_d_double() {
        System.out.println("getDistance");
        double epsilon = 1d / 10000000d;
        V2D_Point_d pt;
        V2D_Line_d instance;
        double expResult;
        double result;
        // Test 1
        pt = pP0P0;
        instance = new V2D_Line_d(pP1P0, pP1P1);
        expResult = 1d;
        result = instance.getDistance(pt, epsilon);
        assertTrue(expResult == result);
        // Test 2
        instance = new V2D_Line_d(pP0P1, pP1P1);
        result = instance.getDistance(pt, epsilon);
        assertTrue(expResult == result);
        // Test 3
        pt = pP1P1;
        expResult = 0d;
        instance = new V2D_Line_d(pP0P0, pP1P1);
        result = instance.getDistance(pt, epsilon);
        assertTrue(expResult == result);
        // Test 4
        pt = pP0P1;
        instance = new V2D_Line_d(pP0P0, pP1P1);
        expResult = Math.sqrt(1d / 2d);
        result = instance.getDistance(pt, epsilon);
        assertTrue(expResult == result);
        // Test 5
        instance = new V2D_Line_d(pP0P0, pP0P1);
        pt = pP1P0;
        expResult = 1d;
        result = instance.getDistance(pt, epsilon);
        assertTrue(expResult == result);
    }

    /**
     * Test of getLineOfIntersect method, of class V2D_Line_d.
     */
    @Test
    public void testGetLineOfIntersection_V2D_Point_d_double() {
        System.out.println("getLineOfIntersection");
        double epsilon = 1d / 10000000d;
        V2D_Point_d pt;
        V2D_Line_d instance;
        V2D_LineSegment_d expResult;
        V2D_Geometry_d result;
        // Test 1
        pt = pP0P0;
        instance = new V2D_Line_d(pP1P0, pP1P1);
        expResult = new V2D_LineSegment_d(pP0P0, pP1P0);
        result = instance.getLineOfIntersect(pt, epsilon);
        assertTrue(expResult.equalsIgnoreDirection(epsilon, (V2D_LineSegment_d) result));
        // Test 2
        instance = new V2D_Line_d(pP1N1, pP1P1);
        expResult = new V2D_LineSegment_d(pP0P0, pP1P0);
        //result = instance.getLineOfIntersect(pt, epsilon);
        //System.out.println(result);
        result = instance.getLineOfIntersect(pt, epsilon);
        assertTrue(expResult.equalsIgnoreDirection(epsilon, (V2D_LineSegment_d) result));
    }

    /**
     * Test of getPointOfIntersection method, of class V2D_Line_d. No test:
     * Test covered by {@link #testGetLineOfIntersection_V2D_Point_d()}
     */
    @Test
    public void testGetPointOfIntersection() {
        System.out.println("getPointOfIntersection");
        double epsilon = 1d / 10000000d;
        V2D_Point_d pt = pP2P0;
        V2D_Line_d instance = new V2D_Line_d(pP0P0, pP0P2);
        V2D_Point_d expResult = pP0P0;
        V2D_Point_d result = instance.getPointOfIntersect(pt, epsilon);
        assertTrue(expResult.equals(result, epsilon));
        // Test 2
        pt = pN2P0;
        instance = new V2D_Line_d(pP0P0, pP0P2);
        expResult = pP0P0;
        result = instance.getPointOfIntersect(pt, epsilon);
        assertTrue(expResult.equals(result, epsilon));
        // Test 3
        pt = pN2P2;
        instance = new V2D_Line_d(pP0P0, pP0P2);
        expResult = pP0P2;
        result = instance.getPointOfIntersect(pt, epsilon);
        assertTrue(expResult.equals(result, epsilon));
        // Test 4
        pt = pN2N2;
        instance = new V2D_Line_d(pP0P0, pP0P2);
        expResult = pP0N2;
        result = instance.getPointOfIntersect(pt, epsilon);
        assertTrue(expResult.equals(result, epsilon));
    }

    /**
     * Test of getLineOfIntersect method, of class V2D_Line_d.
     */
    @Test
    public void testGetLineOfIntersection_V2D_Line_d_double() {
        System.out.println("getLineOfIntersection");
        double epsilon = 1d / 10000000d;
        V2D_Line_d l = new V2D_Line_d(pP1P0, pP1P1);
        V2D_Point_d p = new V2D_Point_d(pP0P0);
        V2D_Geometry_d expResult = new V2D_LineSegment_d(pP0P0, pP1P0);
        V2D_Geometry_d result = l.getLineOfIntersect(p, epsilon);
        assertTrue(((V2D_LineSegment_d) expResult).equalsIgnoreDirection(epsilon, (V2D_LineSegment_d) result));
        // Test 2
        l = new V2D_Line_d(pP0P0, pP0P1);
        result = l.getLineOfIntersect(p, epsilon);
        expResult = new V2D_Point_d(pP0P0);
        assertTrue(((V2D_Point_d) expResult).equals((V2D_Point_d) result, epsilon));
    }

    /**
     * Test of getDistance method, of class V2D_Line_d.
     */
    @Test
    public void testGetDistance_V2D_Line_d_double() {
        System.out.println("getDistance");
        double epsilon = 1d / 100000000d;
        V2D_Line_d l;
        V2D_Line_d instance;
        double expResult;
        double result;
        // Test 1
        l = new V2D_Line_d(pP0P0, pP1P1);
        instance = new V2D_Line_d(pP1N1, pP2P0);
        expResult = Math.sqrt(2d);
        result = instance.getDistance(l, epsilon);
        assertTrue(expResult == result);
    }

    /**
     * Test of toString method, of class V2D_Line_d.
     */
    @Test
    public void testToString_String() {
        System.out.println("toString");
        String pad = "";
        V2D_Line_d instance = V2D_Line_d.X_AXIS;
        String expResult = """
                           V2D_Line_d
                           (
                            offset=V2D_Vector_d
                            (
                             dx=0.0,
                             dy=0.0
                            )
                            ,
                            p=V2D_Point_d
                            (
                             offset=V2D_Vector_d
                             (
                              dx=0.0,
                              dy=0.0
                             )
                             ,
                             rel=V2D_Vector_d
                             (
                              dx=0.0,
                              dy=0.0
                             )
                            )
                            ,
                            v=V2D_Vector_d
                            (
                             dx=1.0,
                             dy=0.0
                            )
                           )""";
        String result = instance.toString(pad);
        //System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of toStringFields method, of class V2D_Line_d.
     */
    @Test
    public void testToStringSimple() {
        System.out.println("toStringSimple");
        String pad = "";
        V2D_Line_d instance = V2D_Line_d.X_AXIS;
        String expResult = """
                           V2D_Line_d
                           (
                            offset=V2D_Vector_d(dx=0.0, dy=0.0),
                            p=V2D_Point_d(offset=V2D_Vector_d(dx=0.0, dy=0.0), rel=V2D_Vector_d(dx=0.0, dy=0.0)),
                            v= V2D_Vector_d(dx=1.0, dy=0.0)
                           )""";
        String result = instance.toStringSimple(pad);
        //System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of getP method, of class V2D_Line_d.
     */
    @Test
    public void testGetP() {
        System.out.println("getP");
        V2D_Line_d instance = new V2D_Line_d(pP0P0, pP1P0);
        V2D_Point_d expResult = pP0P0;
        V2D_Point_d result = instance.getP();
        assertTrue(expResult.equals(result));
    }

    /**
     * Test of rotate method, of class V2D_Line_d.
     */
    @Test
    public void testRotate() {
        System.out.println("rotate");
        double epsilon = 1d / 10000000d;
        double Pi = Math.PI;
        double theta = Pi / 2d;
        V2D_Line_d instance = new V2D_Line_d(pP0P0, pP1P0);
        V2D_Line_d expResult = new V2D_Line_d(pP0P0, pP0P1);
        V2D_Line_d result = instance.rotate(pP0P0, -theta);
        assertTrue(expResult.equals(result, epsilon));
        // Test 2
        expResult = new V2D_Line_d(pP0P0, pP0N1);
        result = instance.rotate(pP0P0, theta);
        assertTrue(expResult.equals(result, epsilon));
        // Test 3
        instance = new V2D_Line_d(pP0P0, pP0P1);
        expResult = new V2D_Line_d(pP0P0, pP1P0);
        result = instance.rotate(pP0P0, theta);
        assertTrue(expResult.equals(result, epsilon));
    }
    
    /**
     * Test of isOnSameSide.
     */
    @Test
    public void testIsOnSameSide() {
        System.out.println("isOnSameSide");
        double epsilon = 1d / 10000000d;
        V2D_Line_d l;
        // Test 1
        l = new V2D_Line_d(pP0P1, pP1P2);
        V2D_Point_d a = pN1P1;
        V2D_Point_d b = pP1P0;
        assertFalse(l.isOnSameSide(a, b, epsilon));        
        // Test 2
        a = pP1P1;
        assertTrue(l.isOnSameSide(a, b, epsilon));
        // Test 3
        a = pP1P0;
        assertTrue(l.isOnSameSide(a, b, epsilon));
    }

    /**
     * Test of isCollinear method, of class V2D_Line_d.
     */
    @Test
    public void testIsCollinear_double_V2D_Line_d_V2D_Point_dArr() {
        System.out.println("isCollinear");
        double epsilon = 1d / 10000000d;
        V2D_Line_d l;
        V2D_Point_d[] points = new V2D_Point_d[2];
        // Test 1
        l = new V2D_Line_d(pN1N1, pP1P1);
        points[0] = pP2P2;
        points[1] = pN2N2;
        assertTrue(V2D_Line_d.isCollinear(epsilon, l, points));
        // Test 2
        points[1] = pN2N1;
        assertFalse(V2D_Line_d.isCollinear(epsilon, l, points));
        // Test 3
        points[0] = pN1N2;
        assertFalse(V2D_Line_d.isCollinear(epsilon, l, points));
    }

    /**
     * Test of isCollinear method, of class V2D_Line_d.
     */
    @Test
    public void testIsCollinear_double_V2D_Point_dArr() {
        System.out.println("isCollinear");
        double epsilon = 1d / 10000000d;
        V2D_Point_d[] points = new V2D_Point_d[3];
        points[0] = pP0P0;
        points[1] = pP1P1;
        points[2] = pP2P2;
        assertTrue(V2D_Line_d.isCollinear(epsilon, points));
        points[2] = pN2N2;
        assertTrue(V2D_Line_d.isCollinear(epsilon, points));
        points[2] = pN1N1;
        assertTrue(V2D_Line_d.isCollinear(epsilon, points));
        points[2] = pN2N1;
        assertFalse(V2D_Line_d.isCollinear(epsilon, points));
        // P2*
        points[0] = pP2P2;
        points[1] = pP2P1;
        points[2] = pP2P0;
        assertTrue(V2D_Line_d.isCollinear(epsilon, points));
        points[2] = pP2N1;
        assertTrue(V2D_Line_d.isCollinear(epsilon, points));
        points[2] = pP2N2;
        assertTrue(V2D_Line_d.isCollinear(epsilon, points));
        points[2] = pN2N1;
        assertFalse(V2D_Line_d.isCollinear(epsilon, points));
    }

}
