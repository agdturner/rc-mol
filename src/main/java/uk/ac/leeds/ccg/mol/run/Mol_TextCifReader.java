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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import uk.ac.leeds.ccg.data.format.Data_ReadCSV;
import uk.ac.leeds.ccg.data.format.Data_ReadTXT;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Files;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;
import uk.ac.leeds.ccg.mol.data.cif.CIF;
import uk.ac.leeds.ccg.mol.data.cif.Category;
import uk.ac.leeds.ccg.mol.data.cif.Category_ID;
import uk.ac.leeds.ccg.mol.data.cif.Column;
import uk.ac.leeds.ccg.mol.data.cif.Column_ID;
import uk.ac.leeds.ccg.mol.data.cif.Columns;
import uk.ac.leeds.ccg.mol.data.cif.Columns_ID;
import uk.ac.leeds.ccg.mol.data.cif.Comment;
import uk.ac.leeds.ccg.mol.data.cif.DataBlock;
import uk.ac.leeds.ccg.mol.data.cif.DataBlockHeading;
import uk.ac.leeds.ccg.mol.data.cif.DataItem;
import uk.ac.leeds.ccg.mol.data.cif.DataItems_ID;
import uk.ac.leeds.ccg.mol.data.cif.Variable_ID;
import uk.ac.leeds.ccg.mol.data.cif.Value;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Atom_Site;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Atom_Sites;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Audit_Author;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Audit_Conform;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Cell;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Chem_Comp;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Chem_Comp_Atom;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Chem_Comp_Bond;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Citation;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Citation_Author;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Database_2;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_3D_Fitting;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_3D_Reconstruction;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Admin;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Buffer;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_CTF_Correction;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Entity_Assembly;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Entity_Assembly_Molwt;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Entity_Assembly_NaturalSource;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Experiment;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Image_Processing;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Image_Recording;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Imaging;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Single_Particle_Entity;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Software;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Specimen;
import uk.ac.leeds.ccg.mol.data.mmcif.category.EM_Vitrification;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Name_Com;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Poly;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Poly_Seq;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entity_Src_Nat;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Entry;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Exptl;
import uk.ac.leeds.ccg.mol.data.mmcif.category.NDB_Struct_Conf_NA;
import uk.ac.leeds.ccg.mol.data.mmcif.category.NDB_Struct_NA_Base_Pair;
import uk.ac.leeds.ccg.mol.data.mmcif.category.NDB_Struct_NA_Base_Pair_Step;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Category;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Details;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Group;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_History;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Revision_Item;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Audit_Support;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Database_Related;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Database_Status;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Poly_Seq_Scheme;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Assembly;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Assembly_Auth_Evidence;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Assembly_Gen;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Oper_List;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Struct_Sheet_Hbond;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Unobs_or_Zero_Occ_Atoms;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_Close_Contact;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_Main_Chain_Plane;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_Peptide;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_Peptide_Omega;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_Polymer_Linkage;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_RMSD_Angle;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_RMSD_Bond;
import uk.ac.leeds.ccg.mol.data.mmcif.category.PDBX_Validate_Torsion;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Asym;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Conf;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Conf_Type;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Conn;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Conn_Type;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Keywords;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Mon_Prot_Cis;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Ref;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Ref_Seq;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Ref_Seq_Dif;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Sheet;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Sheet_Order;
import uk.ac.leeds.ccg.mol.data.mmcif.category.Struct_Sheet_Range;
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

            DataBlock db = null;
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
                        // Columnss
                        ex.parseLoop(db);
                    } else {
                        // DataItemss
                        if (line.startsWith(Mol_Strings.symbol_underscore)) {
                            String[] parts = line.split("\\."); // Need to escape the dot
                            String categoryName = parts[0].substring(1);
                            Category category = ex.getCategory(categoryName, new DataItems_ID(db.dataItemsId2DataItemsName.size()));
                            ArrayList<String> nameAndValue = ex.getValues(parts[1]);
                            ArrayList<DataItem> dataItems = new ArrayList<>();
                            DataItems_ID id = new DataItems_ID(db.dataItemsId2DataItemsName.size());
                            db.dataItemsName2DataItemsId.put(nameAndValue.get(0), id);
                            db.dataItemsId2DataItemsName.put(id, nameAndValue.get(0));
                            DataItem di;
                            if (nameAndValue.size() > 2) {
                                String value = "";
                                for (int i = 1; i < nameAndValue.size(); i++) {
                                    value += nameAndValue.get(i);
                                }
                                di = new DataItem(category, nameAndValue.get(0), value);
                            } else if (nameAndValue.size() == 2) {
                                di = new DataItem(category, nameAndValue.get(0), nameAndValue.get(1));
                            } else {
                                line = ex.reader.readLine();
                                System.out.println(line);
                                if (line.startsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                    di = new DataItem(category, nameAndValue.get(0), line.substring(0));
                                } else {
                                    throw new Exception("Missing value");
                                }
                            }
                            dataItems.add(di);
                        }
                    }
                }
                line = ex.reader.readLine();
                System.out.println(line);
            }
            int debug = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    protected Category getCategory(String categoryName, Category_ID id) throws Exception {
        Category c;
        if (categoryName.equalsIgnoreCase(Entry.NAME)) {
            c = new Entry(id);
        } else if (categoryName.equalsIgnoreCase(Audit_Conform.NAME)) {
            c = new Audit_Conform(id);
        } else if (categoryName.equalsIgnoreCase(Database_2.NAME)) {
            c = new Database_2(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_History.NAME)) {
            c = new PDBX_Audit_Revision_History(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Details.NAME)) {
            c = new PDBX_Audit_Revision_Details(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Group.NAME)) {
            c = new PDBX_Audit_Revision_Group(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Category.NAME)) {
            c = new PDBX_Audit_Revision_Category(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Revision_Item.NAME)) {
            c = new PDBX_Audit_Revision_Item(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Database_Status.NAME)) {
            c = new PDBX_Database_Status(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Database_Related.NAME)) {
            c = new PDBX_Database_Related(id);
        } else if (categoryName.equalsIgnoreCase(Audit_Author.NAME)) {
            c = new Audit_Author(id);
        } else if (categoryName.equalsIgnoreCase(Citation.NAME)) {
            c = new Citation(id);
        } else if (categoryName.equalsIgnoreCase(Citation_Author.NAME)) {
            c = new Citation_Author(id);
        } else if (categoryName.equalsIgnoreCase(Entity.NAME)) {
            c = new Entity(id);
        } else if (categoryName.equalsIgnoreCase(Entity_Name_Com.NAME)) {
            c = new Entity_Name_Com(id);
        } else if (categoryName.equalsIgnoreCase(Entity_Poly.NAME)) {
            c = new Entity_Poly(id);
        } else if (categoryName.equalsIgnoreCase(Entity_Poly_Seq.NAME)) {
            c = new Entity_Poly_Seq(id);
        } else if (categoryName.equalsIgnoreCase(NDB_Struct_Conf_NA.NAME)) {
            c = new NDB_Struct_Conf_NA(id);
        } else if (categoryName.equalsIgnoreCase(NDB_Struct_NA_Base_Pair.NAME)) {
            c = new NDB_Struct_NA_Base_Pair(id);
        } else if (categoryName.equalsIgnoreCase(NDB_Struct_NA_Base_Pair_Step.NAME)) {
            c = new NDB_Struct_NA_Base_Pair_Step(id);

        } else if (categoryName.equalsIgnoreCase(Entity_Src_Nat.NAME)) {
            c = new Entity_Src_Nat(id);
        } else if (categoryName.equalsIgnoreCase(Chem_Comp.NAME)) {
            c = new Chem_Comp(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Poly_Seq_Scheme.NAME)) {
            c = new PDBX_Poly_Seq_Scheme(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Unobs_or_Zero_Occ_Atoms.NAME)) {
            c = new PDBX_Unobs_or_Zero_Occ_Atoms(id);
        } else if (categoryName.equalsIgnoreCase(Cell.NAME)) {
            c = new Cell(id);
        } else if (categoryName.equalsIgnoreCase(Symmetry.NAME)) {
            c = new Symmetry(id);
        } else if (categoryName.equalsIgnoreCase(Exptl.NAME)) {
            c = new Exptl(id);
        } else if (categoryName.equalsIgnoreCase(Struct.NAME)) {
            c = new Struct(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Keywords.NAME)) {
            c = new Struct_Keywords(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Asym.NAME)) {
            c = new Struct_Asym(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Ref.NAME)) {
            c = new Struct_Ref(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Ref_Seq.NAME)) {
            c = new Struct_Ref_Seq(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Ref_Seq_Dif.NAME)) {
            c = new Struct_Ref_Seq_Dif(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Assembly.NAME)) {
            c = new PDBX_Struct_Assembly(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Assembly_Gen.NAME)) {
            c = new PDBX_Struct_Assembly_Gen(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Assembly_Auth_Evidence.NAME)) {
            c = new PDBX_Struct_Assembly_Auth_Evidence(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Oper_List.NAME)) {
            c = new PDBX_Struct_Oper_List(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Conf.NAME)) {
            c = new Struct_Conf(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Conf_Type.NAME)) {
            c = new Struct_Conf_Type(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Conn.NAME)) {
            c = new Struct_Conn(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Conn_Type.NAME)) {
            c = new Struct_Conn_Type(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Mon_Prot_Cis.NAME)) {
            c = new Struct_Mon_Prot_Cis(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Sheet.NAME)) {
            c = new Struct_Sheet(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Sheet_Order.NAME)) {
            c = new Struct_Sheet_Order(id);
        } else if (categoryName.equalsIgnoreCase(Struct_Sheet_Range.NAME)) {
            c = new Struct_Sheet_Range(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Struct_Sheet_Hbond.NAME)) {
            c = new PDBX_Struct_Sheet_Hbond(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_Close_Contact.NAME)) {
            c = new PDBX_Validate_Close_Contact(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_RMSD_Bond.NAME)) {
            c = new PDBX_Validate_RMSD_Bond(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_RMSD_Angle.NAME)) {
            c = new PDBX_Validate_RMSD_Angle(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_Torsion.NAME)) {
            c = new PDBX_Validate_Torsion(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_Peptide.NAME)) {
            c = new PDBX_Validate_Peptide(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_Peptide_Omega.NAME)) {
            c = new PDBX_Validate_Peptide_Omega(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_Main_Chain_Plane.NAME)) {
            c = new PDBX_Validate_Main_Chain_Plane(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Validate_Polymer_Linkage.NAME)) {
            c = new PDBX_Validate_Polymer_Linkage(id);
        } else if (categoryName.equalsIgnoreCase(EM_3D_Fitting.NAME)) {
            c = new EM_3D_Fitting(id);
        } else if (categoryName.equalsIgnoreCase(EM_3D_Reconstruction.NAME)) {
            c = new EM_3D_Reconstruction(id);
        } else if (categoryName.equalsIgnoreCase(Chem_Comp_Atom.NAME)) {
            c = new Chem_Comp_Atom(id);
        } else if (categoryName.equalsIgnoreCase(Chem_Comp_Bond.NAME)) {
            c = new Chem_Comp_Bond(id);
        } else if (categoryName.equalsIgnoreCase(EM_Admin.NAME)) {
            c = new EM_Admin(id);
        } else if (categoryName.equalsIgnoreCase(EM_CTF_Correction.NAME)) {
            c = new EM_CTF_Correction(id);
        } else if (categoryName.equalsIgnoreCase(EM_Entity_Assembly_Molwt.NAME)) {
            c = new EM_Entity_Assembly_Molwt(id);
        } else if (categoryName.equalsIgnoreCase(EM_Entity_Assembly_NaturalSource.NAME)) {
            c = new EM_Entity_Assembly_NaturalSource(id);
        } else if (categoryName.equalsIgnoreCase(EM_Image_Processing.NAME)) {
            c = new EM_Image_Processing(id);
        } else if (categoryName.equalsIgnoreCase(EM_Image_Recording.NAME)) {
            c = new EM_Image_Recording(id);
        } else if (categoryName.equalsIgnoreCase(EM_Software.NAME)) {
            c = new EM_Software(id);
        } else if (categoryName.equalsIgnoreCase(EM_Specimen.NAME)) {
            c = new EM_Specimen(id);
        } else if (categoryName.equalsIgnoreCase(EM_Buffer.NAME)) {
            c = new EM_Buffer(id);
        } else if (categoryName.equalsIgnoreCase(EM_Entity_Assembly.NAME)) {
            c = new EM_Entity_Assembly(id);
        } else if (categoryName.equalsIgnoreCase(EM_Imaging.NAME)) {
            c = new EM_Imaging(id);
        } else if (categoryName.equalsIgnoreCase(EM_Vitrification.NAME)) {
            c = new EM_Vitrification(id);
        } else if (categoryName.equalsIgnoreCase(EM_Experiment.NAME)) {
            c = new EM_Experiment(id);
        } else if (categoryName.equalsIgnoreCase(EM_Single_Particle_Entity.NAME)) {
            c = new EM_Single_Particle_Entity(id);
        } else if (categoryName.equalsIgnoreCase(PDBX_Audit_Support.NAME)) {
            c = new PDBX_Audit_Support(id);
        } else if (categoryName.equalsIgnoreCase(Atom_Sites.NAME)) {
            c = new Atom_Sites(id);
        } else if (categoryName.equalsIgnoreCase(Atom_Site.NAME)) {
            c = new Atom_Site(id);

        } else {
            throw new Exception("Unrecognised category name");
        }
        return c;
    }

    /**
     * For parsing a loop.
     *
     * @param db The DataBlock
     */
    protected void parseLoop(DataBlock db) {
        try {
            String line = reader.readLine();
            System.out.println(line);

            String[] parts = line.split("\\."); // Need to escape the dot

            if (parts.length == 0) {
                int debug = 1;
            }

            if (parts[0].length() == 0) {
                int debug = 1;
            }

            // Initialise columnss
            Columns columns = new Columns(parts[0].substring(1),
                    new Columns_ID(db.columnsId2ColumnsName.size()));
            db.columnss.add(columns);
            // Initialise Columns
            while (line.startsWith(Mol_Strings.symbol_underscore)) {
                parts = line.split("\\."); // Need to escape the dot

                if (parts.length < 2) {
                    int debug = 1;
                }

                columns.cols.add(new Column(columns, parts[1].trim()));
                line = reader.readLine();
                System.out.println(line);
            }

            // Add values
            while (!(line.startsWith(Mol_Strings.symbol_underscore)
                    || line.startsWith(Mol_Strings.SYMBOL_HASH))) {
                ArrayList<String> values = getValues(line);

                while (values.size() != columns.cols.size()) {
                    /**
                     * This may occur where the next variable is on the next
                     * line which should start with ";".
                     */
                    String line2 = reader.readLine();
                    System.out.println(line2);
                    if (line2.startsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
                        if (columns.cols.get(values.size()).name.equalsIgnoreCase("name")) {
                            values.add(line2.substring(1)); // Strip off the semi-colon.
                            String line3 = reader.readLine();
                            System.out.println(line3);
                            if (line3.equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                String line4 = reader.readLine();
                                System.out.println(line4);
                                values.addAll(getValues(line4));
                                
                                if (values.size() != columns.cols.size()) {
                                    int debug = 1;
                                }
                                
                            }
                        } else if (columns.cols.get(values.size()).name.equalsIgnoreCase("pdbx_seq_one_letter_code") || 
                                columns.cols.get(values.size()).name.equalsIgnoreCase("pdbx_seq_one_letter_code_can")) {
                                StringBuilder sb2 = new StringBuilder(line2.substring(1));
                                if (!line2.contains("\\s+") && line2.length() == 81) {
                                    readMultiLine(sb2);
                                }
                                values.add(sb2.toString());
                        } else {
                            String line3 = reader.readLine().trim();
                            System.out.println(line3);
                            values.add(line3);                            
                        }
                    } else {
                        // There are cases where there is no line continuation symbol!
                        values.addAll(getValues(line2));
                        if (values.size() != columns.cols.size()) {
                            int debug = 1;
                        }
                    }
                }

                for (int col = 0; col < columns.cols.size(); col++) {
                    Column column = columns.cols.get(col);
                    column.values.add(new Value(values.get(col)));
                }
                line = reader.readLine();
                System.out.println(line);
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    protected void readMultiLine(StringBuilder sb) throws IOException {
        String line = reader.readLine().trim();
        System.out.println(line);
        while (!line.equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
            sb.append(line);
            line = reader.readLine().trim();
            System.out.println(line);
        }
    }

    public char delimiter = ' ';
    public String delimiter_s = String.valueOf(delimiter);

    /**
     * @param line The line to get the values from.
     * @return An ArrayList of String values.
     */
    public ArrayList<String> getValues(String line) {
        // Replace all white space with a single space.
        String l = line.replaceAll("\\s+", delimiter_s).trim();
        return Data_ReadCSV.parseLine(l, delimiter);
    }

//    /**
//     * @param name The name of the variable.
//     * @param ids The ids to add to.
//     */
//    protected void addColumn(Category category, String name, ArrayList<Variable_ID> ids) {
//        Column column = new Column(category, name);
//        int key = cif.id2name.size();
//        Variable_ID id = new Variable_ID(key);
//        ids.add(id);
//        cif.name2id.put(name, id);
//        cif.id2name.put(id, name);
//        category.setVariable(id, column);
//        category.variables.put(id, column);
//    }
}
