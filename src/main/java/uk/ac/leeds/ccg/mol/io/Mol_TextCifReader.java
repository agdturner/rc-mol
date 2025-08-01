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
package uk.ac.leeds.ccg.mol.io;

import ch.obermuhlner.math.big.BigRational;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.data.format.Data_ReadCSV;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Files;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.math.arithmetic.Math_BigRational;
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
import uk.ac.leeds.ccg.mol.data.cif.Row_ID;
import uk.ac.leeds.ccg.mol.data.cif.Variable_ID;
import uk.ac.leeds.ccg.mol.data.cif.Value;
import uk.ac.leeds.ccg.mol.data.cif.columns.Atom_Site;
import uk.ac.leeds.ccg.mol.data.cif.columns.Atom_Type;
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
import uk.ac.leeds.ccg.mol.data.cif.columns.Database_PDB_Caveat;
import uk.ac.leeds.ccg.mol.data.cif.columns.EM_Entity_Assembly_Recombinant;
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
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Src_Gen;
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
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Entity_NonPoly;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Modification_Feature;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_NonPoly_Scheme;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Database_Related;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Database_Status;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Poly_Seq_Scheme;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Struct_Conn_Angle;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Assembly;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Assembly_Auth_Evidence;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Assembly_Gen;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Oper_List;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Struct_Sheet_Hbond;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Unobs_Or_Zero_Occ_Residues;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Unobs_or_Zero_Occ_Atoms;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Chiral;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Close_Contact;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Main_Chain_Plane;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Validate_Peptide;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Peptide_Omega;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Validate_Planes;
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
import uk.ac.leeds.ccg.mol.data.cif.data_items.Database_PDB_Matrix;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_3D_Fitting_List;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Sample_Support;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Contact_Author;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Entity_Instance_Feature;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Entry_Details;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Initial_Refinement_Model;
import uk.ac.leeds.ccg.mol.data.cif.data_items.Symmetry;

/**
 *
 * @author Andy Turner
 */
public class Mol_TextCifReader {

    protected Data_ReadCSV reader;

    /**
     * Create a new instance.
     */
    public Mol_TextCifReader() {
    }

