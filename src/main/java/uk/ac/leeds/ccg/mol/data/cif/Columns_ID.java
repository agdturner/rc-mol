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

/**
 * Columns_ID class.
 * @author Andy Turner
 */
public class Columns_ID extends Category_ID {

    private static final long serialVersionUID = 1L;
    
    /**
     * Create a new instance.
     * @param id What {@link #id} is set to.
     */
    public Columns_ID(int id){
        super(id);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o != this) {
                if (o instanceof Columns_ID o2) {
                    if (id == o2.id) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
}
