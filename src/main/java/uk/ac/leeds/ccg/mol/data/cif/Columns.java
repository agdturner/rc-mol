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

import java.util.HashMap;
import java.util.TreeMap;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;

/**
 * Columns class. This represents a loop of data. The data are effectively 
 * rectangular and arranged in rows and columns.
 *
 * @author Andy Turner
 */
public class Columns extends Category {

    /**
     * The id.
     */
    public final Columns_ID id;

    /**
     * Data in rows.
     */
    public final TreeMap<Row_ID, TreeMap<Column_ID, Value>> data;
    
    /**
     * Data in columns.
     */
    public final TreeMap<Column_ID, Column> columns;

    /**
     * Lookup Column Name from Column_ID.
     */
    public final HashMap<Column_ID, String> id2name;

    /**
     * Lookup Column_ID from Column Name.
     */
    public final HashMap<String, Column_ID> name2id;

    /**
     * Create a new instance.
     *
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     */
    public Columns(String name, Columns_ID id) {
        super(name);
        this.id = id;
        data = new TreeMap<>();
        columns = new TreeMap<>();
        id2name = new HashMap<>();
        name2id = new HashMap<>();
    }

    /**
     * For getting a value.
     *
     * @param rid The Row_ID of the value to get.
     * @param cid The Column_ID of the value to get
     * @return The value for the row and column with the given IDs.
     */
    public Value getValue(Row_ID rid, Column_ID cid) {
        return data.get(rid).get(cid);
    }

    /**
     * For storing a value.
     *
     * @param rid The Row_ID of the value to store.
     * @param cid The Column_ID of the value to store
     * @param v The value to store.
     */
    public void setValue(Row_ID rid, Column_ID cid, Value v) {
        data.get(rid).put(cid, v);
        columns.get(cid).values.put(rid, v);
    }

    /**
     * For getting a column.
     *
     * @param cid The Column_ID of the column to get.
     * @return The column with the given Column_ID.
     */
    public Column getColumn(Column_ID cid) {
        return columns.get(cid);
    }

    /**
     * @return The number of columns.
     */
    public int getNCols() {
        return columns.size();
    }

    /**
     * @return The number of rows.
     */
    public int getNRows() {
        return data.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Mol_Strings.s_loop_);
        sb.append(Mol_Environment.EOL);
        columns.keySet().forEach(cid -> {
            Column col = columns.get(cid);
            sb.append(col.name);
            sb.append(Mol_Environment.EOL);
        });
        data.keySet().forEach(rid -> {
            TreeMap<Column_ID, Value> cols = data.get(rid);
            cols.keySet().forEach(cid -> {
                String sv = cols.get(cid).v;
                if (sv.contains("'")) {
                    if (sv.startsWith("'")) {
                        sb.append(sv);
                    } else {
                        sb.append("\"");
                        sb.append(sv);
                        sb.append("\"");
                    }
                } else {
                    if (sv.contains("\"")) {
                        if (sv.startsWith("\"")) {
                            sb.append(sv);
                        } else {
                            sb.append("'");
                            sb.append(sv);
                            sb.append("'");
                        }
                    } else {
                        sb.append(sv);
                    }
                }
                sb.append(Mol_Strings.symbol_space);
            });
            sb.deleteCharAt(sb.length());
            sb.append(Mol_Environment.EOL);
        });
        return sb.toString();
    }
    
    /**
     * For adding a column.
     * @param column The column to add.
     */
    public void addColumn(Column column) {
        Column_ID cid = new Column_ID(columns.size());
        name2id.put(column.name, cid);
        id2name.put(cid, column.name);
        columns.put(cid, column);
        data.keySet().forEach(rid -> {
            data.get(rid).put(cid, null);
        });
    }
}
