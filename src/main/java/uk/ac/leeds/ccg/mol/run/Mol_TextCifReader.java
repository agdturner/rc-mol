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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import uk.ac.leeds.ccg.data.format.Data_ReadTXT;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Files;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;
import uk.ac.leeds.ccg.mol.data.cif.CIF;
import uk.ac.leeds.ccg.mol.data.cif.Category;
import uk.ac.leeds.ccg.mol.data.cif.Comment;
import uk.ac.leeds.ccg.mol.data.cif.DataBlock;
import uk.ac.leeds.ccg.mol.data.cif.DataBlockHeading;
import uk.ac.leeds.ccg.mol.data.cif.DataItem;
import uk.ac.leeds.ccg.mol.data.cif.DataItem_ID;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Audit_Conform;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Database_2;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entry;

/**
 *
 * @author Andy Turner
 */
public class Mol_TextCifReader {
    
    /**
     * Create a new instance.
     */
    public Mol_TextCifReader() {
    }

    public static void main(String[] args) {
        Mol_TextCifReader ex = new Mol_TextCifReader();
        try {
            //String pdbId = "4ug0";
            String pdbId = "6xu8";
            //boolean parseBinary = true;
            boolean parseBinary = false;
            System.out.println("Load " + pdbId + " parseBinary " + Boolean.toString(parseBinary));
//            /**
//             * CIF and BinaryCIF are stored in the same data structure to access
//             * the data, it does not matter where and in which format the data
//             * came from all relevant IO operations are exposed by the CifIO
//             * class.
//             */
//            CifFile cifFile;
//            if (parseBinary) {
//                cifFile = CifIO.readFromURL(new URL("https://models.rcsb.org/" + pdbId + ".bcif"));
//            } else {
//                // parse CIF from RCSB PDB
//                cifFile = CifIO.readFromURL(new URL("https://files.rcsb.org/download/" + pdbId + ".cif"));
//            }
//            // fine-grained options are available in the CifOptions class

            Path p = Paths.get("C:", "Users", "geoagdt", "Downloads", "6xu8.cif");
            BufferedReader br = Generic_IO.getBufferedReader(p);
            Generic_Files files = new Generic_Files(new Generic_Defaults());
            Mol_Environment env = new Mol_Environment(new Generic_Environment(files));
            Data_ReadTXT reader = new Data_ReadTXT(env);
            reader.setStreamTokenizer(br, 9);
            
            CIF cif = new CIF(env);
            
            DataBlock db;
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith(Mol_Strings.SYMBOL_HASH)) {
                    cif.comments.add(new Comment(line.split(Mol_Strings.SYMBOL_HASH)[1]));
                } else if (line.startsWith(Mol_Strings.s_data_)) {
                     DataBlockHeading dbh = new DataBlockHeading(env, 
                            line.split(Mol_Strings.s_data_)[1]);
                    db = new DataBlock(env, dbh);
                    cif.dataBlocks.add(db);
                } else {
                    if (line.startsWith(Mol_Strings.symbol_underscore)) {
                        String[] parts = line.split("\\."); // Need to escape the dot
                        String categoryName = parts[0].substring(1);
                        Category category;
                        if (categoryName.equalsIgnoreCase(Entry.s_entry)) {
                            category = new Entry();
                        } else if (categoryName.equalsIgnoreCase(Audit_Conform.s_audit_conform)) {
                            category = new Audit_Conform();                       
                        } else if (categoryName.equalsIgnoreCase(Database_2.s_database_2)) {
                            category = new Database_2();
                        } else {
                            throw new Exception("Unrecognised category name");
                        }
                        String[] nameAndValue = parts[1].split("\\s+");
                        int key = cif.key2name.size();
                        cif.name2key.put(nameAndValue[0], key);
                        cif.key2name.put(key, nameAndValue[0]);
                        DataItem di = new DataItem(category, nameAndValue[0], nameAndValue[1]);
                        category.dis.put(new DataItem_ID(key), di);
                    }
                }
                System.out.println(line);
                line = reader.readLine();
            }
            
//            /**
//             * Access can be generic or using a specified schema - currently
//             * supports MMCIF and CIF_CORE.
//             */
//            MmCifFile mmCifFile = cifFile.as(StandardSchemata.MMCIF);
//            
//            List<MmCifBlock> blocks = mmCifFile.getBlocks();
//            int size = blocks.size();          
//            if (size != 1) {
//                System.err.println("Expecting 1 block, but there are " + size + "!");
//            }
//            //MmCifBlock data = mmCifFile.getFirstBlock();
//            MmCifBlock block = blocks.getFirst();
//            String str = "data_" + block.getBlockHeader();
//            //addLine(lines, str);
//            
//            
//
//            //ex.translate(mmCifFile, 100d, 200d, 300d);
//
//            // the created CifFile instance behaves like a parsed file and can be processed or written as needed
//            ex.dir = Paths.get("C:", "Users", "geoagdt", "Downloads");
//            Path p = Paths.get(ex.dir.toString(), "testtextsb.cif");
//            
//           ex.writeFile(ex.write(cifFile), p);
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
 
    /**
     * For writing CIF format data to file.
     *
     * @param lines The lines to write.
     * @param p The path of the file to be written.
     * @throws IOException
     */
    public void writeFile(byte[] bytes, Path p) throws IOException {
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
        try (var bos = Generic_IO.getBufferedOutputStream(p)) {
            bos.write(bytes);
            bos.flush();
        }
    }
}
