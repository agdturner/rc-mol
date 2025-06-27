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
package uk.ac.leeds.ccg.v2d.geometry;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import uk.ac.leeds.ccg.v2d.core.V2D_Environment;

/**
 * The simplest V2D_Area is a V2D_Triangle that represents a triangle in 2D.
 * V2D_ConvexArea extends V2D_Area and is for representing a convex area
 * comprising one or more triangles sharing internal edges and with a convex
 * external edge. All points of convex areas are on the external edge.
 * V2D_PolygonNoInternalHoles are defined by a V2D_ConvexArea and a collection
 * of non edge sharing V2D_PolygonNoInternalHoles areas each defining an
 * external hole or concavity. V2D_Polygon extends V2D_PolygonNoInternalHoles
 * and is also defined by a collection of non edge sharing internal holes each
 * represented also as a V2D_Polygon.
 * 
 * Conceivably it may be useful to add a further abstraction and have a class 
 * which is similar to a V2D_Polygon but allows external and internal holes to 
 * contain internal holes.
 * 
 * @author Andy Turner
 * @version 1.0
 */
public abstract class V2D_Area extends V2D_FiniteGeometry {

    private static final long serialVersionUID = 1L;

    /**
     * The id of the shape.
     */
    protected final int id;

    /**
     * Creates a new instance with offset V2D_Vector.ZERO.
     *
     * @param env What {@link #env} is set to.
     * @param offset What {@link #offset} is set to.
     */
    public V2D_Area(V2D_Environment env, V2D_Vector offset) {
        super(env, offset);
        this.id = env.getNextID();
    }

    /**
     * For storing the points.
     */
    protected HashMap<Integer, V2D_Point> points;

    /**
     * For storing the edges.
     */
    protected HashMap<Integer, V2D_LineSegment> edges;

    /**
     * For getting the points of a shape.
     *
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode if rounding is needed.
     * @return A HashMap of the points with integer identifier keys.
     */
    public abstract HashMap<Integer, V2D_Point> getPoints(int oom,
            RoundingMode rm);

    /**
     * For getting the edges of a shape.
     *
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode if rounding is needed.
     * @return A HashMap of the edges with integer identifier keys.
     */
    public abstract HashMap<Integer, V2D_LineSegment> getEdges(int oom,
            RoundingMode rm);

    /**
     * @return A copy of the points of the geometries gs.
     *
     * @param ss The geometries.
     * @param oom The Order of Magnitude for the precision.
     * @param rm The RoundingMode if rounding is needed.
     */
    public static ArrayList<V2D_Point> getPoints(HashMap<Integer, V2D_Area> ss,
            int oom, RoundingMode rm) {
        ArrayList<V2D_Point> list = new ArrayList<>();
        ss.values().forEach(x -> list.addAll(x.getPoints(oom, rm).values()));
        return list;
    }
}
