/*
 * Copyright 2025 Andy Turner, University of Leeds.
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
package uk.ac.leeds.ccg.v2d.geometry.d;

import uk.ac.leeds.ccg.math.geometry.Math_AngleDouble;
import uk.ac.leeds.ccg.math.matrices.Math_Matrix_Double;
import uk.ac.leeds.ccg.v2d.core.d.V2D_Environment_d;

/**
 * 2D representation of an infinite length line. The line passes through the
 * point {@link #pv} with vector {@link #v}.
 *
 * @author Andy Turner
 * @version 2.0
 */
public class V2D_Line_d extends V2D_Geometry_d {

    private static final long serialVersionUID = 1L;

    /**
     * The x axis.
     */
    public static final V2D_Line_d X_AXIS = new V2D_Line_d(null,
            V2D_Vector_d.ZERO, V2D_Vector_d.I);

    /**
     * The y axis.
     */
    public static final V2D_Line_d Y_AXIS = new V2D_Line_d(null,
            V2D_Vector_d.ZERO, V2D_Vector_d.J);

    /**
     * If this line is defined by a vector, then the calculation of {@link #q}
     * may be imprecise. If this line is defined by points, then {@link #v} may
     * have been imprecisely calculated.
     */
    public boolean isDefinedByVector;

    /**
     * Used to define {@link #p}.
     */
    protected V2D_Vector_d pv;

    /**
     * Used to store a point on the line as derived from {@link #offset} and
     * {@link #pv}.
     */
    protected V2D_Point_d p;

    /**
     * Another point on the line that is derived from {@link #offset},
     * {@link #pv} and {@link v}.
     */
    protected V2D_Point_d q;

    /**
     * The vector that defines the line. This will not change under translation,
     * but will change under rotation.
     */
    public V2D_Vector_d v;

    /**
     * @param l Used to initialise this.
     */
    public V2D_Line_d(V2D_Line_d l) {
        super(l.env, l.offset);
        this.pv = new V2D_Vector_d(l.pv);
        if (l.p != null) {
            this.p = new V2D_Point_d(l.p);
        }
        if (l.q != null) {
            this.q = new V2D_Point_d(l.q);
        }
        this.v = new V2D_Vector_d(l.v);
        this.isDefinedByVector = l.isDefinedByVector;
    }

    /**
     * @param l Used to initialise this.
     */
    public V2D_Line_d(V2D_LineSegment_d l) {
        this(l.l);
    }

    /**
     * {@code pv} should not be equal to {@code qv}. {@link #offset} is set to
     * {@link V2D_Vector_d#ZERO}.
     *
     * @param env The environment.
     * @param p What {@link #pv} is set to.
     * @param q Another point on the line from which {@link #v} is derived.
     */
    public V2D_Line_d(V2D_Environment_d env, V2D_Vector_d p, 
            V2D_Vector_d q) {
        this(env, V2D_Vector_d.ZERO, p, q);
    }

    /**
     * {@code pv} should not be equal to {@code qv}.
     *
     * @param env The environment.
     * @param offset What {@link #offset} is set to.
     * @param pv What {@link #pv} is cloned from.
     * @param qv Used to calculate {@link q} and {@link #v} (which is calculated
     * by taking the difference between pv and qv.
     */
    public V2D_Line_d(V2D_Environment_d env, V2D_Vector_d offset,
            V2D_Vector_d pv, V2D_Vector_d qv) {
        super(env, offset);
        this.pv = new V2D_Vector_d(pv);
        if (pv.equals(qv)) {
            throw new RuntimeException("" + pv + " and " + qv + " are the same"
                    + " so do not define a line.");
        }
        //q = new V2D_Point_d(offset, qv);
        v = qv.subtract(pv);
        isDefinedByVector = false;
    }

    /**
     * {@code v} should not be the zero vector {@code <0,0,0>}. {@link #offset}
     * is set to {@link V2D_Vector_d#ZERO}.
     *
     * @param env The environment.
     * @param p What {@link #pv} is cloned from.
     * @param v The vector defining the line from {@link #pv}. What {@link #v}
     * is cloned from.
     * @param flag To distinguish this method from
     * {@link #V2D_Line_d(uk.ac.leeds.ccg.v3d.geometry.d.V2D_Vector_d, uk.ac.leeds.ccg.v3d.geometry.d.V2D_Vector_d)}
     */
    public V2D_Line_d(V2D_Environment_d env, V2D_Vector_d p, 
            V2D_Vector_d v, boolean flag) {
        this(env, V2D_Vector_d.ZERO, p, v, flag);
    }

