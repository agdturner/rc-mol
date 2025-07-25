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
package uk.ac.leeds.ccg.mol.core;

import java.io.IOException;
import uk.ac.leeds.ccg.data.core.Data_Environment;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;

/**
 * An Environment class.
 * 
 * @author Andy Turner
 */
public class Mol_Environment extends Data_Environment {
    
    /**
     * The Line Separator.
     */
    public static String EOL = System.getProperty("line.separator");
    
    private static final long serialVersionUID = 1L;
    
    /**
     * For commonly used strings.
     */
    public static Mol_Strings strings;
    
    /**
     * Create a new instance
     * 
     * @param e The Generic_Environment.
     * @throws java.io.IOException
     */
    public Mol_Environment(Generic_Environment e) throws IOException {
        super(e);
        strings = new Mol_Strings();
    } 
    
}
