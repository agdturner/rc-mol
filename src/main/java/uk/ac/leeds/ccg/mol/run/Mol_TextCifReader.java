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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import uk.ac.leeds.ccg.data.format.Data_ReadTXT;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Files;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;
import uk.ac.leeds.ccg.mol.data.cif.CIF;
import uk.ac.leeds.ccg.mol.data.cif.Category;
import uk.ac.leeds.ccg.mol.data.cif.Column;
import uk.ac.leeds.ccg.mol.data.cif.Comment;
import uk.ac.leeds.ccg.mol.data.cif.DataBlock;
import uk.ac.leeds.ccg.mol.data.cif.DataBlockHeading;
import uk.ac.leeds.ccg.mol.data.cif.DataItem;
import uk.ac.leeds.ccg.mol.data.cif.Variable_ID;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Audit_Author;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Audit_Conform;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Cell;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Chem_Comp;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Citation;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Citation_Author;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Database_2;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Name_Com;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Poly;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Poly_Seq;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Src_Nat;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entry;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Exptl;
import uk.ac.leeds.ccg.mol.data.mmcif.category.NDB_Struct_NA_Base_Pair_Step;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Category;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Details;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Group;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_History;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Item;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Database_Related;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Database_Status;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Poly_Seq_Scheme;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Assembley;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Assembley_Auth_Evidence;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Assembley_Gen;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Oper_List;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Unobs_or_Zero_Occ_Atoms;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Asym;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Keywords;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Ref;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Ref_Seq;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Ref_Seq_Dif;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Symmetry;

/**
 *
 * @author Andy Turner
 */
public class Mol_TextCifReader {

    protected Data_ReadTXT reader;

    protected CIF cif;

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
            ex.reader = new Data_ReadTXT(env);
            ex.reader.setStreamTokenizer(br, 10);

            ex.cif = new CIF(env);