    /**
     * Checks to ensure v is not the zero vector {@code <0,0,0>}.
     *
     * @param p What {@link #pv} is set to.
     * @param v The vector defining the line from {@link #pv}.
     */
    public V2D_Line_d(V2D_Point_d p, V2D_Vector_d v) {
        this(p.env, p.offset, p.rel, v, true);
    }

    /**
     * Checks to ensure v is not the zero vector {@code <0,0,0>}.
     *
     * @param env The environment.
     * @param offset What {@link #offset} is set to.
     * @param p What {@link #pv} is set to.
     * @param v The vector defining the line from {@link #pv}.
     * @param flag To distinguish this from
     * {@link #V2D_Line_d(uk.ac.leeds.ccg.v3d.geometry.d.V2D_Vector_d, uk.ac.leeds.ccg.v3d.geometry.d.V2D_Vector_d, uk.ac.leeds.ccg.v3d.geometry.d.V2D_Vector_d)}
     * @throws RuntimeException if {@code v.isZero()}.
     */
    public V2D_Line_d(V2D_Environment_d env, V2D_Vector_d offset, 
            V2D_Vector_d p, V2D_Vector_d v, boolean flag) {
        super(env, offset);
        if (v.isZero()) {
            throw new RuntimeException("Vector " + v + " is the zero vector "
                    + "which cannot be used to define a line.");
        }
        pv = new V2D_Vector_d(p);
        this.v = new V2D_Vector_d(v);
        isDefinedByVector = true;
    }

    /**
     * Create a new instance.
     *
     * @param p What {@link #pv} is cloned from.
     * @param q What {@link #v} is derived from.
     */
    public V2D_Line_d(V2D_Point_d p, V2D_Point_d q) {
        super(p.env, new V2D_Vector_d(p.offset));
        V2D_Point_d q2 = new V2D_Point_d(q);
        q2.setOffset(p.offset);
        if (p.rel.equals(q2.rel)) {
            throw new RuntimeException("Points " + p + " and " + q
                    + " are the same and so do not define a line.");
        }
        pv = new V2D_Vector_d(p.rel);
        v = q2.rel.subtract(pv);
    }

    @Override
    public String toString() {
        //return toString("");
        return toStringSimple("");
    }

    /**
     * @param pad A padding of spaces.
     * @return A description of this.
     */
    public String toString(String pad) {
        return this.getClass().getSimpleName() + "\n"
                + pad + "(\n"
                + toStringFields(pad + " ") + "\n"
                + pad + ")";
    }

    /**
     * @param pad A padding of spaces.
     * @return A description of this.
     */
    public String toStringSimple(String pad) {
        return this.getClass().getSimpleName() + "\n"
                + pad + "(\n"
                + toStringFieldsSimple(pad + " ") + "\n"
                + pad + ")";
    }

    /**
     * @param pad A padding of spaces.
     * @return A description of the fields.
     */
    @Override
    protected String toStringFields(String pad) {
        String r = super.toStringFields(pad) + "\n"
                + pad + ",\n";
        r += pad + "p=" + getP().toString(pad) + "\n"
                + pad + ",\n"
                + pad + "v=" + v.toString(pad);
        return r;
    }

    /**
     * @param pad A padding of spaces.
     * @return A description of the fields.
     */
    @Override
    protected String toStringFieldsSimple(String pad) {
        String r = super.toStringFieldsSimple(pad) + ",\n";
        r += pad + "p=" + getP().toStringSimple("") + ",\n"
                + pad + "v=" + v.toStringSimple(pad);
        return r;
    }

    /**
     * @param l The line to test if it is the same as {@code this}.
     * @return {@code true} iff {@code l} is the same as {@code this}.
     */
    public boolean equals(V2D_Line_d l) {
//        if (v.isScalarMultiple(l.v)) {
//            if (intersects(l.getP())) {
//                if (intersects(l.getQ())) {
                    if (l.intersects(getP())) {
                        if (l.intersects(getQ())) {
                            return true;
                        }
                    }
//                }
//            }
//        }
        return false;
    }

