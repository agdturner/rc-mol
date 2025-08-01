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
package uk.ac.leeds.ccg.mol.run;

import uk.ac.leeds.ccg.mol.io.Mol_TextCifWriter;
import uk.ac.leeds.ccg.mol.io.Mol_TextCifReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.mol.data.cif.CIF;
import uk.ac.leeds.ccg.mol.geom.QCProt;

/**
 *
 * @author Andy Turner
 */
public class Align {
    
    /**
     * Download data:
     * 
     * https://files.rcsb.org/download/4ug0.cif
     * https://files.rcsb.org/download/6xu8.cif
     * https://files.rcsb.org/download/6xyw.cif
     * https://files.rcsb.org/download/6xu6.cif
     * https://files.rcsb.org/download/8scb.cif
     * https://files.rcsb.org/download/7qwq.cif
     * https://files.rcsb.org/download/8ccs.cif
     * https://files.rcsb.org/download/4v88.cif
     * https://files.rcsb.org/download/6fxc.cif
     * @param args 
     */
    public static void main(String[] args) {
        try {
        Mol_TextCifReader reader = new Mol_TextCifReader();
        Mol_TextCifWriter writer = new Mol_TextCifWriter();
        
        Path dir = Paths.get("C:", "Users", "geoagdt", "Downloads");
        String pdbid1 = "4ug0";
//        String pdbid2 = "6xu8";
//        String pdbid2 = "6xyw";
//        String pdbid2 = "6xu6";
        String pdbid2 = "8scb";
//        String pdbid2 = "7qwq";
//        String pdbid2 = "8ccs";
//        String pdbid2 = "4v88";
//        String pdbid2 = "6fxc";
        
        CIF cif1 = null;
        //CIF cif1 = reader.getCif(pdbid1 + "_centralised", dir);
        //CIF cif2 = reader.getCif(pdbid2 + "_centralised", dir);
        //CIF cif1 = reader.getCif(pdbid1, dir);
        CIF cif2 = reader.getCif(pdbid2, dir);
        //cif1.centralise(null);
        //cif2.centralise(null);
        TreeSet<String> atomTypes = new TreeSet<>();
        atomTypes.add("P");
        cif1.centralise(atomTypes);
        cif2.centralise(atomTypes);
        String atomTypesString = "_";
        Iterator<String> ite = atomTypes.iterator();
        while (ite.hasNext()) {
            atomTypesString += ite.next();
        }
        String name = atomTypesString + "_centralised";
        writer.write(cif1, dir, pdbid1, name);
        writer.write(cif2, dir, pdbid2, name);
        
        QCProt qcprot = new QCProt(cif1, cif2, atomTypes);
        
        cif2.rotate(qcprot.rotmat);
        name += "_rotated";
        writer.write(cif2, dir, pdbid2, name);
        } catch (IOException ex) {
            Logger.getLogger(Align.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
