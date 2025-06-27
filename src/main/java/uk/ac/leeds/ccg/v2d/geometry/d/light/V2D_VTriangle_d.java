/*
 * Copyright 2020-2025 Andy Turner, University of Leeds.
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
package uk.ac.leeds.ccg.v2d.geometry.d.light;

import java.io.Serializable;

/**
 * For representing simple triangles in 2D. The triangle is
 * defined by three edge lines {@link #pq}, {@link #qr} and {@link #rp}. The
 * following is a depiction {@code
 *
 *  p *- - - - - - - - - - - - - - - - - - - - - - -* q
 *     \                                           /
 *      \                                         /
 *       \                                       /
 *        \                                     /
 *         \                                   /
 *          \                                 /
 *           \                               /
 *            \                             /
 *             \                           /
 *              \                         /
 *               \                       /
 *                \                     /
 *                 \                   /
 *                  \                 /
 *                   \               /
 *                    \             /
 *                     \           /
 *                      \         /
 *                       \       /
 *                        \     /
 *                         \   /
 *                          \ /
 *                           *
 *                           r
 * }
 * A more complicated triangle representation is given in 
 * uk.ac.leeds.ccg.v3d.geometry.d.V2D_Triangle_d.
 *
 * @author Andy Turner
 * @version 2.0
 */
public class V2D_VTriangle_d implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The pq edge.
     */
    public V2D_VLine_d pq;

    /**
     * The qr edge.
     */
    public V2D_VLine_d qr;

    /**
     * The rp edge.
     */
    public V2D_VLine_d rp;

    /**
     * Creates a new triangle.
     *
     * @param pq What {@link #pq} is set to.
     * @param qr What {@link #qr} is set to.
     * @param rp What {@link #rp} is set to.
     */
    public V2D_VTriangle_d(V2D_VLine_d pq, V2D_VLine_d qr, 
            V2D_VLine_d rp) {
        this.pq = pq;
        this.qr = qr;
        this.rp = rp;
    }

    @Override
    public String toString() {
        return toString("");
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
    protected String toStringFields(String pad) {
        return pad + "pq=" + pq.toString(pad) + "\n"
                + pad + ",\n"
                + pad + "qr=" + qr.toString(pad) + "\n"
                + pad + ",\n"
                + pad + "rp=" + rp.toString(pad);
    }
}
