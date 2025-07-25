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

//import uk.ac.leeds.ccg.mol.data.cif.Category;
import uk.ac.leeds.ccg.mol.data.cif.DataItems;
import uk.ac.leeds.ccg.mol.data.cif.DataItems_ID;
import uk.ac.leeds.ccg.mol.data.cif.Category_ID;

/**
 * Entry DataItems.
 * @author Andy Turner
 */
public class Entry extends DataItems {
    
    /**
     * "entry"
     */
    public static String NAME = "entry";
    
    /**
     * Create a new instance.
     */
    public Entry(DataItems_ID id) {
        super(NAME, id);
    }
    
}
