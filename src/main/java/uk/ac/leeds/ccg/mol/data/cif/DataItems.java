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
import java.util.Iterator;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;

/**
 * DataItems class. This represents a List of DataItems.
 * 
 * @author Andy Turner
 */
public class DataItems extends Category {
    
    /**
     * The id.
     */
    public final DataItems_ID id;
    
    /**
     * DataItems.
     */
    public HashMap<DataItem_ID, DataItem> dataItems;
    
    /**
     * Create a new instance.
     * 
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     */
    public DataItems(String name, DataItems_ID id){
        this(name, id, new HashMap<>());
    }
    
    /**
     * Create a new instance.
     * 
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     * @param dataItems What {@link #dataItems} is set to.
     */
    public DataItems(String name, DataItems_ID id, 
            HashMap<DataItem_ID, DataItem> dataItems) {
        super(name);
        this.id = id;
        this.dataItems = dataItems;
    }
    
    /**
     * @return The maximum token length for all DataItems in {@link #variables}. 
     */
    public int getNameMaxLength() {
        int r = 0;
        Iterator<DataItem> ite = dataItems.values().iterator();
        while (ite.hasNext()) {
            DataItem d = ite.next();
            String n = d.name;
            r = Math.max(r, n.length());
        }
        return r;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Mol_Strings.s_loop_);
        sb.append(Mol_Environment.EOL);
        dataItems.values().forEach(x -> {
            sb.append(x.name);
            sb.append(Mol_Strings.symbol_space);
            sb.append(x.value);
            sb.append(Mol_Environment.EOL);
        });
        return sb.toString();
    }
    
    public DataItem_ID getNextDataItem_ID() {
        return new DataItem_ID(dataItems.size());
    }
}
