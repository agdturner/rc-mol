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
 * PDBX_Struct_Sheet_Hbond Category.
 * @author Andy Turner
 */
public class PDBX_Struct_Sheet_Hbond extends Category {
    
    /**
     * "pdbx_struct_sheet_hbond"
     */
    public static String s_pdbx_struct_sheet_hbond = "pdbx_struct_sheet_hbond";
    
    /**
     * Create a new instance.
     */
    public PDBX_Struct_Sheet_Hbond() {
        super(s_pdbx_struct_sheet_hbond);
    }
    
}
