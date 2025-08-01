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
package uk.ac.leeds.ccg.mol.data.cif;

import ch.obermuhlner.math.big.BigRational;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import uk.ac.leeds.ccg.math.arithmetic.Math_BigRational;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Object;

/**
 * CIF class.
 *
 * @author Andy Turner
 */
public class CIF extends Mol_Object {

    private static final long serialVersionUID = 1L;

    public static int LINE_CHAR_LENGTH_MAX = 2048;

    public static int NAME_CODE_LENGTH_MAX = 75;

    public static int HEADER_LENGTH_MAX = 80;
    
    /**
     * The data.
     */
    public ArrayList<DataBlock> dataBlocks;

    /**
     * Comments.
     */
    public ArrayList<Comment> comments;

    /**
     * @param env What {@link #env} is set to.
     */
    public CIF(Mol_Environment env) {
        super(env);
        dataBlocks = new ArrayList<>();
        comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        comments.stream().forEach(x -> {
            sb.append(x.toString());
        });
        dataBlocks.stream().forEach(x -> {
            sb.append(x.toString());
        });
        return sb.toString();
    }

    /**
     * @return A double[][] of the coordinates.
     */
    public double[][] getCoords(TreeSet<String> atomTypes) {
        Columns columns = dataBlocks.get(0).getColumns("atom_site");
        // Get the columns
        Column_ID type_symbol_cid = columns.name2id.get("type_symbol");
        Column type_symbol_column = columns.getColumn(type_symbol_cid);
        Column_ID x_cid = columns.name2id.get("Cartn_x");
        Column x_column = columns.getColumn(x_cid);
        Column_ID y_cid = columns.name2id.get("Cartn_y");
        Column y_column = columns.getColumn(y_cid);
        Column_ID z_cid = columns.name2id.get("Cartn_z");
        Column z_column = columns.getColumn(z_cid);

        double[][] coords = null;
        // Get the coordinates from cif
        if (atomTypes == null) {
            int len = x_column.values.size();
            coords = new double[3][len];
            for (int i = 0; i < len; i ++) {
                Row_ID rid = new Row_ID(i);
                coords[0][i] = Double.parseDouble(x_column.values.get(rid).v);
                coords[1][i] = Double.parseDouble(y_column.values.get(rid).v);
                coords[2][i] = Double.parseDouble(z_column.values.get(rid).v);
            }
        } else {
            TreeMap<Row_ID, Double> xs = new TreeMap<>();
            TreeMap<Row_ID, Double> ys = new TreeMap<>();
            TreeMap<Row_ID, Double> zs = new TreeMap<>();
            int len = x_column.values.size();
            for (int i = 0; i < len; i ++) {
                Row_ID rid = new Row_ID(i);
                String atomType = type_symbol_column.values.get(rid).v;
                if (atomTypes.contains(atomType)) {
                    xs.put(rid, Double.valueOf(x_column.values.get(rid).v));
                    ys.put(rid, Double.valueOf(y_column.values.get(rid).v));
                    zs.put(rid, Double.valueOf(z_column.values.get(rid).v));
                }
            }
            coords = new double[3][xs.size()];
            int i = 0;
            for (Row_ID rid : xs.keySet()) {
                coords[0][i] = xs.get(rid);
                coords[1][i] = ys.get(rid);
                coords[2][i] = zs.get(rid);
                i ++;
            }
        }
        return coords;
    }
    
