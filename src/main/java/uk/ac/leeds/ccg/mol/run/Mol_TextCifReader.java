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
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.data.format.Data_ReadCSV;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Files;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;
import uk.ac.leeds.ccg.mol.data.cif.CIF;
import uk.ac.leeds.ccg.mol.data.cif.Column;
import uk.ac.leeds.ccg.mol.data.cif.Column_ID;
import uk.ac.leeds.ccg.mol.data.cif.Columns;
import uk.ac.leeds.ccg.mol.data.cif.Columns_ID;
import uk.ac.leeds.ccg.mol.data.cif.Comment;
import uk.ac.leeds.ccg.mol.data.cif.DataBlock;
import uk.ac.leeds.ccg.mol.data.cif.DataBlockHeading;
import uk.ac.leeds.ccg.mol.data.cif.DataItem;
import uk.ac.leeds.ccg.mol.data.cif.DataItems;
import uk.ac.leeds.ccg.mol.data.cif.DataItems_ID;
import uk.ac.leeds.ccg.mol.data.cif.Variable_ID;
import uk.ac.leeds.ccg.mol.data.cif.Value;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Atom_Site;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Atom_Sites;
import uk.ac.leeds.ccg.mol.data.cif.columns.Audit_Author;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Audit_Conform;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Cell;
import uk.ac.leeds.ccg.mol.data.cif.columns.Chem_Comp;
import uk.ac.leeds.ccg.mol.data.cif.columns.Chem_Comp_Atom;
import uk.ac.leeds.ccg.mol.data.cif.columns.Chem_Comp_Bond;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Citation;
import uk.ac.leeds.ccg.mol.data.cif.columns.Citation_Author;
import uk.ac.leeds.ccg.mol.data.cif.columns.Database_2;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_3D_Fitting;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_3D_Reconstruction;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Admin;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Buffer;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_CTF_Correction;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Entity_Assembly;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Entity_Assembly_Molwt;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Entity_Assembly_NaturalSource;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Experiment;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Image_Processing;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Image_Recording;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Imaging;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Single_Particle_Entity;
import uk.ac.leeds.ccg.mol.data.cif.columns.EM_Software;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Specimen;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Vitrification;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Name_Com;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Poly;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Poly_Seq;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Src_Nat;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Entry;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Exptl;
import uk.ac.leeds.ccg.mol.data.cif.columns.NDB_Struct_Conf_NA;
import uk.ac.leeds.ccg.mol.data.cif.columns.NDB_Struct_NA_Base_Pair;
import uk.ac.leeds.ccg.mol.data.cif.columns.NDB_Struct_NA_Base_Pair_Step;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Audit_Revision_Category;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Audit_Revision_Details;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Audit_Revision_Group;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Audit_Revision_History;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Audit_Revision_Item;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Audit_Support;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Database_Related;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Database_Status;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Poly_Seq_Scheme;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Assembly;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Assembly_Auth_Evidence;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Assembly_Gen;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Oper_List;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Struct_Sheet_Hbond;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Unobs_or_Zero_Occ_Atoms;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Close_Contact;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Main_Chain_Plane;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Validate_Peptide;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Peptide_Omega;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Polymer_Linkage;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_RMSD_Angle;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_RMSD_Bond;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Torsion;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Struct;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Asym;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Conf;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Struct_Conf_Type;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Conn;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Struct_Conn_Type;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Struct_Keywords;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Mon_Prot_Cis;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Ref;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Ref_Seq;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Ref_Seq_Dif;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Sheet;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Sheet_Order;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Sheet_Range;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Symmetry;

/**
 *
 * @author Andy Turner
 */
public class Mol_TextCifReader {

    protected Data_ReadCSV reader;

    protected CIF cif;

    public final HashMap<Integer, String> padding;

