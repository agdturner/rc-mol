package uk.ac.leeds.ccg.mol.data.cif;

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
import java.util.ArrayList;
import java.util.HashMap;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Object;

/**
 * CIF class.
 * @author Andy Turner
 */
public class CIF extends Mol_Object {

    private static final long serialVersionUID = 1L;
    
    /**
     * A look up from a name to the key.
     */
    public HashMap<String, Integer> name2key;

    /**
     * A look up from a key to a name.
     */
    public HashMap<Integer, String> key2name;
    
    /**
     * Comments
     */
    public ArrayList<Comment> comments;
    
    /**
     * DataBlocks
     */
    public ArrayList<DataBlock> dataBlocks;
    
    /**
     * @param env What {@link #env} is set to.
     */
    public CIF(Mol_Environment env){
        super(env);
        this.name2key = new HashMap<>();
        this.key2name = new HashMap<>();
        this.comments = new ArrayList<>();
        this.dataBlocks = new ArrayList<>();
    }
    
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
