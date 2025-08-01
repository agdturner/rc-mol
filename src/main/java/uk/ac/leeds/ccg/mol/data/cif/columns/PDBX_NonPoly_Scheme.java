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
package uk.ac.leeds.ccg.mol.data.cif.columns;

import uk.ac.leeds.ccg.mol.data.cif.Columns;
import uk.ac.leeds.ccg.mol.data.cif.Columns_ID;

/**
 * PDBX_NonPoly_Scheme Columns.
 * @author Andy Turner
 */
public class PDBX_NonPoly_Scheme extends Columns {
    
    /**
     * "pdbx_nonpoly_scheme"
     */
    public static String NAME = "pdbx_nonpoly_scheme";
    
    /**
     * Create a new instance.
     * @param id What {@link #id} is set to.
     */
    public PDBX_NonPoly_Scheme(Columns_ID id) {
        super(NAME, id);
    }
    
}