    /**
     * Create a new instance.
     */
    public Mol_TextCifReader() {
        padding = new HashMap<>();
        String s = "";
        for (int i = 0; i < 80; i++) {
            padding.put(i, s);
            s += Mol_Strings.symbol_space;
        }
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

            String code = "6xu8";
            Path dir = Paths.get("C:", "Users", "geoagdt", "Downloads");
            Path p = Paths.get(dir.toString(), code + ".cif");

            // Read data
            // Set up reader
            BufferedReader br = Generic_IO.getBufferedReader(p);
            Generic_Files files = new Generic_Files(new Generic_Defaults());
            Mol_Environment env = new Mol_Environment(new Generic_Environment(files));
            ex.reader = new Data_ReadCSV(env);
            ex.reader.setStreamTokenizer(br, 10);
            // Initialise in memory store
            ex.cif = new CIF(env);
            DataBlock db = null;
            String line = ex.reader.readLine();
            //System.out.println(line);
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
                            ArrayList<String> values = ex.getValues(line);
                            String[] parts = values.get(0).split("\\."); // Need to escape the dot
                            String name = parts[0].substring(1);

                            if (name.equalsIgnoreCase(PDBX_Database_Status.NAME)) {
                                int debug = 1;
                            }

                            String vname = parts[1];
                            DataItems dataItems = db.getDataItems(name);
                            if (dataItems == null) {
                                DataItems_ID id = db.getNextDataItems_ID();
                                dataItems = new DataItems(name, id);
                                db.addDataItems(dataItems);
                            }
                            String value = "";
                            if (values.size() == 1) {
                                line = ex.reader.readLine();
                                //System.out.println(line);
                                if (line.startsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                    value += line.trim().substring(1);
                                } else {
                                    int debug = 1;
                                }
                            } else {
                                for (int i = 1; i < values.size(); i++) {
                                    value += values.get(i);
                                }
                            }
                            dataItems.add(new DataItem(dataItems, vname, value));
                        } else {
                            if (!line.trim().equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                int debug = 1;
                            }
                        }
                    }
                }
                line = ex.reader.readLine();
                //System.out.println(line);
            }

            // Print/write from the in memory representation.
            // Set up writer
            Path outp = Paths.get(dir.toString(), code + ".cif2");
            BufferedWriter bw = Generic_IO.getBufferedWriter(outp, false);

            ex.cif.dataBlocks.stream().forEach(x -> {
                try {
                    String s0 = x.dbh + Mol_Environment.EOL;
                    //System.out.print(s0);
                    bw.write(s0);
                    x.columnsAndDataItems.forEach(y -> {
                        try {
                            //System.out.println(Mol_Strings.SYMBOL_HASH);
                            bw.write(Mol_Strings.SYMBOL_HASH);
                            bw.write(Mol_Environment.EOL);
                            if (y instanceof Columns_ID id) {
                                //System.out.print(Mol_Strings.s_loop_);
                                bw.write(Mol_Strings.s_loop_);
                                bw.write(Mol_Environment.EOL);
                                Columns columns = x.getColumns(id);
                                // Header
                                columns.cols.keySet().forEach(cid -> {
                                    Column col = columns.cols.get(cid);
                                    try {
                                        String s2 = Mol_Strings.symbol_underscore
                                                + columns.name + Mol_Strings.symbol_dot
                                                + col.name + Mol_Environment.EOL;
                                        //System.out.print(s2);
                                        bw.write(s2);
                                    } catch (IOException ex1) {
                                        Logger.getLogger(Mol_TextCifReader.class.getName()).log(Level.SEVERE, null, ex1);
                                    }
                                });
                                // Values
                                int nrows = columns.getNRows();
                                int ncols = columns.getNCols();
                                for (int r = 0; r < nrows; r++) {
                                    StringBuilder sb = new StringBuilder();
                                    for (int c = 0; c < ncols; c++) {
                                        Column_ID cid = new Column_ID(c); // It would probably be better to look this up rather than create it each time.
                                        Column column = columns.cols.get(cid);

                                        int w = column.getWidth();
                                        Value v = columns.getValue(r, c);
                                        int sw = v.v.length();
                                        String pad = ex.padding.get(w - sw + 1);
                                        if (pad == null) {
                                            pad = " ";
                                        }
                                        String sbs = sb.toString();
                                        String[] sbss = sbs.split(Mol_Environment.EOL);
                                        if (sw > Mol_TextCifWriter.HEADER_LENGTH_MAX) {
                                            sb.append(Mol_Environment.EOL);
                                            sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                            String ss = v.v.substring(0, Mol_TextCifWriter.HEADER_LENGTH_MAX);
                                            sb.append(ss);
                                            sb.append(Mol_Environment.EOL);
                                            String sv = v.v.substring(Mol_TextCifWriter.HEADER_LENGTH_MAX, sw);
                                            while (sv.length() > Mol_TextCifWriter.HEADER_LENGTH_MAX) {
                                                ss = sv.substring(0, Mol_TextCifWriter.HEADER_LENGTH_MAX);
                                                sb.append(ss);
                                                sb.append(Mol_Environment.EOL);
                                                sv = sv.substring(Mol_TextCifWriter.HEADER_LENGTH_MAX, sv.length());
                                            }
                                            sb.append(sv);
                                            sb.append(Mol_Environment.EOL);
                                            sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                            if (columns.name.equalsIgnoreCase("struct_ref")) {
                                                sb.append(Mol_Environment.EOL);
                                            }
                                        } else {
                                            // Handle special cases:
                                            if (columns.name.equalsIgnoreCase("atom_site")) {
                                                sb.append(v.v);
                                            } else if (columns.name.equalsIgnoreCase("ndb_struct_na_base_pair_step") ||
                                                    columns.name.equalsIgnoreCase("struct_conn")) {
                                                if (sbss[sbss.length - 1].length() + sw > 130) {
                                                    sb.append(Mol_Environment.EOL);
                                                }
                                                sb.append(v.v);
                                            } else if (columns.name.equalsIgnoreCase("struct_ref")) {
                                                if (sw >= Mol_TextCifWriter.HEADER_LENGTH_MAX) {
                                                    sb.append(Mol_Environment.EOL);
                                                    sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                    sb.append(v.v);
                                                    sb.append(Mol_Environment.EOL);
                                                    sb.append(Mol_Environment.EOL);
                                                    sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                    sb.append(Mol_Environment.EOL);
                                                } else {
                                                    sb.append(v.v);
                                                }
                                            } else if (columns.name.equalsIgnoreCase("entity_poly")) {
                                                if (sbss[sbss.length - 1].length() > Mol_TextCifWriter.HEADER_LENGTH_MAX) {
                                                    if (sw > 10 || sbss[sbss.length - 1].length() > 120) {
                                                        sb.append(Mol_Environment.EOL);
                                                    }
                                                }
                                                if (column.name.equalsIgnoreCase("pdbx_strand_id")) {
                                                    if (sw + sbs.length() > Mol_TextCifWriter.HEADER_LENGTH_MAX) {
                                                        if (sbs.endsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                                            sb.append(Mol_Environment.EOL);
                                                        }
                                                    }
                                                }
                                                sb.append(v.v);
                                            } else if (columns.name.equalsIgnoreCase("citation_author") && column.name.equalsIgnoreCase("name")) {
                                                if (!v.v.startsWith("'")) {
                                                    sb.append(Mol_Environment.EOL);
                                                    sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                    sb.append(v.v);
                                                    sb.append(Mol_Environment.EOL);
                                                    sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                    sb.append(Mol_Environment.EOL);
                                                } else {
                                                    sb.append(v.v);
                                                }
                                            } else if (columns.name.equalsIgnoreCase("struct_ref")) {
                                                if (sbss[sbss.length - 1].length() > Mol_TextCifWriter.HEADER_LENGTH_MAX) {
                                                    if (sw > 10) {
                                                        sb.append(Mol_Environment.EOL);
                                                    }
                                                }
                                                sb.append(v.v);
                                            } else {
                                                sb.append(v.v);
                                            }
                                            if (c < ncols - 1) {
                                                sb.append(pad);
                                            }
                                        }
                                    }
                                    sb.append(Mol_Environment.EOL);
                                    //System.out.print(sb.toString());
                                    bw.write(sb.toString());
                                }
                            } else {
                                DataItems dataItems = x.getDataItems((DataItems_ID) y);
                                int nml = dataItems.getNameMaxLength();
                                //dataItems.dataItems.values().forEach(z -> {
                                dataItems.dataItems.keySet().forEach(z -> {
                                    try {
                                        DataItem d = dataItems.dataItems.get(z);
                                        String dv = d.value;
                                        String pad = ex.padding.get(nml - d.name.length() + 3);
                                        StringBuilder sb = new StringBuilder();
                                        sb.append(Mol_Strings.symbol_underscore);
                                        sb.append(dataItems.name);
                                        sb.append(Mol_Strings.symbol_dot);
                                        sb.append(d.name);
                                        // Handle special cases:
                                        if (dataItems.name.equalsIgnoreCase("em_entity_assembly")) {
                                            if (sb.toString().length() + pad.length() + dv.length() > 130) {
                                                sb.append(Mol_Environment.EOL);
                                                sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                sb.append(dv);
                                                sb.append(Mol_Environment.EOL);
                                                sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                sb.append(Mol_Environment.EOL);
                                            } else {
                                                sb.append(pad);
                                                sb.append(dv);
                                                sb.append(Mol_Environment.EOL);
                                            }
                                        } else if (dataItems.name.equalsIgnoreCase("pdbx_struct_assembly_gen")) {
                                            if (sb.toString().length() + pad.length() + dv.length() > Mol_TextCifWriter.HEADER_LENGTH_MAX) {
                                                sb.append(Mol_Environment.EOL);
                                                sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                sb.append(dv);
                                                sb.append(Mol_Environment.EOL);
                                                sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
                                                sb.append(Mol_Environment.EOL);
                                            } else {
                                                sb.append(pad);
                                                sb.append(dv);
                                                sb.append(Mol_Environment.EOL);
                                            }
                                        } else {
                                            sb.append(pad);
                                            sb.append(dv);
                                            sb.append(Mol_Environment.EOL);
                                        }

                                        //System.out.print(sb.toString());
                                        bw.write(sb.toString());
                                    } catch (IOException ex1) {
                                        Logger.getLogger(Mol_TextCifReader.class.getName()).log(Level.SEVERE, null, ex1);
                                        ex1.printStackTrace();
                                    }
                                });
                            }
                        } catch (IOException ex1) {
                            Logger.getLogger(Mol_TextCifReader.class.getName()).log(Level.SEVERE, null, ex1);
                            ex1.printStackTrace();
                        }
                    });
                } catch (IOException ex1) {
                    Logger.getLogger(Mol_TextCifReader.class.getName()).log(Level.SEVERE, null, ex1);
                    ex1.printStackTrace();
                }
            });
            //bw.write(Mol_Environment.EOL);
            bw.write(Mol_Strings.SYMBOL_HASH);
            //bw.write(Mol_Environment.EOL);
            bw.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * For initialising a DataItems instance using the given parameters.
     *
     * @param name The DataItems name.
     * @param id The DataItems_ID
     * @return a new DataItems.
     * @throws Exception if name not recognised.
     */
    protected DataItems getDataItems(String name, DataItems_ID id) throws Exception {
        DataItems r;
        if (name.equalsIgnoreCase(Entry.NAME)) {
            r = new Entry(id);
        } else if (name.equalsIgnoreCase(Audit_Conform.NAME)) {
            r = new Audit_Conform(id);
        } else if (name.equalsIgnoreCase(PDBX_Audit_Revision_Details.NAME)) {
            r = new PDBX_Audit_Revision_Details(id);
        } else if (name.equalsIgnoreCase(PDBX_Database_Status.NAME)) {
            r = new PDBX_Database_Status(id);
        } else if (name.equalsIgnoreCase(PDBX_Database_Related.NAME)) {
            r = new PDBX_Database_Related(id);
        } else if (name.equalsIgnoreCase(Citation.NAME)) {
            r = new Citation(id);
        } else if (name.equalsIgnoreCase(Cell.NAME)) {
            r = new Cell(id);
        } else if (name.equalsIgnoreCase(Symmetry.NAME)) {
            r = new Symmetry(id);
        } else if (name.equalsIgnoreCase(Exptl.NAME)) {
            r = new Exptl(id);
        } else if (name.equalsIgnoreCase(Struct.NAME)) {
            r = new Struct(id);
        } else if (name.equalsIgnoreCase(Struct_Keywords.NAME)) {
            r = new Struct_Keywords(id);
        } else if (name.equalsIgnoreCase(PDBX_Struct_Assembly.NAME)) {
            r = new PDBX_Struct_Assembly(id);
        } else if (name.equalsIgnoreCase(PDBX_Struct_Assembly_Gen.NAME)) {
            r = new PDBX_Struct_Assembly_Gen(id);
        } else if (name.equalsIgnoreCase(PDBX_Struct_Assembly_Auth_Evidence.NAME)) {
            r = new PDBX_Struct_Assembly_Auth_Evidence(id);
        } else if (name.equalsIgnoreCase(PDBX_Struct_Oper_List.NAME)) {
            r = new PDBX_Struct_Oper_List(id);
        } else if (name.equalsIgnoreCase(Struct_Conf_Type.NAME)) {
            r = new Struct_Conf_Type(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Peptide.NAME)) {
            r = new PDBX_Validate_Peptide(id);
        } else if (name.equalsIgnoreCase(Struct_Conn_Type.NAME)) {
            r = new Struct_Conn_Type(id);
        } else if (name.equalsIgnoreCase(EM_3D_Fitting.NAME)) {
            r = new EM_3D_Fitting(id);
        } else if (name.equalsIgnoreCase(EM_3D_Reconstruction.NAME)) {
            r = new EM_3D_Reconstruction(id);
        } else if (name.equalsIgnoreCase(EM_Admin.NAME)) {
            r = new EM_Admin(id);
        } else if (name.equalsIgnoreCase(EM_CTF_Correction.NAME)) {
            r = new EM_CTF_Correction(id);
        } else if (name.equalsIgnoreCase(EM_Entity_Assembly_Molwt.NAME)) {
            r = new EM_Entity_Assembly_Molwt(id);
        } else if (name.equalsIgnoreCase(EM_Entity_Assembly_NaturalSource.NAME)) {
            r = new EM_Entity_Assembly_NaturalSource(id);
        } else if (name.equalsIgnoreCase(EM_Image_Processing.NAME)) {
            r = new EM_Image_Processing(id);
        } else if (name.equalsIgnoreCase(EM_Image_Recording.NAME)) {
            r = new EM_Image_Recording(id);
        } else if (name.equalsIgnoreCase(EM_Specimen.NAME)) {
            r = new EM_Specimen(id);
        } else if (name.equalsIgnoreCase(EM_Buffer.NAME)) {
            r = new EM_Buffer(id);
        } else if (name.equalsIgnoreCase(EM_Entity_Assembly.NAME)) {
            r = new EM_Entity_Assembly(id);
        } else if (name.equalsIgnoreCase(EM_Imaging.NAME)) {
            r = new EM_Imaging(id);
        } else if (name.equalsIgnoreCase(EM_Vitrification.NAME)) {
            r = new EM_Vitrification(id);
        } else if (name.equalsIgnoreCase(EM_Experiment.NAME)) {
            r = new EM_Experiment(id);
        } else if (name.equalsIgnoreCase(EM_Single_Particle_Entity.NAME)) {
            r = new EM_Single_Particle_Entity(id);
        } else if (name.equalsIgnoreCase(Atom_Sites.NAME)) {
            r = new Atom_Sites(id);
        } else if (name.equalsIgnoreCase(Atom_Sites.NAME)) {
            r = new Atom_Site(id);
        } else {
            throw new Exception("Unrecognised DataItems name " + name);
        }
        return r;
    }

    /**
     * For initialising a Columns instance using the given parameters.
     *
     * @param name The Columns name.
     * @param id The Columns_ID
     * @return A new Columns.
     * @throws Exception if name not recognised.
     */
    protected Columns getColumns(String name, Columns_ID id) throws Exception {
        Columns r;
        if (name.equalsIgnoreCase(Database_2.NAME)) {
            r = new Database_2(id);
        } else if (name.equalsIgnoreCase(PDBX_Audit_Revision_History.NAME)) {
            r = new PDBX_Audit_Revision_History(id);
        } else if (name.equalsIgnoreCase(PDBX_Audit_Revision_Group.NAME)) {
            r = new PDBX_Audit_Revision_Group(id);
        } else if (name.equalsIgnoreCase(PDBX_Audit_Revision_Category.NAME)) {
            r = new PDBX_Audit_Revision_Category(id);
        } else if (name.equalsIgnoreCase(PDBX_Audit_Revision_Item.NAME)) {
            r = new PDBX_Audit_Revision_Item(id);
        } else if (name.equalsIgnoreCase(Audit_Author.NAME)) {
            r = new Audit_Author(id);
        } else if (name.equalsIgnoreCase(Citation_Author.NAME)) {
            r = new Citation_Author(id);
        } else if (name.equalsIgnoreCase(Entity.NAME)) {
            r = new Entity(id);
        } else if (name.equalsIgnoreCase(Entity_Name_Com.NAME)) {
            r = new Entity_Name_Com(id);
        } else if (name.equalsIgnoreCase(Entity_Poly.NAME)) {
            r = new Entity_Poly(id);
        } else if (name.equalsIgnoreCase(Entity_Poly_Seq.NAME)) {
            r = new Entity_Poly_Seq(id);
        } else if (name.equalsIgnoreCase(Entity_Src_Nat.NAME)) {
            r = new Entity_Src_Nat(id);
        } else if (name.equalsIgnoreCase(Chem_Comp.NAME)) {
            r = new Chem_Comp(id);
        } else if (name.equalsIgnoreCase(PDBX_Poly_Seq_Scheme.NAME)) {
            r = new PDBX_Poly_Seq_Scheme(id);
        } else if (name.equalsIgnoreCase(PDBX_Unobs_or_Zero_Occ_Atoms.NAME)) {
            r = new PDBX_Unobs_or_Zero_Occ_Atoms(id);
        } else if (name.equalsIgnoreCase(Struct_Asym.NAME)) {
            r = new Struct_Asym(id);
        } else if (name.equalsIgnoreCase(Struct_Ref.NAME)) {
            r = new Struct_Ref(id);
        } else if (name.equalsIgnoreCase(Struct_Ref_Seq.NAME)) {
            r = new Struct_Ref_Seq(id);
        } else if (name.equalsIgnoreCase(Struct_Ref_Seq_Dif.NAME)) {
            r = new Struct_Ref_Seq_Dif(id);
        } else if (name.equalsIgnoreCase(Struct_Conf.NAME)) {
            r = new Struct_Conf(id);
        } else if (name.equalsIgnoreCase(Struct_Conn.NAME)) {
            r = new Struct_Conn(id);
        } else if (name.equalsIgnoreCase(Struct_Mon_Prot_Cis.NAME)) {
            r = new Struct_Mon_Prot_Cis(id);
        } else if (name.equalsIgnoreCase(Struct_Sheet.NAME)) {
            r = new Struct_Sheet(id);
        } else if (name.equalsIgnoreCase(Struct_Sheet_Order.NAME)) {
            r = new Struct_Sheet_Order(id);
        } else if (name.equalsIgnoreCase(Struct_Sheet_Range.NAME)) {
            r = new Struct_Sheet_Range(id);
        } else if (name.equalsIgnoreCase(PDBX_Struct_Sheet_Hbond.NAME)) {
            r = new PDBX_Struct_Sheet_Hbond(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Close_Contact.NAME)) {
            r = new PDBX_Validate_Close_Contact(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_RMSD_Bond.NAME)) {
            r = new PDBX_Validate_RMSD_Bond(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_RMSD_Angle.NAME)) {
            r = new PDBX_Validate_RMSD_Angle(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Torsion.NAME)) {
            r = new PDBX_Validate_Torsion(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Peptide_Omega.NAME)) {
            r = new PDBX_Validate_Peptide_Omega(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Main_Chain_Plane.NAME)) {
            r = new PDBX_Validate_Main_Chain_Plane(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Polymer_Linkage.NAME)) {
            r = new PDBX_Validate_Polymer_Linkage(id);
        } else if (name.equalsIgnoreCase(Chem_Comp_Atom.NAME)) {
            r = new Chem_Comp_Atom(id);
        } else if (name.equalsIgnoreCase(Chem_Comp_Bond.NAME)) {
            r = new Chem_Comp_Bond(id);
        } else if (name.equalsIgnoreCase(EM_Software.NAME)) {
            r = new EM_Software(id);
        } else if (name.equalsIgnoreCase(NDB_Struct_Conf_NA.NAME)) {
            r = new NDB_Struct_Conf_NA(id);
        } else if (name.equalsIgnoreCase(NDB_Struct_NA_Base_Pair.NAME)) {
            r = new NDB_Struct_NA_Base_Pair(id);
        } else if (name.equalsIgnoreCase(NDB_Struct_NA_Base_Pair_Step.NAME)) {
            r = new NDB_Struct_NA_Base_Pair_Step(id);
        } else if (name.equalsIgnoreCase(PDBX_Audit_Support.NAME)) {
            r = new PDBX_Audit_Support(id);
        } else {
            throw new Exception("Unrecognised Columns name " + name);
        }
        return r;
    }

    /**
     * For parsing a loop.
     *
     * @param db The DataBlock
     */
    protected void parseLoop(DataBlock db) {
        try {
            String line = reader.readLine();
            //System.out.println(line);

            String[] parts = line.split("\\."); // Need to escape the dot

            if (parts.length == 0) {
                int debug = 1;
            }

            if (parts[0].length() == 0) {
                int debug = 1;
            }

            // Initialise columnss
            Columns columns = new Columns(parts[0].substring(1),
                    db.getNextColumns_ID());
            db.addColumns(columns);
            // Initialise Columns
            while (line.startsWith(Mol_Strings.symbol_underscore)) {
                parts = line.split("\\."); // Need to escape the dot

                if (parts.length < 2) {
                    int debug = 1;
                }

                columns.cols.put(new Column_ID(columns.cols.size()),
                        new Column(columns, parts[1].trim()));
                line = reader.readLine();
                //System.out.println(line);
            }

            // Add values
            int row = 0;
            while (!(line.startsWith(Mol_Strings.symbol_underscore)
                    || line.startsWith(Mol_Strings.SYMBOL_HASH))) {
                ArrayList<String> values = getValues(line);

                while (values.size() != columns.cols.size()) {
                    /**
                     * This may occur where the next variable is on the next
                     * line which should start with ";".
                     */
                    String line2 = reader.readLine();
                    //System.out.println(line2);
                    if (line2.startsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
                        Column_ID cid = new Column_ID(values.size());
                        Column column = columns.cols.get(cid);
                        if (column.name.equalsIgnoreCase("name")) {
                            values.add(line2.substring(1)); // Strip off the semi-colon.
                            String line3 = reader.readLine();
                            //System.out.println(line3);
                            if (line3.equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                String line4 = reader.readLine();
                                //System.out.println(line4);
                                values.addAll(getValues(line4));

                                if (values.size() != columns.cols.size()) {
                                    int debug = 1;
                                }

                            }
                        } else if (column.name.equalsIgnoreCase("pdbx_seq_one_letter_code")
                                || column.name.equalsIgnoreCase("pdbx_seq_one_letter_code_can")) {
                            StringBuilder sb2 = new StringBuilder(line2.substring(1));
                            if (!line2.contains("\\s+") && line2.length() == 81) {
                                readMultiLine(sb2);
                            }
                            values.add(sb2.toString());
                        } else {
                            String line3 = reader.readLine().trim();
                            //System.out.println(line3);
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
                columns.addRow(values);
                line = reader.readLine();
                //System.out.println(line);
                row++;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    protected void readMultiLine(StringBuilder sb) throws IOException {
        String line = reader.readLine().trim();
        //System.out.println(line);
        while (!line.equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
            sb.append(line);
            line = reader.readLine().trim();
            //System.out.println(line);
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