    /**
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param l The line to test if it is the same as {@code this}.
     * @return {@code true} iff {@code l} is the same as {@code this} within
     * epsilon.
     */
    public boolean equals(V2D_Line_d l, double epsilon) {
        //if (v.isScalarMultiple(epsilon, l.v)) {
        if (l.intersects(epsilon, getP())) {
            if (l.intersects(epsilon, getQ())) {
                return true;
            }
        }
        //}
        return false;
    }

    /**
     * The point of the line as calculated from {@link #pv} and {@link #offset}.
     *
     * @return {@link #pv} with {@link #offset} applied.
     */
    public V2D_Point_d getP() {
        if (p == null) {
            p = new V2D_Point_d(env, offset, pv);
        }
        return p;
    }

    /**
     * A point on the line as calculated from {@link #pv}, {@link #offset} and
     * {@link #v}.
     *
     * @return Another point on the line derived from {@link #v}.
     */
    public V2D_Point_d getQ() {
        if (q == null) {
            q = new V2D_Point_d(env, offset, pv.add(v));
        }
        return q;
    }

    /**
     * @param pt A point to test for intersection.
     * @return {@code true} if pv is on the line.
     */
    public boolean intersects(V2D_Point_d pt) {
        p = getP();
        q = getQ();
        if (p.equals(pt)
                || q.equals(pt)) {
            return true;
        }
        V2D_Vector_d dpt = new V2D_Vector_d(
                    pt.getX() - p.getX(),
                    pt.getY() - p.getY());
        return dpt.isScalarMultiple(v);
    }

    /**
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param pt A point to test for intersection.
     * @return {@code true} if pv is on the line.
     */
    public boolean intersects(double epsilon, V2D_Point_d pt) {
        p = getP();
        q = getQ();
        if (p.equals(pt, epsilon) || q.equals(pt, epsilon)) {
            return true;
        }
        V2D_Vector_d dpt = new V2D_Vector_d(
                    pt.getX() - p.getX(),
                    pt.getY() - p.getY());
        return dpt.isScalarMultiple(epsilon, v);
    }
    
    /**
     * @param x1 p.getX()
     * @param x2 q.getX()
     * @param x3 l.p.getX()
     * @param x4 l.q.getX()
     * @param y1 p.getY()
     * @param y2 q.getY()
     * @param y3 l.p.getY()
     * @param y4 l.q.getY()
     * @return ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4))
     */
    public static double getIntersectDenominator(double x1,  double x2,
            double x3, double x4, double y1, double y2, double y3, double y4) {
        return ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
    }
    
//    /**
//     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
//     * @param l A line to test for intersection.
//     * @return {@code true} if lines intersect.
//     */
//    public boolean intersects(V2D_Line_d l) {
//        return intersects(l, 
//                getIntersectDenominator(p.getX(), q.getX(), l.p.getX(),
//                l.q.getX(), p.getY(), q.getY(), l.p.getY(), l.q.getY()));
//    }
//    
//    /**
//     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
//     * @param l A line to test for intersection.
//     * @param den getIntersectDenominator(p.getX(), q.getX(), l.p.getX(),
//     *           l.q.getX(), p.getY(), q.getY(), l.p.getY(), l.q.getY())
//     * @return {@code true} if lines intersect.
//     */
//    public boolean intersects(V2D_Line_d l, double den) {
//        if (den == 0.0D) {
//            // Lines are parallel or coincident.
//            return equals(l);
//        }
//        return true;
//    }
//    
//    /**
//     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
//     * @param l A line to test for intersection.
//     * @param x1 p.getX()
//     * @param x2 q.getX()
//     * @param x3 l.p.getX()
//     * @param x4 l.q.getX()
//     * @param y1 p.getY()
//     * @param y2 q.getY()
//     * @param y3 l.p.getY()
//     * @param y4 l.q.getY()
//     * @return {@code true} if lines intersect.
//     */
//    public boolean intersects(V2D_Line_d l, double x1,  double x2,
//            double x3, double x4, double y1, double y2, double y3, double y4) {
//        return intersects(l,
//                getIntersectDenominator(x1, x2, x3, x4, y1, y2, y3, y4));
//    }
    
