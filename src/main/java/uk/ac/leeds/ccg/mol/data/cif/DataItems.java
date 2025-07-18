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
import java.util.OptionalInt;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;

/**
 * DataItems class. This represents a List of DataItems.
 * 
 * @author Andy Turner
 */
public class DataItems extends Category {
    
    /**
     * DataItems.
     */
    public ArrayList<DataItem> dataItems;
    
    /**
     * Create a new instance.
     * 
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     */
    public DataItems(String name, DataItems_ID id){
        this(name, id, new ArrayList<>());
    }
    
    /**
     * Create a new instance.
     * 
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     * @param rows The data organised as rows of columns.
     */
    public DataItems(String name, DataItems_ID id, 
            ArrayList<DataItem> dataItems) {
        super(name, id);
        this.dataItems = dataItems;
    }
    
    /**
     * @param s A line of values.
     */
    public void addItem(DataItem di) {
        
    }
    
    /**
     * @return The maximum token length for all DataItems in {@link #variables}. 
     */
    public int getTokenMaxLength() {
        OptionalInt o = dataItems.stream().map(Variable::getToken)
                            .mapToInt(String::length)
                            .max();
        return o.orElse(-1); // Return value held by o, or -1 if there is no value.
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Mol_Strings.s_loop_);
        sb.append(Mol_Environment.EOL);
        dataItems.forEach(x -> {
            sb.append(x.name);
            sb.append(x.value);
            sb.append(Mol_Environment.EOL);
        });
        return sb.toString();
    }
}
