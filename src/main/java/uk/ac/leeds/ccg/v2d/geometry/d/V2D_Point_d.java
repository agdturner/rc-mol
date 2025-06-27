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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import uk.ac.leeds.ccg.math.arithmetic.Math_Double;
import uk.ac.leeds.ccg.math.geometry.Math_AngleDouble;
import uk.ac.leeds.ccg.v2d.core.d.V2D_Environment_d;

/**
 * A point is defined by two vectors: {@link #offset} and {@link #rel}. Adding
 * these gives the position of a point. Two points are equal according to
 * {@link #equals(uk.ac.leeds.ccg.v3d.geometry.d.V2D_Point)} if they have the
 * same position.
 *
 * @author Andy Turner
 * @version 2.0
 */
public class V2D_Point_d extends V2D_FiniteGeometry_d implements Comparable<V2D_Point_d> {

    private static final long serialVersionUID = 1L;

    /**
     * The origin of the Euclidean space.
     */
    public static final V2D_Point_d ORIGIN = new V2D_Point_d(null, 0, 0);

    /**
     * The position relative to the {@link #offset}. Taken together with
     * {@link #offset}, this gives the point location.
     */
    public V2D_Vector_d rel;

    /**
     * Create a new instance which is completely independent of {@code pv}.
     *
     * @param p The point to clone/duplicate.
     */
    public V2D_Point_d(V2D_Point_d p) {
        super(p.env, new V2D_Vector_d(p.offset));
        rel = new V2D_Vector_d(p.rel);
    }

    /**
     * Create a new instance with {@link #offset} set to
     * {@link V2D_Vector_d#ZERO}.
     *
     * @param env The environment.
     * @param rel Cloned to initialise {@link #rel}.
     */
    public V2D_Point_d(V2D_Environment_d env, V2D_Vector_d rel) {
        this(env, V2D_Vector_d.ZERO, rel);
    }

    /**
     * Create a new instance.
     *
     * @param env The environment.
     * @param offset What {@link #offset} is set to.
     * @param rel Cloned to initialise {@link #rel}.
     */
    public V2D_Point_d(V2D_Environment_d env, V2D_Vector_d offset,
            V2D_Vector_d rel) {
        super(env, offset);
        this.rel = new V2D_Vector_d(rel);
    }

    /**
     * Create a new instance with {@link #offset} set to
     * {@link V2D_Vector_d#ZERO}.
     *
     * @param env The environment.
     * @param x What {@link #rel} x component is set to.
     * @param y What {@link #rel} y component is set to.
     */
    public V2D_Point_d(V2D_Environment_d env, double x, double y) {
        super(env, V2D_Vector_d.ZERO);
        rel = new V2D_Vector_d(x, y);
    }

    @Override
    public String toString() {
        return toStringSimple("");
    }