    /**
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * @param epsilon The tolerance within which two lines are considered equal.
     * @param l A line to test for intersection.
     * @return {@code true} if lines intersect.
     */
    public boolean intersects(double epsilon, V2D_Line_d l) {
        return intersects(epsilon, l, 
                getIntersectDenominator(p.getX(), getQ().getX(), l.p.getX(),
                l.getQ().getX(), p.getY(), q.getY(), l.p.getY(), l.q.getY()));
    }
    
    /**
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * @param epsilon The tolerance within which two lines are considered equal.
     * @param l A line to test for intersection.
     * @param x1 getP().getX()
     * @param x2 getQ().getX()
     * @param x3 l.getP().getX()
     * @param x4 l.getQ().getX()
     * @param y1 p.getY()
     * @param y2 q.getY()
     * @param y3 l.p.getY()
     * @param y4 l.q.getY()
     * @return {@code true} if lines intersect.
     */
    public boolean intersects(double epsilon, V2D_Line_d l, double x1,
            double x2, double x3, double x4, double y1, double y2, double y3, 
            double y4) {
        return intersects(epsilon, l, 
                getIntersectDenominator(x1, x2, x3, x4, y1, y2, y3, y4));
    }
    
    /**
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * @param epsilon The tolerance within which two lines are considered equal.
     * @param l A line to test for intersection.
     * @param den getIntersectDenominator(p.getX(), q.getX(), l.p.getX(),
           l.q.getX(), p.getY(), q.getY(), l.p.getY(), l.q.getY())
     * @return {@code true} if lines intersect.
     */
    public boolean intersects(double epsilon, V2D_Line_d l, double den) {
        if (den == 0d) {
            // Lines are parallel or coincident.
            return equals(l, epsilon);
        }
        return true;
    }
    
    /**
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param l Line to intersect with.
     * @return The geometry or null if there is no intersection.
     */
    public V2D_Geometry_d getIntersect(double epsilon, V2D_Line_d l) {
        double x1 = getP().getX();
        double x2 = getQ().getX();
        double x3 = l.getP().getX();
        double x4 = l.getQ().getX();
        double y1 = p.getY();
        double y2 = q.getY();
        double y3 = l.p.getY();
        double y4 = l.q.getY();
        double den = getIntersectDenominator(x1, x2, x3, x4, y1, y2, y3, y4);
        return getIntersect(epsilon, l, den, x1, x2, x3, x4, y1, y2, y3, y4);
    }
    
    /**
     * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
     * @param epsilon The tolerance within which two lines are considered equal.
     * @param l Line to intersect with.
     * @param den getIntersectDenominator(x1, x2, x3, x4, y1, y2, y3, y4)
     * @param x1 getP().getX()
     * @param x2 getQ().getX()
     * @param x3 l.getP().getX()
     * @param x4 l.getQ().getX()
     * @param y1 p.getY()
     * @param y2 q.getY()
     * @param y3 l.p.getY()
     * @param y4 l.q.getY()
     * @return The geometry or null if there is no intersection.
     */
    public V2D_Geometry_d getIntersect(double epsilon, V2D_Line_d l, 
            double den, double x1, double x2, double x3, double x4, double y1, 
            double y2, double y3, double y4) {
        if (intersects(epsilon, l, den)) {
            // Check for coincident lines
            if (equals(l, epsilon / 10d)) {
                return l;
            }
            double x1y2sy1x2 = (x1 * y2 - y1 * x2);
            double x3y4sy3x4 = (x3 * y4 - y3 * x4);
            double numx = (x1y2sy1x2 * (x3 - x4)) - ((x1 - x2) * x3y4sy3x4);
            double numy = (x1y2sy1x2 * (y3 - y4)) - ((y1 - y2) * x3y4sy3x4);
            return new V2D_Point_d(env, numx / den, numy / den);            
        }
        return null;
    }
    
    /**
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param l The line to test if it is parallel to this.
     * @return {@code true} If this and {@code l} are parallel.
     */
    public boolean isParallel(V2D_Line_d l, double epsilon) {
        return v.isScalarMultiple(epsilon, l.v);
    }
    
    /**
     * @param pt A point for which the shortest line segment to this is
     * returned.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return The line segment having the shortest distance between {@code pt}
     * and {@code this}.
     */
    public V2D_FiniteGeometry_d getLineOfIntersect(V2D_Point_d pt,
            double epsilon) {
        if (intersects(epsilon, pt)) {
            return pt;
        }
        return new V2D_LineSegment_d(pt, getPointOfIntersect(pt, epsilon));
    }

