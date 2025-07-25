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

import java.util.ArrayList;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;

/**
 * Columns class. This represents a loop of data. The data are arranged in rows
 * and columns.
 *
 * @author Andy Turner
 */
public class Columns extends Category {
    
    /**
     * The id.
     */
    public final Columns_ID id;

    /**
     * Data in columns.
     */
    public ArrayList<Column> cols;

    /**
     * Data in rows.
     */
    private final ArrayList<ArrayList<Value>> rows;

    /**
     * Create a new instance.
     *
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     */
    public Columns(String name, Columns_ID id) {
        this(name, id, new ArrayList<>());
    }

    /**
     * Create a new instance.
     *
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     * @param rows The data organised as rows of columns.
     */
    public Columns(String name, Columns_ID id,
            ArrayList<ArrayList<Value>> rows) {
        super(name);
        this.id = id;
        this.rows = rows;
        int nrows = rows.size();
        if (nrows > 0) {
            int ncols = rows.get(0).size();
            cols = new ArrayList<>(ncols);
            for (int row = 0; row < nrows; row++) {
                ArrayList<Value> values = rows.get(row);
                for (int col = 0; col < ncols; col++) {
                    Column c = cols.get(col);
                    c.values.set(col, values.get(col));
                }
            }
        } else {
            cols = new ArrayList<>();
        }
    }

    /**
     * @param values The values to add.
     */
    public void addRow(ArrayList<String> values) {
        int row = rows.size();
        for (int col = 0; col < values.size(); col++) {
            Value v = new Value(values.get(col));
            // Add value to column
            cols.get(col).values.add(v);
            // Add value to data organised in rows.
            setValue(row, col, v);
        }
    }

    /**
     * For getting a value.
     *
     * @param row The row index of the value to get.
     * @param col The column index of the value to get.
     * @return The value at the given row and column index.
     */
    public Value getValue(int row, int col) {
        return rows.get(row).get(col);
    }

    /**
     * For setting a value.
     *
     * @param row The row index of the value to set.
     * @param col The column index of the value to set.
     * @param v The value to set.
     */
    public void setValue(int row, int col, Value v) {
        while (rows.size() <= row) {
            rows.add(new ArrayList<>());
        }
        while (rows.get(row).size() <= col) {
            rows.get(row).add(new Value(""));
        }
        rows.get(row).set(col, v);
        cols.get(col).values.set(row, v);
    }
    
    /**
     * @return The number of columns. 
     */
    public int getNCols() {
        return cols.size();
    }
    
    /**
     * @return The number of rows. 
     */
    public int getNRows() {
        return rows.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Mol_Strings.s_loop_);
        sb.append(Mol_Environment.EOL);
        cols.forEach(col -> {
            sb.append(col.name);
            sb.append(Mol_Environment.EOL);
        });
        rows.forEach(row -> {
            row.forEach(col -> {
                sb.append(col.v);
                sb.append(Mol_Strings.symbol_space);
            });
            sb.deleteCharAt(sb.length());
            sb.append(Mol_Environment.EOL);
        });
        return sb.toString();
    }
}
