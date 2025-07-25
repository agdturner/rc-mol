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


/**
 * Column class.
 * @author Andy Turner
 */
public class Column extends Variable {
    
    /**
     * For storing the maximum width of a value.
     */
    private Integer w;
    
    /**
     * The values. 
     */
    public ArrayList<Value> values;
    
    /**
     * Create a new instance.
     * @param columns What {@link #category} is set to.
     * @param name What {@link #name} is set to.
     */
    public Column(Columns columns, String name) {
        super(columns, name);
        this.values = new ArrayList<>();
    }
    
    /**
     * @return The maximum text width of {@link #values}.
     */
    public int getWidth() {
        if (w == null) {
            w = 0;
            values.forEach(x -> {
                w = Math.max(w, x.v.length());
            });
        }
        return w;
    }
}
