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

import uk.ac.leeds.ccg.mol.core.Mol_Strings;

/**
 * Column class.
 * @author Andy Turner
 */
public class Variable {
    
    /**
     * The category.
     */
    public final Category category;
    
    /**
     * The name.
     */
    public final String name;
    
    /**
     * Create a new instance.
     * @param category What {@link #category} is set to.
     * @param name What {@link #name} is set to.
     */
    public Variable(Category category, String name) {
        this.category = category;
        this.name = name;
    }
    
    /**
     * @return The mmCIF token.
     */
    public String getToken() {
        return Mol_Strings.symbol_underscore + category.name 
                + Mol_Strings.symbol_dot + name;
    }
}