    /**
     * @param rotmat The rotation matrix. 
     */
    public void rotate(double[] rotmat) {
        // Get the coordinates from cif
        Columns columns = dataBlocks.get(0).getColumns("atom_site");
        Column_ID x_cid = columns.name2id.get("Cartn_x");
        Column x_column = columns.getColumn(x_cid);
        Column_ID y_cid = columns.name2id.get("Cartn_y");
        Column y_column = columns.getColumn(y_cid);
        Column_ID z_cid = columns.name2id.get("Cartn_z");
        Column z_column = columns.getColumn(z_cid);
        
        for (var rid : x_column.values.keySet()) {
            double x = Double.parseDouble(x_column.values.get(rid).v);
            double y = Double.parseDouble(y_column.values.get(rid).v);
            double z = Double.parseDouble(z_column.values.get(rid).v);
            columns.setValue(rid, x_cid, new Value(Double.toString(rotmat[0] * x + rotmat[1] * y + rotmat[2] * z)));
            columns.setValue(rid, y_cid, new Value(Double.toString(rotmat[3] * x + rotmat[4] * y + rotmat[5] * z)));
            columns.setValue(rid, z_cid, new Value(Double.toString(rotmat[6] * x + rotmat[7] * y + rotmat[8] * z)));
        }
        
    }
    
    /**
     * Translate all coordinates so that the average is the centre.
     * @param atomType e.g. P for phosphorous, C for Carbon.
     */
    public void centralise(TreeSet<String> atomTypes) {
        // Calculate the average atom location.
        Columns columns = dataBlocks.get(0).getColumns("atom_site");
        
        Column_ID type_symbol_cid = columns.name2id.get("type_symbol");
        Column type_symbol_column = columns.getColumn(type_symbol_cid);
        

        Column_ID x_cid = columns.name2id.get("Cartn_x");
        Column x_column = columns.getColumn(x_cid);
        Column_ID y_cid = columns.name2id.get("Cartn_y");
        Column y_column = columns.getColumn(y_cid);
        Column_ID z_cid = columns.name2id.get("Cartn_z");
        Column z_column = columns.getColumn(z_cid);

        BigRational x_sum = BigRational.ZERO;
        BigRational y_sum = BigRational.ZERO;
        BigRational z_sum = BigRational.ZERO;
        
        int n = 0;
        if (atomTypes == null) {
            for (var rid : x_column.values.keySet()) {
                x_sum = x_sum.add(BigRational.valueOf(x_column.values.get(rid).v));
                y_sum = y_sum.add(BigRational.valueOf(y_column.values.get(rid).v));
                z_sum = z_sum.add(BigRational.valueOf(z_column.values.get(rid).v));
                n ++;
            }
        } else {
            for (var rid : x_column.values.keySet()) {
                if (atomTypes.contains(type_symbol_column.values.get(rid).v)) {
                    x_sum = x_sum.add(BigRational.valueOf(x_column.values.get(rid).v));
                    y_sum = y_sum.add(BigRational.valueOf(y_column.values.get(rid).v));
                    z_sum = z_sum.add(BigRational.valueOf(z_column.values.get(rid).v));
                    n ++;
                }
            }
        }
        //System.out.println("x_sum " + x_sum.toString());
        //System.out.println("y_sum " + y_sum.toString());
        //System.out.println("z_sum " + z_sum.toString());
        BigRational x_average = x_sum.divide(n);
        BigRational y_average = y_sum.divide(n);
        BigRational z_average = z_sum.divide(n);
        //System.out.println("x average " + x_average);
        //System.out.println("y average " + y_average);
        //System.out.println("z average " + z_average);

        // Translate all coordinates so that the average is the centre.
        for (var rid : x_column.values.keySet()) {
            BigRational x = Math_BigRational.round(BigRational.valueOf(x_column.values.get(rid).v).subtract(x_average), -3, RoundingMode.UP);
            BigRational y = Math_BigRational.round(BigRational.valueOf(y_column.values.get(rid).v).subtract(y_average), -3, RoundingMode.UP);
            BigRational z = Math_BigRational.round(BigRational.valueOf(z_column.values.get(rid).v).subtract(z_average), -3, RoundingMode.UP);
            columns.setValue(rid, x_cid, new Value(x.toPlainString()));
            columns.setValue(rid, y_cid, new Value(y.toPlainString()));
            columns.setValue(rid, z_cid, new Value(z.toPlainString()));
        }
    }
}