    /**
     * https://files.rcsb.org/download/4ug0.cif
     * https://files.rcsb.org/download/6xu8.cif
     *
     * @param pdbId e.g. 4ug0, 6xu8
     * @param dir = Paths.get("C:", "Users", "geoagdt", "Downloads");
     * @return
     */
    public CIF getCif(String pdbId, Path dir) {
        CIF cif = null;
        try {
            //boolean parseBinary = true;
            boolean parseBinary = false;
            System.out.println("Load " + pdbId + " parseBinary " + Boolean.toString(parseBinary));
            Path p = Paths.get(dir.toString(), pdbId + ".cif");

            // Read data
            // Set up reader
            BufferedReader br = Generic_IO.getBufferedReader(p);
            Generic_Files files = new Generic_Files(new Generic_Defaults());
            Mol_Environment env = new Mol_Environment(new Generic_Environment(files));
            reader = new Data_ReadCSV(env);
            reader.setStreamTokenizer(br, 10);
            // Initialise in memory store
            cif = new CIF(env);
            DataBlock db = null;
            String line = reader.readLine();
            //System.out.println(line);
            while (line != null) {
                if (line.startsWith(Mol_Strings.SYMBOL_HASH)) {
                    cif.comments.add(new Comment(line.split(Mol_Strings.SYMBOL_HASH)[1]));
                } else if (line.startsWith(Mol_Strings.s_data_)) {
                    DataBlockHeading dbh = new DataBlockHeading(env,
                            line.split(Mol_Strings.s_data_)[1]);
                    db = new DataBlock(env, dbh);
                    cif.dataBlocks.add(db);
                } else {
                    if (line.startsWith(Mol_Strings.s_loop_)) {
                        // Columnss
                        parseLoop(db);
                    } else {
                        // DataItemss
                        if (line.startsWith(Mol_Strings.symbol_underscore)) {
                            ArrayList<String> values = getValues(line);
                            String[] parts = values.get(0).split("\\."); // Need to escape the dot
                            String name = parts[0].substring(1);

                            if (name.equalsIgnoreCase(PDBX_Database_Status.NAME)) {
                                int debug = 1;
                            }

                            String vname = parts[1];
                            DataItems dataItems = db.getDataItems(name);
                            if (dataItems == null) {
                                DataItems_ID id = db.getNextDataItems_ID();
                                //dataItems = new DataItems(name, id);
                                dataItems = getDataItems(name, id);
                                db.addDataItems(dataItems);
                            }
                            String value = "";
                            if (values.size() == 1) {
                                line = reader.readLine();
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
                line = reader.readLine();
                //System.out.println(line);
            }

            //cif.centralise();
            //Mol_TextCifWriter writer = new Mol_TextCifWriter();
            //writer.write(cif, dir, pdbId);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return cif;
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
        } else if (name.equalsIgnoreCase(PDBX_Contact_Author.NAME)) {
            r = new PDBX_Contact_Author(id);
        } else if (name.equalsIgnoreCase(Citation.NAME)) {
            r = new Citation(id);
        } else if (name.equalsIgnoreCase(PDBX_Entity_Instance_Feature.NAME)) {
            r = new PDBX_Entity_Instance_Feature(id);
        } else if (name.equalsIgnoreCase(Cell.NAME)) {
            r = new Cell(id);
        } else if (name.equalsIgnoreCase(Symmetry.NAME)) {
            r = new Symmetry(id);
        } else if (name.equalsIgnoreCase(Exptl.NAME)) {
            r = new Exptl(id);
        } else if (name.equalsIgnoreCase(Database_PDB_Matrix.NAME)) {
            r = new Database_PDB_Matrix(id);
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
        } else if (name.equalsIgnoreCase(PDBX_Entry_Details.NAME)) {
            r = new PDBX_Entry_Details(id);
        } else if (name.equalsIgnoreCase(uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Validate_Planes.NAME)) {
            r = new uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Validate_Planes(id);    
        } else if (name.equalsIgnoreCase(PDBX_Validate_Peptide.NAME)) {
            r = new PDBX_Validate_Peptide(id);
        } else if (name.equalsIgnoreCase(Struct_Conn_Type.NAME)) {
            r = new Struct_Conn_Type(id);
        } else if (name.equalsIgnoreCase(PDBX_Modification_Feature.NAME)) {
            r = new PDBX_Modification_Feature(id);
        } else if (name.equalsIgnoreCase(EM_3D_Fitting.NAME)) {
            r = new EM_3D_Fitting(id);
        } else if (name.equalsIgnoreCase(EM_3D_Fitting_List.NAME)) {
            r = new EM_3D_Fitting_List(id);
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
        } else if (name.equalsIgnoreCase(EM_Sample_Support.NAME)) {
            r = new EM_Sample_Support(id);
        } else if (name.equalsIgnoreCase(EM_Vitrification.NAME)) {
            r = new EM_Vitrification(id);
        } else if (name.equalsIgnoreCase(EM_Experiment.NAME)) {
            r = new EM_Experiment(id);
        } else if (name.equalsIgnoreCase(EM_Single_Particle_Entity.NAME)) {
            r = new EM_Single_Particle_Entity(id);
        } else if (name.equalsIgnoreCase(uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Audit_Support.NAME)) {
            r = new uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Audit_Support(id);
        } else if (name.equalsIgnoreCase(PDBX_Initial_Refinement_Model.NAME)) {
            r = new PDBX_Initial_Refinement_Model(id);
        } else if (name.equalsIgnoreCase(Atom_Sites.NAME)) {
            r = new Atom_Sites(id);
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
        } else if (name.equalsIgnoreCase(Database_PDB_Caveat.NAME)) {
            r = new Database_PDB_Caveat(id);
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
        } else if (name.equalsIgnoreCase(PDBX_Entity_NonPoly.NAME)) {
            r = new PDBX_Entity_NonPoly(id);
        } else if (name.equalsIgnoreCase(Entity_Poly_Seq.NAME)) {
            r = new Entity_Poly_Seq(id);
        } else if (name.equalsIgnoreCase(Entity_Src_Gen.NAME)) {
            r = new Entity_Src_Gen(id);
        } else if (name.equalsIgnoreCase(Entity_Src_Nat.NAME)) {
            r = new Entity_Src_Nat(id);
        } else if (name.equalsIgnoreCase(Chem_Comp.NAME)) {
            r = new Chem_Comp(id);
        } else if (name.equalsIgnoreCase(PDBX_Poly_Seq_Scheme.NAME)) {
            r = new PDBX_Poly_Seq_Scheme(id);
        } else if (name.equalsIgnoreCase(PDBX_NonPoly_Scheme.NAME)) {
            r = new PDBX_NonPoly_Scheme(id);
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
        } else if (name.equalsIgnoreCase(uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Conn_Type.NAME)) {
            r = new uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Conn_Type(id);
        } else if (name.equalsIgnoreCase(uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Modification_Feature.NAME)) {
            r = new uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Modification_Feature(id);
        } else if (name.equalsIgnoreCase(PDBX_Struct_Conn_Angle.NAME)) {
            r = new PDBX_Struct_Conn_Angle(id);
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
        } else if (name.equalsIgnoreCase(uk.ac.leeds.ccg.mol.data.cif.columns.EM_Entity_Assembly.NAME)) {
            r = new uk.ac.leeds.ccg.mol.data.cif.columns.EM_Entity_Assembly(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Close_Contact.NAME)) {
            r = new PDBX_Validate_Close_Contact(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_RMSD_Bond.NAME)) {
            r = new PDBX_Validate_RMSD_Bond(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_RMSD_Angle.NAME)) {
            r = new PDBX_Validate_RMSD_Angle(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Torsion.NAME)) {
            r = new PDBX_Validate_Torsion(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Chiral.NAME)) {
            r = new PDBX_Validate_Chiral(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Planes.NAME)) {
            r = new PDBX_Validate_Planes(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Peptide_Omega.NAME)) {
            r = new PDBX_Validate_Peptide_Omega(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Main_Chain_Plane.NAME)) {
            r = new PDBX_Validate_Main_Chain_Plane(id);
        } else if (name.equalsIgnoreCase(PDBX_Validate_Polymer_Linkage.NAME)) {
            r = new PDBX_Validate_Polymer_Linkage(id);
        } else if (name.equalsIgnoreCase(Chem_Comp_Atom.NAME)) {
            r = new Chem_Comp_Atom(id);
        } else if (name.equalsIgnoreCase(PDBX_Unobs_Or_Zero_Occ_Residues.NAME)) {
            r = new PDBX_Unobs_Or_Zero_Occ_Residues(id);
        } else if (name.equalsIgnoreCase(Chem_Comp_Bond.NAME)) {
            r = new Chem_Comp_Bond(id);
        } else if (name.equalsIgnoreCase( uk.ac.leeds.ccg.mol.data.cif.columns.EM_Entity_Assembly_NaturalSource.NAME)) {
            r = new  uk.ac.leeds.ccg.mol.data.cif.columns.EM_Entity_Assembly_NaturalSource(id);
        } else if (name.equalsIgnoreCase(EM_Entity_Assembly_Recombinant.NAME)) {
            r = new EM_Entity_Assembly_Recombinant(id);
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
        } else if (name.equalsIgnoreCase(Atom_Site.NAME)) {
            r = new Atom_Site(id);
        } else if (name.equalsIgnoreCase(Atom_Type.NAME)) {
            r = new Atom_Type(id);
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
            //Columns columns = new Columns(parts[0].substring(1),
            //        db.getNextColumns_ID());
            Columns columns = getColumns(parts[0].substring(1),
                    db.getNextColumns_ID());
            db.addColumns(columns);
            // Initialise Columns
            while (line.startsWith(Mol_Strings.symbol_underscore)) {
                parts = line.split("\\."); // Need to escape the dot

                if (parts.length < 2) {
                    int debug = 1;
                }

                columns.addColumn(new Column(columns, parts[1].trim()));
                //columns.cols.put(new Column_ID(columns.cols.size()),
                //        new Column(columns, parts[1].trim()));
                line = reader.readLine();
                //System.out.println(line);
            }

            // Add values
            int row = 0;
            while (!(line.startsWith(Mol_Strings.symbol_underscore)
                    || line.startsWith(Mol_Strings.SYMBOL_HASH))) {
                ArrayList<String> values = getValues(line.trim());
                Row_ID rid = new Row_ID(row);
                while (values.size() != columns.getNCols()) {
                    /**
                     * This may occur where the next variable is on the next
                     * line which should start with ";".
                     */
                    String line2 = reader.readLine();
                    //System.out.println(line2);
                    if (line2.startsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
                        Column_ID cid = new Column_ID(values.size());
                        Column column = columns.columns.get(cid);
                        if (column == null) {
                            int debug = 1;
                        }
                        if (column.name.equalsIgnoreCase("name")) {
                            values.add(line2.substring(1)); // Strip off the semi-colon.
                            String line3 = reader.readLine();
                            //System.out.println(line3);
                            if (line3.equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                if (values.size() != columns.getNCols()) {
                                    String line4 = reader.readLine();
                                    //System.out.println(line4);
                                    values.addAll(getValues(line4));
                                    if (values.size() != columns.getNCols()) {
                                        int debug = 1;
                                    }
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
                            line2 = line2.substring(1);
                            while (!line3.equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                line2 += line3;
                                line3 = reader.readLine().trim();
                                //System.out.println(line3);
                            }
                            values.add(line2);                            
                        }
                    } else {
                        // There are cases where there is no line continuation symbol!
                        values.addAll(getValues(line2));
                        if (values.size() != columns.getNCols()) {
                            int debug = 1;
                        }
                    }
                }
                columns.data.put(rid, new TreeMap<>());
                for (int col = 0; col < values.size(); col++) {
                    Column_ID cid = new Column_ID(col);
                    columns.setValue(rid, cid, new Value(values.get(col)));
                }
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
//     * @param ids The ids to add1 to.
//     */
//    protected void addColumn(Category category, String name, ArrayList<Variable_ID> ids) {
//        Column column = new Column(category, name);
//        int key = cif.id2name.size();
//        Variable_ID id = new Variable_ID(key);
//        ids.add1(id);
//        cif.name2id.put(name, id);
//        cif.id2name.put(id, name);
//        category.setVariable(id, column);
//        category.variables.put(id, column);
//    }
}
