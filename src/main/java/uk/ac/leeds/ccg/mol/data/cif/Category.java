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
     * The Variables
     */
    public HashMap<Variable_ID, Variable> variables;
    
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
     * @param variables What {@link #variables} is set to.
     */
    public Category(String name, HashMap<Variable_ID, Variable> variables){
        this.name = name;
        this.variables = variables;
    }
    
    /**
     * Adds or replaces the Variable with the Variable ID.
     * @param id The Variable ID.
     * @param v The Variable.
     * @return The existing Variable or null.
     */
    public Variable setVariable(Variable_ID id, Variable v) {
        Variable r = variables.get(id);
        variables.put(id, v);
        return r;
    }
    
    /**
     * @return The maximum token length for all DataItems in {@link #variables}. 
     */
    public int getTokenMaxLength() {
        OptionalInt o = variables.values().stream().map(Variable::getToken)
                            .mapToInt(String::length)
                            .max();
        return o.orElse(-1); // Return value held by o, or -1 if there is no value.
    }
    
}