    /**
     * @param pt The point projected onto this.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return A point on {@code this} which is the shortest distance from
     * {@code pt}.
     */
    public V2D_Point_d getPointOfIntersect(V2D_Point_d pt, double epsilon) {
        if (intersects(epsilon, pt)) {
            return pt;
        }
        return V2D_Line_d.this.getPointOfIntersect(pt, true, epsilon);
    }
    
    /**
     * @param pt The point projected onto this.
     * @param noint True iff there is no intersection.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return A point on {@code this} which is the shortest distance from
     * {@code pt}.
     */
    public V2D_Point_d getPointOfIntersect(V2D_Point_d pt, boolean noint, double epsilon) {
        V2D_Line_d l = new V2D_Line_d(pt, v.rotate90());
        return (V2D_Point_d) V2D_Line_d.this.getIntersect(epsilon, l);
    }
    
    /**
     * @param pt A point for which the minimum distance from {@code this} is
     * returned.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return The minimum distance between this and {@code pv}.
     */
    public double getDistance(V2D_Point_d pt, double epsilon) {
        return Math.sqrt(getDistanceSquared(pt, epsilon));
    }
    
    /**
     * @param pt A point for which the minimum distance from {@code this} is
     * returned.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return The minimum distance between this and {@code pv}.
     */
    public double getDistanceSquared(V2D_Point_d pt, double epsilon) {
        if (intersects(epsilon, pt)) {
            return 0d;
        }
        return getDistanceSquared(pt, true, epsilon);
    }
    
    /**
     * @param pt A point for which the minimum distance from {@code this} is
     * returned.
     * @param noint True iff pt is not intersecting.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return The minimum distance between this and {@code pv}.
     */
    public double getDistanceSquared(V2D_Point_d pt, boolean noint, double epsilon) {
        V2D_Point_d poi = V2D_Line_d.this.getPointOfIntersect(pt, noint, epsilon);
        return poi.getDistanceSquared(pt);
    }

    /**
     * @param l A line for which the minimum distance from {@code this} is
     * returned.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return The minimum distance between this and {@code l}.
     */
    public double getDistance(V2D_Line_d l, double epsilon) {
        return Math.sqrt(getDistanceSquared(l, epsilon));
    }

    /**
     * @param l A line for which the minimum distance from {@code this} is
     * returned.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return The minimum distance between this and {@code l}.
     */
    public double getDistanceSquared(V2D_Line_d l, double epsilon) {
        if (this.equals(l, epsilon)) {
            return 0D;
        }
        if (this.isParallel(l, epsilon)) {
            return this.getDistanceSquared(l.p, epsilon);
        }
        return 0D;
    }

    /**
     * @return {@code true} iff this is parallel to the plane defined by x=0.
     */
    public boolean isParallelToX0() {
        return v.dx == 0d;
    }

    /**
     * @return {@code true} iff this is parallel to the plane defined by y=0.
     */
    public boolean isParallelToY0() {
        return v.dy == 0d;
    }

    /**
     * @return The points that define the plan as a matrix.
     */
    public Math_Matrix_Double getAsMatrix() {
        V2D_Point_d tp = getP();
        V2D_Point_d tq = getQ();
        double[][] m = new double[2][2];
        m[0][0] = tp.getX();
        m[0][1] = tp.getY();
        m[1][0] = tq.getX();
        m[1][1] = tq.getY();
        return new Math_Matrix_Double(m);
    }

    /**
     * Translate (move relative to the origin).
     *
     * @param v The vector to translate.
     */
    @Override
    public void translate(V2D_Vector_d v) {
        super.translate(v);
        //pv.translate(v);
        if (p != null) {
            p.translate(v);
        }
        if (q != null) {
            q.translate(v);
        }
    }

    @Override
    public V2D_Line_d rotate(V2D_Point_d pt, double theta) {
        theta = Math_AngleDouble.normalise(theta);
        if (theta == 0d) {
            return new V2D_Line_d(this);
        } else {
            return rotateN(pt, theta);
        }
    }
    
