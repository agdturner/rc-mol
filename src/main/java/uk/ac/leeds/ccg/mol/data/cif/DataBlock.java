package uk.ac.leeds.ccg.mol.data.cif;

import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import java.util.HashMap;
import uk.ac.leeds.ccg.mol.core.Mol_Object;

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

/**
 * DataBlock class.
 * @author Andy Turner
 */
public class DataBlock extends Mol_Object {

    private static final long serialVersionUID = 1L;

    /**
     * The DataBlockHeading.
     */
    public DataBlockHeading dbh;
    
    /**
     * The categories
     */
    HashMap<Category_ID, Category> categories;

    /**
     * The SaveFrame
     */
    public SaveFrame sf;
    
    /**
     * Create a new instance.
     * @param env What {@link #env} is set to.
     * @param dbh What {@link #dbh} is set to.
     */
    public DataBlock(Mol_Environment env, DataBlockHeading dbh){
        this(env, dbh, new HashMap<>());
    }

    /**
     * Create a new instance.
     * @param env What {@link #env} is set to.
     * @param dbh What {@link #dbh} is set to.
     * @param categories What {@link #categories} is set to.
     */
    public DataBlock(Mol_Environment env, DataBlockHeading dbh, 
            HashMap<Category_ID, Category> categories){
        this(env, dbh, categories, null);
    }

    /**
     * Create a new instance.
     * @param env What {@link #env} is set to.
     * @param dbh What {@link #dbh} is set to.
     * @param categories What {@link #categories} is set to.
     * @param sf What {@link #sf} is set to.
     */
    public DataBlock(Mol_Environment env, DataBlockHeading dbh, 
            HashMap<Category_ID, Category> categories, SaveFrame sf) {
        super(env);
        this.dbh = dbh;
        this.categories = categories;
        this.sf = sf;
    }
    
    /**
     * @return A string representation 
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(dbh.toString());
        sb.append(Mol_Environment.EOL);
        sb.append(categories.toString());
        sb.append(Mol_Environment.EOL);
        if (sf != null) {
            sb.append(sf.toString());
            sb.append(Mol_Environment.EOL);
        }
        return sb.toString();
    }

}
