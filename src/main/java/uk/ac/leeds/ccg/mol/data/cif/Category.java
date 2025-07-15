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
import java.util.OptionalInt;

/**
 * Category class.
 * @author Andy Turner
 */
public class Category {
    
    /**
     * The name.
     */
    public final String name;

    /**
     * The DataItems
     */
    public HashMap<DataItem_ID, DataItem> dis;
    
    /**
     * Create a new instance.
     * 
     * @param name What {@link name} is set to.
     */
    public Category(String name){
        this(name, new HashMap<>());
    }
    
    /**
     * Create a new instance.
     * 
     * @param name What {@link name} is set to.
     * @param dis What {@link dis} is set to.
     */
    public Category(String name, HashMap<DataItem_ID, DataItem> dis){
        this.name = name;
        this.dis = dis;
    }
    
    /**
     * Adds or replaces the DataItem with the DataItem ID.
     * @param id The DataItem ID.
     * @param di The DataItem
     * @return The existing data item or null.
     */
    public DataItem setDataItem(DataItem_ID id, DataItem di) {
        DataItem r = dis.get(id);
        dis.put(id, di);
        return r;
    }
    
    /**
     * @return The maximum token length for all DataItems in {@link #dis}. 
     */
    public int getTokenMaxLength() {
        OptionalInt o = dis.values().stream().map(DataItem::getToken)
                            .mapToInt(String::length)
                            .max();
        return o.orElse(-1); // Return value held by o, or -1 if there is no value.
    }
    
}
