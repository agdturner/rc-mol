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

import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Object;

/**
 * Class for a DataBlockHeading
 * 
 * @author Andy Turner
 */
public class Heading extends Mol_Object {
    
    /**
     * The type that distinguishes this.
     */
    public String type;
    
    /**
     * The name that distinguishes this.
     */
    public String name;

    /**
     * Create a new instance.
     * @param env What {@link #env} is set to.
     * @param type What {@link #type} is set to. 
     * @param name What {@link #name} is set to. 
     */
    public Heading(Mol_Environment env, String type, String name) {
        super(env);
        this.type = type;
        this.name = name;
    }
    
    /**
     * @return {@link #type} + {@link #name}.
     */
    public String toString() {
        return type + name;
    }
}
