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
package uk.ac.leeds.ccg.mol.data.mmcif.category;

import uk.ac.leeds.ccg.mol.data.cif.Category;
import uk.ac.leeds.ccg.mol.data.cif.Category_ID;

/**
 * EM_Single_Particle_Entity Category.
 * @author Andy Turner
 */
public class EM_Single_Particle_Entity extends Category {
    
    /**
     * "em_single_particle_entity"
     */
    public static String NAME = "em_single_particle_entity";
    
    /**
     * Create a new instance.
     * @param id What {@link #id} is set to.
     */
    public EM_Single_Particle_Entity(Category_ID id) {
        super(NAME, id);
    }
    
}
