package uk.ac.leeds.ccg.mol.data.cif;

import java.util.ArrayList;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import java.util.HashMap;
import uk.ac.leeds.ccg.mol.core.Mol_Object;

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

/**
 * DataBlock class.
 * @author Andy Turner
 */
public class DataBlock extends Mol_Object {

    private static final long serialVersionUID = 1L;

    /**
     * The DataBlockHeading.
     */
    public DataBlockHeading dbh;
    
    /**
     * This list orders Columns and DataItems. 
     */
    public final ArrayList<Category_ID> columnsAndDataItems;
    
    /**
     * A look up from a Column name to a Column_ID.
     */
    protected HashMap<String, Columns_ID> columnsName2ColumnsId;

    /**
     * A look up from a Column_ID to a Column name.
     */
    protected HashMap<Columns_ID, String> columnsId2ColumnsName;
    
    /**
     * Columnss
     */
    protected HashMap<Columns_ID, Columns> columnss;
            
    /**
     * A look up from a DataItem name to a DataItem_ID.
     */
    protected HashMap<String, DataItems_ID> dataItemsName2DataItemsId;

    /**
     * A look up from a DataItem_ID to a DataItem name.
     */
    protected HashMap<DataItems_ID, String> dataItemsId2DataItemsName;

    /**
     * DataItemss
     */
    protected HashMap<DataItems_ID, DataItems> dataItemss;

    /**
     * Create a new instance.
     * @param env What {@link #env} is set to.
     * @param dbh What {@link #dbh} is set to.
     */
    public DataBlock(Mol_Environment env, DataBlockHeading dbh) {
        super(env);
        this.dbh = dbh;
        columnsAndDataItems = new ArrayList<>();
        columnsName2ColumnsId = new HashMap<>();
        columnsId2ColumnsName = new HashMap<>();
        columnss = new HashMap<>();
        dataItemsName2DataItemsId = new HashMap<>();
        dataItemsId2DataItemsName = new HashMap<>();
        dataItemss = new HashMap<>();
    }
    
    /**
     * @return A string representation 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(dbh.toString());
        sb.append(Mol_Environment.EOL);
//        sb.append(categories.toString());
//        sb.append(Mol_Environment.EOL);
//        if (sf != null) {
//            sb.append(sf.toString());
//            sb.append(Mol_Environment.EOL);
//        }
        return sb.toString();
    }
    
    /**
     * @param cs The Columns to add.
     */
    public void addColumns(Columns cs) {
        columnsAndDataItems.add(cs.id);
        columnsId2ColumnsName.put(cs.id, cs.name);
        columnsName2ColumnsId.put(cs.name, cs.id);
        columnss.put(cs.id, cs);
    }

    /**
     * @param ds The DataItems to add.
     */
    public void addDataItems(DataItems ds) {
        columnsAndDataItems.add(ds.id);
        dataItemsId2DataItemsName.put(ds.id, ds.name);
        dataItemsName2DataItemsId.put(ds.name, ds.id);
        dataItemss.put(ds.id, ds);
    }
    
    /**
     * @param id The Columns_ID of the Columns to get
     * @return The Columns with Columns_ID id.
     */
    public Columns getColumns(Columns_ID id) {
        return columnss.get(id);
    }
    
    /**
     * @param name The name of the Columns to get
     * @return The Columns with name.
     */
    public Columns getColumns(String name) {
        return columnss.get(columnsName2ColumnsId.get(name));
    }

    /**
     * @param id The DataItems_ID of the DataItems to get
     * @return The DataItems with DataItems_ID id.
     */
    public DataItems getDataItems(DataItems_ID id) {
        return dataItemss.get(id);
    }
    
    /**
     * @param name The name of the DataItems to get
     * @return The DataItems with name.
     */
    public DataItems getDataItems(String name) {
        return dataItemss.get(dataItemsName2DataItemsId.get(name));
    }
    
    /**
     * @return The next Columns_ID.
     */
    public Columns_ID getNextColumns_ID() {
        return new Columns_ID(columnsId2ColumnsName.size());
    }
    
    /**
     * @return The next DataItems_ID.
     */
    public DataItems_ID getNextDataItems_ID() {
        return new DataItems_ID(dataItemsId2DataItemsName.size());
    }
    
}
