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

import uk.ac.leeds.ccg.data.id.Data_ID_int;

/**
 * Column_ID class.
 * @author Andy Turner
 */
public class Column_ID extends Data_ID_int {

    private static final long serialVersionUID = 1L;
    
    /**
     * Create a new instance.
     * @param id What {@link #id} is set to.
     */
    public Column_ID(int id){
        super(id);
    }
    
}