    @Override
    public V2D_Line_d rotateN(V2D_Point_d pt, double theta) {
        V2D_Point_d rp = getP().rotateN(pt, theta);
        V2D_Vector_d rv = v.getUnitVector().rotateN(theta);
        return new V2D_Line_d(rp, rv);
    }
    
    /**
     * @param ps The points to test for collinearity.
     * @return {@code true} iff all points are collinear with l.
     */
    public static boolean isCollinear(V2D_Point_d... ps) {
        V2D_Line_d l = getLine(ps);
        if (l == null) {
            return false;
        }
        return isCollinear(l, ps);
    }

    /**
     * There should be at least two different points in points. This does not
     * check ps are collinear.
     *
     * @param ps Any number of points, but with two being different.
     * @return A line defined by any two different points or null if the points
     * are coincident.
     */
    public static V2D_Line_d getLine(V2D_Point_d... ps) {
        var p0 = ps[0];
        for (var p : ps) {
            if (!p.equals(p0)) {
                return new V2D_Line_d(p0, p);
            }
        }
        return null;
    }

    /**
     * @param l The line to test points are collinear with.
     * @param ps The points to test if they are collinear with l.
     * @return {@code true} iff all points are collinear with l.
     */
    public static boolean isCollinear(V2D_Line_d l, 
            V2D_Point_d... ps) {        
        for (var p : ps) {
            if (!l.intersects(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param ps The points to test for collinear within epsilon tolerance.
     * @return {@code true} iff all points are collinear with l given epsilon 
     * tolerance.
     */
    public static boolean isCollinear(double epsilon, V2D_Point_d... ps) {
        V2D_Line_d l = getLine(epsilon, ps);
        if (l == null) {
            return false;
        }
        return isCollinear(epsilon, l, ps);
    }

    /**
     * @param l The line to test points are collinear with.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param ps The points to test if they are collinear with l.
     * @return {@code true} iff all points are collinear with l.
     */
    public static boolean isCollinear(double epsilon, V2D_Line_d l, 
            V2D_Point_d... ps) {
        for (var p : ps) {
            if (!l.intersects(epsilon, p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * There should be at least two different points in points. This does not
     * check for collinearity of all the points. It returns a line defined by 
     * the first points that have the greatest distance between them.
     * 
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param points Any number of points, but with two being different.
     * @return A line defined by any two different points or null if the points
     * are coincident.
     */
    public static V2D_Line_d getLine(double epsilon, V2D_Point_d... points) {
        if (points.length < 2) {
            return null;
        }
        // Find the points which are furthest apart.
        double max = 0d;
        V2D_Point_d a = null;
        V2D_Point_d b = null;
        for (int i = 0; i < points.length; i ++) {
            for (int j = i + 1; j < points.length; j ++) {
                double d2 = points[i].getDistanceSquared(points[j]);
                if (d2 > max) {
                    a = points[i];
                    b = points[j];
                    max = d2;
                }
            }
        }
        if (max == 0d) {
            return null;
        } else {
            return new V2D_Line_d(a, b);
        }
    }
    
    /**
     * https://math.stackexchange.com/questions/162728/how-to-determine-if-2-points-are-on-opposite-sides-of-a-line
     * @param a A point.
     * @param b Another point.
     * @param epsilon.
     * @return True iff a and b are on the same side of this. (If a is on the 
     * line, then so must b for them to be on the same side.
     */
    public boolean isOnSameSide(V2D_Point_d a, V2D_Point_d b, double epsilon) {
        if (intersects(epsilon, a) && intersects(epsilon, b)) {
            return true;
        }
        p = getP();
        q = getQ();
        double x1 = p.getX();
        double y1 = p.getY();
        double x2 = q.getX();
        double y2 = q.getY();
        double ax = a.getX();
        double ay = a.getY();
        double bx = b.getX();
        double by = b.getY();
        double y1sy2 = y1 - y2; 
        double x2sx1 = x2 - x1;
        //double p1 = ((y1sy2)*(ax-x1)+(x2sx1)*(ay-y1));
        //double p2 = ((y1sy2)*(bx-x1)+(x2sx1)*(by-y1));
        return ((y1sy2)*(ax-x1)+(x2sx1)*(ay-y1))*((y1sy2)*(bx-x1)+(x2sx1)*(by-y1)) + epsilon >= 0D;
    }
}
