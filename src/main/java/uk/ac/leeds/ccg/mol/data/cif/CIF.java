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

import java.util.ArrayList;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Object;

/**
 * CIF class.
 * @author Andy Turner
 */
public class CIF extends Mol_Object {

    private static final long serialVersionUID = 1L;
    
    /**
     * The data.
     */
    public ArrayList<DataBlock> dataBlocks;
    
    /**
     * Comments.
     */
    public ArrayList<Comment> comments;
    
    /**
     * @param env What {@link #env} is set to.
     */
    public CIF(Mol_Environment env){
        super(env);
        dataBlocks = new ArrayList<>();
        comments = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        comments.stream().forEach(x -> {
            sb.append(x.toString());
        });
        dataBlocks.stream().forEach(x -> {
            sb.append(x.toString());
        });
        return sb.toString();
    }
}
