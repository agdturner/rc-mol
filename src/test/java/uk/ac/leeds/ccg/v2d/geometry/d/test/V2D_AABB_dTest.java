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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_AABB_d;
import uk.ac.leeds.ccg.v2d.geometry.d.V2D_Vector_d;

/**
 * Test class for V2D_AABB_d.
 *
 * @author Andy Turner
 * @version 1.0
 */
public class V2D_AABB_dTest extends V2D_Test_d {

    public V2D_AABB_dTest() {
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
     * Test of toString method, of class V2D_AABB_d.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        V2D_AABB_d instance = new V2D_AABB_d(pP0P0);
        String expResult = "V2D_AABB_d(xMin=0.0, xMax=0.0, yMin=0.0,"
                + " yMax=0.0)";
        String result = instance.toString();
        //System.out.println(result);
        assertTrue(result.equalsIgnoreCase(expResult));
    }

    /**
     * Test of envelop method, of class V2D_AABB_d.
     */
    @Test
    public void testAABB() {
        System.out.println("envelope");
        V2D_AABB_d e1 = new V2D_AABB_d(pP0P0);
        V2D_AABB_d instance = new V2D_AABB_d(pP1P1);
        V2D_AABB_d expResult = new V2D_AABB_d(pP0P0, pP1P1);
        V2D_AABB_d result = instance.union(e1);
        assertTrue(result.equals(expResult));
    }

    /**
     * Test of intersects method, of class V2D_AABB_d.
     */
    @Test
    public void testIsIntersectedBy_V2D_AABB_d() {
        System.out.println("intersects");
        V2D_AABB_d instance = new V2D_AABB_d(pP0P0);
        V2D_AABB_d en = new V2D_AABB_d(pP0P0, pP1P1);
        assertTrue(instance.intersects(en));
        // Test 2
        instance = new V2D_AABB_d(pN1N1, pP0P0);
        assertTrue(instance.intersects(en));
        // Test 3
        en = new V2D_AABB_d(pN2N2, pP2P2);
        assertTrue(instance.intersects(en));
        // Test 4
        en = new V2D_AABB_d(pP0P0, pP1P1);
        instance = new V2D_AABB_d(pN1N1, pN1P0);
        assertFalse(instance.intersects(en));
        // Test 5
        en = new V2D_AABB_d(pP0P0, pP1P1);
        instance = new V2D_AABB_d(pN1P0, pP0P1);
        assertTrue(instance.intersects(en));
    }

    /**
     * Test of intersects method, of class V2D_AABB_d.
     */
    @Test
    public void testIsIntersectedBy_V2D_Point_d() {
        System.out.println("intersects");
        V2D_AABB_d instance = new V2D_AABB_d(pN1N1, pP1P1);
        // Test 1 the centre
        assertTrue(instance.intersects(pP0P0));
        // Test 2 to 6 the corners
        // Test 2
        assertTrue(instance.intersects(pP1P1));
        // Test 3
        assertTrue(instance.intersects(pN1N1));
        // Test 4
        assertTrue(instance.intersects(pN1P1));
        // Test 6
        assertTrue(instance.intersects(pP1N1));
    }

    /**
     * Test of equals method, of class V2D_AABB_d.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        V2D_AABB_d instance = new V2D_AABB_d(pP0P0, pP1P1);
        V2D_AABB_d en = new V2D_AABB_d(pP0P0, pP1P1);
        assertTrue(instance.equals(en));
        // Test 2
        en = new V2D_AABB_d(pP1P1, pP0P0);
        assertTrue(instance.equals(en));
        // Test 3
        en = new V2D_AABB_d(pP1N1, pP0P0);
        assertFalse(instance.equals(en));
    }

    /**
     * Test of intersects method, of class V2D_AABB_d. No need for a full
     * set of test here as this is covered by
     * {@link #testIsIntersectedBy_V2D_Point_d()}
     */
    @Test
    public void testIsIntersectedBy_2args() {
        System.out.println("intersects");
        V2D_AABB_d instance = new V2D_AABB_d(pN1N1, pP1P1);
        assertTrue(instance.intersects(0d, 0d));
        instance = new V2D_AABB_d(pP1P0);
        assertFalse(instance.intersects(0d, 0d));
    }

