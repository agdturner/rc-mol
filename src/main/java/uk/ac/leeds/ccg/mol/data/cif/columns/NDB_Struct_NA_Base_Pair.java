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
 * NDB_Struct_NA_Base_Pair Columns.
 * @author Andy Turner
 */
public class NDB_Struct_NA_Base_Pair extends Columns {
    
    /**
     * "ndb_struct_na_base_pair"
     */
    public static String NAME = "ndb_struct_na_base_pair";
    
    /**
     * Create a new instance.
     * @param id What {@link #id} is set to.
     */
    public NDB_Struct_NA_Base_Pair(Columns_ID id) {
        super(NAME, id);
    }
    
}
