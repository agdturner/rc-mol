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
import uk.ac.leeds.ccg.mol.core.Mol_Strings;

/**
 *
 * @author Andy Turner
 */
public class Comment {
    
    /**
     * The comment.
     */
    public String comment;
    
    /**
     * Create a new instance
     * @param comment What {@link #comment} is set to.
     */
    public Comment(String comment){
        this.comment = comment;
    }
    
    @Override
    public String toString() {
        return Mol_Strings.SYMBOL_HASH + comment + Mol_Environment.EOL;
    }
    
}
