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
package uk.ac.leeds.ccg.mol.data.cif.data_items;

import uk.ac.leeds.ccg.mol.data.cif.DataItems;
import uk.ac.leeds.ccg.mol.data.cif.DataItems_ID;

/**
 * EM_Entity_Assembly_NaturalSource DataItems.
 * @author Andy Turner
 */
public class EM_Entity_Assembly_NaturalSource extends DataItems {
    
    /**
     * "em_entity_assembly_naturalsource"
     */
    public static String NAME = "em_entity_assembly_naturalsource";
    
    /**
     * Create a new instance.
     * @param id What {@link #id} is set to.
     */
    public EM_Entity_Assembly_NaturalSource(DataItems_ID id) {
        super(NAME, id);
    }
    
}
