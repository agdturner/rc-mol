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
import java.util.Map.Entry;
import java.util.OptionalInt;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;

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
     * The id.
     */
    public final Category_ID id;
    
    /**
     * Create a new instance.
     * 
     * @param name What {@link name} is set to.
     * @param id What {@link id} is set to.
     */
    public Category(String name, Category_ID id){
        this.name = name;
        this.id = id;
    }
    
}