    /**
     * Test of getIntersection method, of class V2D_AABB_d.
     */
    @Test
    public void testGetIntersection_V2D_AABB_d() {
        System.out.println("getIntersection");
        V2D_AABB_d en;
        V2D_AABB_d instance;
        V2D_AABB_d expResult;
        V2D_AABB_d result;
        // Test 1
        en = new V2D_AABB_d(pN1N1, pP1P1);
        instance = new V2D_AABB_d(pP0P0, pP1P1);
        expResult = new V2D_AABB_d(pP0P0, pP1P1);
        result = instance.getIntersection(en);
        assertTrue(result.equals(expResult));
        // Test 2
        en = new V2D_AABB_d(pN1N1, pP0P0);
        instance = new V2D_AABB_d(pP0P0, pP1P1);
        expResult = new V2D_AABB_d(pP0P0);
        result = instance.getIntersection(en);
        assertTrue(result.equals(expResult));
        // Test 3
        en = new V2D_AABB_d(pN1N1, pP0P0);
        instance = new V2D_AABB_d(pP0P0, pP1P1);
        expResult = new V2D_AABB_d(pP0P0);
        result = instance.getIntersection(en);
        assertTrue(result.equals(expResult));
        // Test 4
        en = new V2D_AABB_d(pN1N1, pP0P1);
        instance = new V2D_AABB_d(pP0N1, pP1P1);
        expResult = new V2D_AABB_d(pP0N1, pP0P1);
        result = instance.getIntersection(en);
        assertTrue(result.equals(expResult));
    }

    /**
     * Test of union method, of class V2D_AABB_d.
     */
    @Test
    public void testUnion() {
        System.out.println("union");
        V2D_AABB_d en = new V2D_AABB_d(pN2N2, pP1P1);
        V2D_AABB_d instance = new V2D_AABB_d(pN1N1, pP2P2);
        V2D_AABB_d expResult = new V2D_AABB_d(pN2N2, pP2P2);
        V2D_AABB_d result = instance.union(en);
        assertTrue(result.equals(expResult));
    }

    /**
     * Test of contains method, of class V2D_AABB_d.
     */
    @Test
    public void testIsContainedBy() {
        System.out.println("isContainedBy");
        V2D_AABB_d en = new V2D_AABB_d(pN2N2, pP2P2);
        V2D_AABB_d instance = new V2D_AABB_d(pN1N1, pP1P1);
        assertTrue(instance.contains(en));
        // Test 2
        instance = new V2D_AABB_d(pN2N2, pP2P2);
        assertTrue(instance.contains(en));
        // Test 3
        en = new V2D_AABB_d(pN1N1, pP2P2);
        assertFalse(instance.contains(en));
    }

    /**
     * Test of getXMin method, of class V2D_AABB_d.
     */
    @Test
    public void testGetXMin() {
        System.out.println("getxMin");
        V2D_AABB_d instance = new V2D_AABB_d(pP0N1, pP0N1, pN2N2);
        double expResult = -2d;
        double result = instance.getXMin();
        assertTrue(expResult == result);
    }

    /**
     * Test of getXMax method, of class V2D_AABB_d.
     */
    @Test
    public void testGetXMax() {
        System.out.println("getxMax");
        V2D_AABB_d instance = new V2D_AABB_d(pP0N1, pP0P0);
        double expResult = 0d;
        double result = instance.getXMax();
        assertTrue(expResult == result);
    }

    /**
     * Test of getYMin method, of class V2D_AABB_d.
     */
    @Test
    public void testGetYMin() {
        System.out.println("getyMin");
        V2D_AABB_d instance = new V2D_AABB_d(pP0N1, pP0P0, pN2N2);
        double expResult = -2d;
        double result = instance.getYMin();
        assertTrue(expResult == result);
    }

    /**
     * Test of getYMax method, of class V2D_AABB_d.
     */
    @Test
    public void testGetYMax() {
        System.out.println("getyMax");
        V2D_AABB_d instance = new V2D_AABB_d(pP0N1, pP0N1, pN2N2);
        double expResult = -1d;
        double result = instance.getYMax();
        assertTrue(expResult == result);
    }

    /**
     * Test of translate method, of class V2D_AABB_d.
     */
    @Test
    public void testTranslate() {
        System.out.println("translate");
        V2D_Vector_d v = P1P1;
        V2D_AABB_d instance = new V2D_AABB_d(pP0P0, pP1P1);
        V2D_AABB_d expResult = new V2D_AABB_d(pP1P1, pP2P2);
        instance.translate(v);
        assertTrue(expResult.equals(instance));
        // Test 2
        v = N1N1;
        instance = new V2D_AABB_d(pP0P0, pP1P1);
        expResult = new V2D_AABB_d(pN1N1, pP0P0);
        instance.translate(v);
        assertTrue(expResult.equals(instance));
    }
}