    /**
     * @param pad A padding of spaces.
     * @return A description of this.
     */
    public String toStringSimple(String pad) {
        return this.getClass().getSimpleName() + "("
                + toStringFieldsSimple("") + ")";
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
     * @return A description of the fields.
     */
    @Override
    protected String toStringFields(String pad) {
        return super.toStringFields(pad) + "\n"
                + pad + ",\n"
                + pad + "rel=" + rel.toString(pad);
    }

    /**
     * @param pad A padding of spaces.
     * @return A description of the fields.
     */
    @Override
    protected String toStringFieldsSimple(String pad) {
        return pad + super.toStringFieldsSimple("") + ", rel=" + rel.toStringSimple("");
    }

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof V2D_Point_d p) {
                return equals(p);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.rel);
        return hash;
    }
    
    /**
     * Two points are equal if they are at the same location defined by each
     * points relative start location and translation vector.
     *
     * @param p The point to test if it is the same as {@code this}.
     * @return {@code true} iff {@code pv} is the same as {@code this}.
     */
    public boolean equals(V2D_Point_d p) {
        if (p == null) {
            return false;
        }
        if (getX() == p.getX()) {
            if (getY() == p.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Two points are equal if they are at the same location defined by each
     * points relative start location and translation vector.
     *
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param p The point to test if it is the same as {@code this}.
     * @return {@code true} iff {@code pv} is equal to {@code this} given the
     * epsilon.
     */
    public boolean equals(V2D_Point_d p, double epsilon) {
        if (p == null) {
            return false;
        }
        double x = this.getX();
        double y = this.getY();
        double px = p.getX();
        double py = p.getY();
        return Math_Double.equals(x, px, epsilon)
                && Math_Double.equals(y, py, epsilon);
    }
    
    /**
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param ps The points to test if they are coincident.
     * @return {@code true} iff all the points are coincident.
     */
    public static boolean equals(double epsilon, V2D_Point_d... ps) {
        if (ps.length < 2) {
            return true;
        }
        for (int i = 1; i < ps.length; i++) {
            if (!ps[0].equals(ps[i], epsilon)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * For determining if all points in {@code ps} are coincident to 
     * {@code this} within a tolerance given by {@code epsilon}.
     *
     * @param ps The points to test if they are equal to this.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return {@code true} iff all points in {@code ps} are equal to 
     * {@code this}.
     */
    public boolean equalsAll(Collection<V2D_Point_d> ps, double epsilon) {
        return ps.parallelStream().allMatch(x -> equals(x, epsilon));
    }

    /**
     * For determining if all points in {@code ps} are coincident to 
     * {@code this} within a tolerance given by {@code epsilon}.
     *
     * @param ps The points to test if they are equal to this.
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @return {@code true} iff all points in {@code ps} are equal to 
     * {@code this}.
     */
    public boolean equalsAny(Collection<V2D_Point_d> ps, double epsilon) {
        return ps.parallelStream().anyMatch(x -> equals(x, epsilon));
    }

    /**
     * @return The vector - {@code v.add(offset, oom)}.
     */
    public V2D_Vector_d getVector() {
        return rel.add(offset);
    }

    /**
     * @return The x component of {@link #rel} with {@link #offset} applied.
     */
    public double getX() {
        return rel.dx + offset.dx;
    }

    /**
     * @return The y component of {@link #rel} with {@link #offset} applied.
     */
    public double getY() {
        return rel.dy + offset.dy;
    }

    /**
     * @return true iff this is equal to the ORIGIN.
     */
    public boolean isOrigin() {
        return equals(ORIGIN);
    }
    
    @Override
    public V2D_Point_d[] getPointsArray() {
        V2D_Point_d[] r = new V2D_Point_d[1];
        r[0] = this;
        return r;
    }

    /**
     * Returns true if this is between a and b. If this equals a or b then
     * return true. Being between does not necessarily mean being collinear.
     *
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param a A point
     * @param b A point
     * @return true iff this is equal to the ORIGIN.
     */
    public boolean isBetween(double epsilon, V2D_Point_d a, V2D_Point_d b) {
        if (this.equals(a)) {
            return true;
        }
        if (this.equals(b)) {
            return true;
        }
        if (a.equals(b)) {
            return false;
        }
        V2D_Vector_d ab = new V2D_Vector_d(a, b);
        V2D_Vector_d v90 = ab.rotate90();
        V2D_Line_d ap = new V2D_Line_d(a, v90);
        if (ap.isOnSameSide(this, b, epsilon)) {
            V2D_Line_d bp = new V2D_Line_d(b, v90);
            return bp.isOnSameSide(this, a, epsilon);
        }
        return false;
    }

    /**
     * Get the distance between this and {@code pv}.
     *
     * @param p A point.
     * @return The distance from {@code pv} to this.
     */
    public double getDistance(V2D_Point_d p) {
        if (this.equals(p)) {
            return 0d;
        }
        return Math.sqrt(getDistanceSquared(p));
    }

    /**
     * Get the distance squared between this and {@code pt}.
     *
     * @param pt A point.
     * @return The distance squared from {@code pv} to this.
     */
    public double getDistanceSquared(V2D_Point_d pt) {
        double dx = getX() - pt.getX();
        double dy = getY() - pt.getY();
        return dx * dx + dy * dy;
    }

    @Override
    public V2D_AABB_d getAABB() {
        return new V2D_AABB_d(this);
    }

    /**
     * @return The location of the point:
     * <Table>
     * <caption>Locations</caption>
     * <thead>
     * <tr><td>Code</td><td>Description</td></tr>
     * </thead>
     * <tbody>
     * <tr><td>1</td><td>Px, Py</td></tr>
     * <tr><td>2</td><td>Px, Ny</td></tr>
     * <tr><td>3</td><td>Nx, Py</td></tr>
     * <tr><td>4</td><td>Nx, Ny</td></tr>
     * </tbody>
     * </Table>
     */
    public int getLocation() {
        if (getX() >= 0d) {
            if (getY() >= 0d) {
                if (isOrigin()) {
                    return 0;
                }
                return 1;
            } else {
                return 2;
            }
        } else {
            if (getY() >= 0d) {
                return 3;
            } else {
                return 4;
            }
        }
    }

    /**
     * Change {@link #offset} without changing the overall point vector by also
     * adjusting {@link #rel}.
     *
     * @param offset What {@link #offset} is set to.
     */
    public void setOffset(V2D_Vector_d offset) {
        if (!offset.equals(this.offset)) {
            rel = getVector().subtract(offset);
            this.offset = offset;
        }
    }

    /**
     * Change {@link #rel} without changing the overall point vector by also
     * adjusting {@link #offset}.
     *
     * @param rel What {@link #rel} is set to.
     */
    public void setRel(V2D_Vector_d rel) {
        //offset = getVector(e.oom).subtract(v, e.oom);
        offset = offset.subtract(rel).add(this.rel);
        this.rel = rel;
    }

    @Override
    public V2D_Point_d rotate(V2D_Point_d pt, double theta) {
        theta = Math_AngleDouble.normalise(theta);
        if (theta == 0d) {
            return new V2D_Point_d(this);
        } else {
            return rotateN(pt, theta);
        }
    }

    @Override
    public V2D_Point_d rotateN(V2D_Point_d pt, double theta) {
        V2D_Vector_d tv = new V2D_Vector_d(pt.getX(), pt.getY());
        V2D_Point_d tp = new V2D_Point_d(this);
        tp.translate(tv.reverse());
        V2D_Vector_d tpv = tp.getVector();
        V2D_Point_d r = new V2D_Point_d(env, tpv.rotateN(theta));
        r.translate(tv);
        return r;
    }

    /**
     * A collection method for getting unique points.
     *
     * @param epsilon The tolerance within which vector components are
     * considered equal.
     * @param pts The points to derive a unique list from.
     * @return A unique list made from those in pts.
     */
    public static ArrayList<V2D_Point_d> getUnique(
            List<V2D_Point_d> pts, double epsilon) {
//        System.out.println("Before unique");
//        for (int i = 0; i < pts.size(); i++) {
//            System.out.println("i=" + i);
//            System.out.println(pts.get(i).toStringSimple(""));
//        }
        HashSet<Integer> indexes = new HashSet<>();
        ArrayList<V2D_Point_d> r = new ArrayList<>();
        for (int i = 0; i < pts.size(); i++) {
            if (!indexes.contains(i)) {
                V2D_Point_d p = pts.get(i);
                r.add(p);
                for (int j = i + 1; j < pts.size(); j++) {
                    if (!indexes.contains(j)) {
                        V2D_Point_d p2 = pts.get(j);
                        if (p.equals(p2, epsilon)) {
                            indexes.add(j);
                        }
                    }
                }
            }
        }
//        System.out.println("After unique");
//        for (int i = 0; i < r.size(); i++) {
//            System.out.println("i=" + i);
//            System.out.println(r.get(i).toStringSimple(""));
//        }
        return r;
    }

//    /**
//     * A collection method for getting unique points.
//     *
//     * @param pts The points to derive a unique list from.
//     * @param epsilon The tolerance within which two points are regarded as
//     * equal.
//     * @return A unique list made from those in pts.
//     */
//    public static ArrayList<V2D_Point_d> getUnique(double epsilon, 
//            V2D_Point_d... pts) {
//        return getUnique(Arrays.asList(pts), epsilon);
//    }
    public boolean intersects(V2D_AABB_d aabb, double epsilon) {
        return aabb.intersects(this);
    }

    @Override
    public int compareTo(V2D_Point_d p) {
        if (getY() > p.getY()) {
            return 1;
        } else {
            if (getY() < p.getY()) {
                return -1;
            } else {
                if (getX() > p.getX()) {
                    return 1;
                } else {
                    if (getX() < p.getX()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }
}
