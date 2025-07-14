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
 *
 * @author Andy Turner
 */
public class DataItem {
    
    /**
     * The value. 
     */
    private String value;
    
    /**
     * The category name.
     */
    private final String categoryName;
    
    /**
     * The attribute name.
     */
    private final String attributeName;
    
    public DataItem(String categoryName, String attributeName) {
        this.categoryName = categoryName;
        this.attributeName = attributeName;
    }
    
    /**
     * @return The mmCIF token.
     */
    public String getName() {
        return Mol_Strings.symbol_underscore + categoryName 
                + Mol_Strings.symbol_dot + attributeName;
    }
    
}