            DataBlock db;
            String line = ex.reader.readLine();
            System.out.println(line);
            while (line != null) {
                if (line.startsWith(Mol_Strings.SYMBOL_HASH)) {
                    ex.cif.comments.add(new Comment(line.split(Mol_Strings.SYMBOL_HASH)[1]));
                } else if (line.startsWith(Mol_Strings.s_data_)) {
                    DataBlockHeading dbh = new DataBlockHeading(env,
                            line.split(Mol_Strings.s_data_)[1]);
                    db = new DataBlock(env, dbh);
                    ex.cif.dataBlocks.add(db);
                } else {
                    if (line.startsWith(Mol_Strings.s_loop_)) {
                        ex.parseLoop();
                    } else {
                        if (line.startsWith(Mol_Strings.symbol_underscore)) {
                            String[] parts = line.split("\\."); // Need to escape the dot
                            String categoryName = parts[0].substring(1);
                            Category category = ex.getCategory(categoryName);
                            String[] nameAndValue = parts[1].split("\\s+");
                            Variable_ID id = new Variable_ID(ex.cif.id2name.size());
                            ex.cif.name2id.put(nameAndValue[0], id);
                            ex.cif.id2name.put(id, nameAndValue[0]);
                            DataItem di = new DataItem(category, nameAndValue[0], nameAndValue[1]);
                            category.variables.put(id, di);
                        }
                    }
                }
                line = ex.reader.readLine();
                System.out.println(line);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    protected Category getCategory(String categoryName) throws Exception {
        Category c;
        if (categoryName.equalsIgnoreCase(Entry.s_entry)) {
            c = new Entry();
        } else if (categoryName.equalsIgnoreCase(Audit_Conform.s_audit_conform)) {
            c = new Audit_Conform();
        } else if (categoryName.equalsIgnoreCase(Database_2.s_database_2)) {
            c = new Database_2();
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_History.s_pdbx_audit_revision_history)) {
            c = new PDBX_Audit_Revision_History();
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Details.s_pdbx_audit_revision_details)) {
            c = new PDBX_Audit_Revision_Details();
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Group.s_pdbx_audit_revision_group)) {
            c = new PDBX_Audit_Revision_Group();
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Category.s_pdbx_audit_revision_category)) {
            c = new PDBX_Audit_Revision_Category();
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Item.s_pdbx_audit_revision_item)) {
            c = new PDBX_Audit_Revision_Item();
        } else if (categoryName.equalsIgnoreCase(PDBX_Database_Status.s_pdbx_database_status)) {
            c = new PDBX_Database_Status();
        } else if (categoryName.equalsIgnoreCase(PDBX_Database_Related.s_pdbx_database_related)) {
            c = new PDBX_Database_Related();
        } else if (categoryName.equalsIgnoreCase(Audit_Author.s_audit_author)) {
            c = new Audit_Author();
        } else if (categoryName.equalsIgnoreCase(Citation.s_citation)) {
            c = new Citation();
        } else if (categoryName.equalsIgnoreCase(Citation_Author.s_citation_author)) {
            c = new Citation_Author();
        } else if (categoryName.equalsIgnoreCase(Entity.s_entity)) {
            c = new Entity();
        } else if (categoryName.equalsIgnoreCase(Entity_Name_Com.s_entity_name_com)) {
            c = new Entity_Name_Com();
        } else if (categoryName.equalsIgnoreCase(Entity_Poly.s_entity_poly)) {
            c = new Entity_Poly();
        } else if (categoryName.equalsIgnoreCase(Entity_Poly_Seq.s_entity_poly_seq)) {
            c = new Entity_Poly_Seq();
        } else if (categoryName.equalsIgnoreCase(NDB_Struct_NA_Base_Pair_Step.s_ndb_struct_na_base_pair_step)) {
            c = new Entity_Poly();
        } else if (categoryName.equalsIgnoreCase(Entity_Src_Nat.s_entity_src_nat)) {
            c = new Entity_Src_Nat();
        } else if (categoryName.equalsIgnoreCase(Chem_Comp.s_chem_comp)) {
            c = new Chem_Comp();
        } else if (categoryName.equalsIgnoreCase(PDBX_Poly_Seq_Scheme.s_pdbx_poly_seq_scheme)) {
            c = new PDBX_Poly_Seq_Scheme();
        } else if (categoryName.equalsIgnoreCase(PDBX_Unobs_or_Zero_Occ_Atoms.s_pdbx_unobs_or_zero_occ_atoms)) {
            c = new PDBX_Unobs_or_Zero_Occ_Atoms();
        } else if (categoryName.equalsIgnoreCase(Cell.s_cell)) {
            c = new Cell();
        } else if (categoryName.equalsIgnoreCase(Symmetry.s_symmetry)) {
            c = new Symmetry();
        } else if (categoryName.equalsIgnoreCase(Exptl.s_exptl)) {
            c = new Exptl();
        } else if (categoryName.equalsIgnoreCase(Struct.s_struct)) {
            c = new Struct();
        } else if (categoryName.equalsIgnoreCase(Struct_Keywords.s_struct_keywords)) {
            c = new Struct_Keywords();
        } else if (categoryName.equalsIgnoreCase(Struct_Asym.s_struct_asym)) {
            c = new Struct_Asym();
        } else if (categoryName.equalsIgnoreCase(Struct_Ref.s_struct_ref)) {
            c = new Struct_Ref();
        } else if (categoryName.equalsIgnoreCase(Struct_Ref_Seq.s_struct_ref_seq)) {
            c = new Struct_Ref_Seq();
        } else if (categoryName.equalsIgnoreCase(Struct_Ref_Seq_Dif.s_struct_ref_seq_dif)) {
            c = new Struct_Ref_Seq_Dif();
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Assembley.s_pdbx_struct_assembly)) {
            c = new PDBX_Struct_Assembley();
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Assembley_Gen.s_pdbx_struct_assembly_gen)) {
            c = new PDBX_Struct_Assembley_Gen();
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Assembley_Auth_Evidence.s_pdbx_struct_assembly_auth_evidence)) {
            c = new PDBX_Struct_Assembley_Auth_Evidence();
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Oper_List.s_pdbx_struct_oper_list)) {
            c = new PDBX_Struct_Oper_List();
            
            
        } else {
            throw new Exception("Unrecognised category name");
        }
        return c;
    }

    /**
     * For parsing a loop.
     */
    protected void parseLoop() {
        try {
            String line = reader.readLine();
            System.out.println(line);
            String[] parts = line.split("\\."); // Need to escape the dot
            String categoryName = parts[0].substring(1);
            Category category = getCategory(categoryName);
            // Initialise columns
            ArrayList<Variable_ID> ids = new ArrayList<>();
            addColumn(category, parts[1], ids);
            line = reader.readLine();
            System.out.println(line);
            while (line.startsWith(Mol_Strings.symbol_underscore)) {
                parts = line.split("\\."); // Need to escape the dot
                addColumn(category, parts[1], ids);
                line = reader.readLine();
                System.out.println(line);
            }
            // Initialise values
            while (!(line.startsWith(Mol_Strings.symbol_underscore)
                    || line.startsWith(Mol_Strings.SYMBOL_HASH))) {
                parts = line.split("\\s+"); // Need to escape the dot
                for (int i = 0; i < ids.size(); i++) {
                    Variable_ID id = ids.get(i);
                    Column column = (Column) category.variables.get(id);
                    column.values.add(parts[i]);
                }
                line = reader.readLine();
                System.out.println(line);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * @param category The Category.
     * @param name The name of the variable.
     * @param ids The ids to add to.
     */
    protected void addColumn(Category category, String name, ArrayList<Variable_ID> ids) {
        Column column = new Column(category, name);
        int key = cif.id2name.size();
        Variable_ID id = new Variable_ID(key);
        ids.add(id);
        cif.name2id.put(name, id);
        cif.id2name.put(id, name);
        category.setVariable(id, column);
        category.variables.put(id, column);
    }

}
