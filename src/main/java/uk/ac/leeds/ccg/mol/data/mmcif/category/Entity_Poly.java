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

/**
 * Entity_Poly Category.
 * @author Andy Turner
 */
public class Entity_Poly extends Category {
    
    /**
     * "entity_poly"
     */
    public static String s_entity_poly = "entity_poly";
    
    /**
     * Create a new instance.
     */
    public Entity_Poly() {
        super(s_entity_poly);
    }
    
}
