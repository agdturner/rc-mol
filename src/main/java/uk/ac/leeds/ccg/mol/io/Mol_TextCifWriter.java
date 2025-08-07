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

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.generic.core.Generic_Strings;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.mol.core.Mol_Environment;
import uk.ac.leeds.ccg.mol.core.Mol_Strings;
import uk.ac.leeds.ccg.mol.data.cif.CIF;
import uk.ac.leeds.ccg.mol.data.cif.Column;
import uk.ac.leeds.ccg.mol.data.cif.Column_ID;
import uk.ac.leeds.ccg.mol.data.cif.Columns;
import uk.ac.leeds.ccg.mol.data.cif.Columns_ID;
import uk.ac.leeds.ccg.mol.data.cif.DataItem;
import uk.ac.leeds.ccg.mol.data.cif.DataItems;
import uk.ac.leeds.ccg.mol.data.cif.DataItems_ID;
import uk.ac.leeds.ccg.mol.data.cif.Value;
import uk.ac.leeds.ccg.mol.data.cif.columns.Atom_Site;
import uk.ac.leeds.ccg.mol.data.cif.columns.Citation_Author;
import uk.ac.leeds.ccg.mol.data.cif.columns.Database_PDB_Caveat;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Name_Com;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Poly;
import uk.ac.leeds.ccg.mol.data.cif.columns.Entity_Src_Gen;
import uk.ac.leeds.ccg.mol.data.cif.columns.NDB_Struct_NA_Base_Pair_Step;
import uk.ac.leeds.ccg.mol.data.cif.columns.PDBX_Entity_NonPoly;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Conn;
import uk.ac.leeds.ccg.mol.data.cif.columns.Struct_Ref;
import uk.ac.leeds.ccg.mol.data.cif.data_items.EM_Entity_Assembly;
import uk.ac.leeds.ccg.mol.data.cif.data_items.PDBX_Struct_Assembly_Gen;

/**
 *
 * @author Andy Turner
 */
public class Mol_TextCifWriter {

    public static int LINE_CHAR_LENGTH_MAX = 2048;

    public static int NAME_CODE_LENGTH_MAX = 75;

    public static int HEADER_LENGTH_MAX = 80;

    public final HashMap<Integer, String> padding;

    public Mol_TextCifWriter() {
        padding = new HashMap<>();
        String s = "";
        for (int i = 0; i < LINE_CHAR_LENGTH_MAX; i++) {
            padding.put(i, s);
            s += Mol_Strings.symbol_space;
        }
    }

    /**
     * @param cif The CIF to write.
     * @param dir The directory to write to.
     * @param pdbId The first part of the filename.
     * @param name The second part of the filename.
     * @throws IOException
     */
    public void write(CIF cif, Path dir, String pdbId, String name) throws IOException {
        // Print/write from the in memory representation.
        // Set up writer
        Path outp = Paths.get(dir.toString(), pdbId + name + ".cif");
        try (BufferedWriter bw = Generic_IO.getBufferedWriter(outp, false)) {
            cif.dataBlocks.stream().forEach(x -> {
                try {
                    String s0 = x.dbh + Mol_Environment.EOL;
                    //System.out.print(s0);
                    bw.write(s0);
                    x.columnsAndDataItems.forEach(y -> {
                        try {
                            //System.out.println(Mol_Strings.SYMBOL_HASH);
                            bw.write(Mol_Strings.SYMBOL_HASH);
                            bw.write(Mol_Strings.symbol_space);
                            bw.write(Mol_Environment.EOL);
                            if (y instanceof Columns_ID id) {
                                //System.out.print(Mol_Strings.s_loop_);
                                bw.write(Mol_Strings.s_loop_);
                                bw.write(Mol_Environment.EOL);
                                Columns columns = x.getColumns(id);
                                // Header
                                columns.columns.keySet().forEach(cid -> {
                                    String columnName = columns.id2name.get(cid);
                                    try {
                                        writeName(bw, columns.name, columnName);
                                        //System.out.println();
                                        bw.write(Mol_Strings.symbol_space);
                                        bw.write(Mol_Environment.EOL);
                                    } catch (IOException ex1) {
                                        Logger.getLogger(Mol_TextCifReader.class.getName()).log(Level.SEVERE, null, ex1);
                                    }
                                });
                                // Values
                                columns.data.keySet().forEach(rid -> {
                                    TreeMap<Column_ID, Value> cols = columns.data.get(rid);
                                    StringBuilder sb = new StringBuilder();
                                    cols.keySet().forEach(cid -> {
                                        Value v = cols.get(cid);
                                        Column column = columns.columns.get(cid);
                                        int w = column.getWidth();
                                        int sw = v.v.length();
                                        String pad = padding.get(w - sw + 1);
                                        if (pad == null) {
                                            pad = " ";
                                        }
                                        if (cid.id == columns.getNCols() - 1) {
                                            pad = " ";
                                        }
                                        String sbs = sb.toString();
                                        String[] sbss = sbs.split(Mol_Environment.EOL);
                                        if (sw > CIF.HEADER_LENGTH_MAX) {
                                            // Handle special cases:
                                            if (columns.name.equalsIgnoreCase(Entity_Name_Com.NAME)) {
                                                if (sw + sbs.length() <= 130) {
                                                    add_s_pad(sb, pad, v.v);
                                                    //break;
                                                } else {
                                                    addMultiline1(sb, v.v);
                                                }
                                            } else if (columns.name.equalsIgnoreCase(Database_PDB_Caveat.NAME)) {
                                                addMultiline1(sb, v.v);
                                            } else if (columns.name.equalsIgnoreCase(Entity.NAME)
                                                    || columns.name.equalsIgnoreCase(PDBX_Entity_NonPoly.NAME)) {
                                                if (sw > 131) {
                                                    addMultiline1(sb, v.v);
                                                } else {
                                                    if (sbss[sbss.length - 1].length() + sw > 131) {
                                                        sb.append(Mol_Environment.EOL);
                                                    }
                                                    add_s_pad(sb, pad, v.v);
                                                }
                                            } else if (columns.name.equalsIgnoreCase(Entity_Poly.NAME)) {

                                                if (v.v.contains(";;")) {
                                                    int debug = 1;
                                                }

                                                addMultiline0(sb, sw, v.v);
                                            } else if (columns.name.equalsIgnoreCase(Struct_Ref.NAME)) {
                                                addMultiline0(sb, sw, v.v);
                                                sb.append(Mol_Environment.EOL);
                                            } else {
                                                addMultiline0(sb, sw, v.v);
                                            }
                                        } else {
                                            // Handle special cases:
                                            if (columns.name.equalsIgnoreCase(Entity.NAME)) {
                                                if (sbss[sbss.length - 1].length() + sw > 131) {
                                                    sb.append(Mol_Environment.EOL);
                                                }
                                                add_s_pad(sb, pad, v.v);
                                            } else if (columns.name.equalsIgnoreCase(Entity_Name_Com.NAME)) {
                                                if (sw + sbs.length() > 130) {
                                                    if (sbs.endsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                                        sb.append(Mol_Environment.EOL);
                                                    }
                                                    add_s_pad(sb, pad, v.v);
                                                } else {
                                                    if (v.v.contains(" ") && !v.v.startsWith("'")) {
                                                        addMultiline1(sb, v.v);
                                                    } else {
                                                        add_s_pad(sb, pad, v.v);
                                                    }
                                                }
                                            } else if (columns.name.equalsIgnoreCase(Atom_Site.NAME)) {
                                                add_s_pad(sb, pad, v.v);
                                            } else if (columns.name.equalsIgnoreCase(NDB_Struct_NA_Base_Pair_Step.NAME)
                                                    || columns.name.equalsIgnoreCase(Struct_Conn.NAME)) {
                                                if (sbss[sbss.length - 1].length() + sw > 131) {
                                                    sb.append(Mol_Environment.EOL);
                                                }
                                                if (column.name.equalsIgnoreCase("details") || column.name.equalsIgnoreCase("pdbx_dist_value")) {
                                                    pad = " ";
                                                }
                                                add_s_pad(sb, pad, v.v);
                                            } else if (columns.name.equalsIgnoreCase(Struct_Ref.NAME)) {
                                                if (sw >= CIF.HEADER_LENGTH_MAX) {
                                                    add2(sb, v.v.concat(Mol_Environment.EOL));
                                                } else {
                                                    add_s_pad(sb, pad, v.v);
                                                }
                                            } else if (columns.name.equalsIgnoreCase(Entity_Poly.NAME)) {
//                                                if (sbss[sbss.length - 1].length() >= CIF.HEADER_LENGTH_MAX) {
//                                                    if (sw > 10 || sbss[sbss.length - 1].length() > 120) {
//                                                        sb.append(Mol_Environment.EOL);
//                                                    }
//                                                }
//                                                if (column.name.equalsIgnoreCase("pdbx_strand_id")) {
//                                                    if (sw + sbs.length() >= CIF.HEADER_LENGTH_MAX) {
//                                                        if (sbs.endsWith(Mol_Strings.SYMBOL_SEMI_COLON)) {
//                                                            sb.append(Mol_Environment.EOL);
//                                                        }
//                                                    }
//                                                }
                                                if (sbss[sbss.length - 1].equalsIgnoreCase(Mol_Strings.SYMBOL_SEMI_COLON)) {
                                                    sb.append(Mol_Environment.EOL);
                                                }
                                                pad = " ";

                                                if (v.v.contains(";;")) {
                                                    int debug = 1;
                                                }

                                                add_s_pad(sb, pad, v.v);
                                            } else if (columns.name.equalsIgnoreCase(Entity_Src_Gen.NAME)) {
                                                if (sbss[sbss.length - 1].length() + sw > 131) {
                                                    sb.append(Mol_Environment.EOL);
                                                }
                                                add_s_pad(sb, pad, v.v);
                                            } else if (columns.name.equalsIgnoreCase(Citation_Author.NAME) && column.name.equalsIgnoreCase("name")) {
                                                if (!v.v.startsWith("'")) {
                                                    add2(sb, v.v);
                                                } else {
                                                    add_s_pad(sb, pad, v.v);
                                                }
                                            } else {
                                                if (v.v.contains(" ") && !v.v.startsWith("'")) {
                                                    addMultiline1(sb, v.v);
                                                    sb.append(Mol_Environment.EOL);
                                                } else {
                                                    add_s_pad(sb, pad, v.v);
                                                }
                                            }
                                        }
                                    });
                                    //sb.append(Mol_Strings.symbol_space);
                                    sb.append(Mol_Environment.EOL);
                                    try {
                                        //System.out.print(sb.toString());

                                        if (sb.toString().contains(";;")) {
                                            int debug = 1;
                                        }

                                        bw.write(sb.toString());
                                    } catch (IOException ex1) {
                                        Logger.getLogger(Mol_TextCifReader.class.getName()).log(Level.SEVERE, null, ex1);
                                        ex1.printStackTrace();
                                    }
                                });
                            } else {
                                DataItems dataItems = x.getDataItems((DataItems_ID) y);
                                int nml = dataItems.getNameMaxLength();
                                //dataItems.dataItems.values().forEach(z -> {
                                dataItems.dataItems.keySet().forEach(z -> {
                                    try {
                                        DataItem d = dataItems.dataItems.get(z);
                                        writeName(bw, dataItems.name, d.name);
                                        String dv = d.value;
                                        String pad = padding.get(nml - d.name.length() + 3);
                                        StringBuilder sb = new StringBuilder();
                                        // Handle special cases:
                                        if (dataItems.name.equalsIgnoreCase(EM_Entity_Assembly.NAME)) {
                                            if (sb.toString().length() + pad.length() + dv.length() > 130) {
                                                add1(sb, pad, dv);
                                            } else {
                                                add0(sb, pad, dv);
                                            }
                                        } else if (dataItems.name.equalsIgnoreCase(PDBX_Struct_Assembly_Gen.NAME)) {
                                            if (sb.toString().length() + pad.length() + dv.length() > CIF.HEADER_LENGTH_MAX) {
                                                add1(sb, pad, dv);
                                            } else {
                                                add0(sb, pad, dv);
                                            }
                                        } else {
                                            if (dv.contains(" ") && !dv.startsWith("'")) {
                                                addMultiline1(sb, dv);
                                                sb.append(Mol_Environment.EOL);
                                            } else {
                                                add0(sb, pad, dv);
                                            }
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
            bw.write(Mol_Strings.SYMBOL_HASH + Mol_Strings.symbol_space);
            bw.write(Mol_Environment.EOL);
        }
    }

    /**
     * @param sb The StringBuilder.
     * @param sw The length of s.
     * @param s The String.
     */
    protected void addMultiline0(StringBuilder sb, int sw, String s) {
        sb.append(Mol_Environment.EOL);
        sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
        if (s.contains("(") && Generic_Strings.countChars(s, '(') == Generic_Strings.countChars(s, ')')) {
            String ss = s.substring(0, CIF.HEADER_LENGTH_MAX);
            String sv = splitAndAppend(sb, s, ss);
            while (sv.length() > CIF.HEADER_LENGTH_MAX) {
                ss = sv.substring(0, CIF.HEADER_LENGTH_MAX);
                sv = splitAndAppend(sb, sv, ss);
            }
            sb.append(sv);
        } else {
            String ss = s.substring(0, CIF.HEADER_LENGTH_MAX);
            String sv = s.substring(CIF.HEADER_LENGTH_MAX, s.length());
            sb.append(ss);
            sb.append(Mol_Environment.EOL);
            while (sv.length() > CIF.HEADER_LENGTH_MAX) {
                ss = sv.substring(0, CIF.HEADER_LENGTH_MAX);
                sv = sv.substring(CIF.HEADER_LENGTH_MAX, sv.length());
                sb.append(ss);
                sb.append(Mol_Environment.EOL);
            }
            sb.append(sv);
        }
        sb.append(Mol_Environment.EOL);
        sb.append(Mol_Strings.SYMBOL_SEMI_COLON);

        if (sb.toString().contains(";;")) {
            int debug = 1;
        }
    }

    /**
     * @param sb The StringBuilder.
     * @param s A string.
     * @param ss The first CIF.HEADER_LENGTH_MAX characters of s.
     * @return A next sub-string of s.
     */
    protected String splitAndAppend(StringBuilder sb, String s, String ss) {
        if (Generic_Strings.countChars(ss, '(') != Generic_Strings.countChars(ss, ')')) {
            int li = ss.lastIndexOf('(');
            ss = ss.substring(0, li);
            s = s.substring(li, s.length());
        } else {
            s = s.substring(CIF.HEADER_LENGTH_MAX, s.length());
        }
        sb.append(ss);
        sb.append(Mol_Environment.EOL);
        return s;
    }

    /**
     * @param sb The StringBuilder.
     * @param s The String.
     */
    protected void addMultiline1(StringBuilder sb, String s) {
        sb.append(Mol_Environment.EOL);
        sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
        sb.append(s);
        sb.append(Mol_Environment.EOL);
        sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
    }

    /**
     *
     * @param sb The StringBuilder.
     * @param pad The padding to add1 before the variable
     * @param s The string to append after pad and before a space and EOL.
     */
    protected static void add0(StringBuilder sb, String pad, String s) {
        sb.append(pad);
        sb.append(s);
        sb.append(Mol_Strings.symbol_space);
        sb.append(Mol_Environment.EOL);
    }

    /**
     * Appends pad to sb, then calls
     * {@link #add2(java.lang.StringBuilder, java.lang.String)}.
     *
     * @param sb The StringBuilder.
     * @param pad The padding to prepend to sb.
     * @param s What is passed to
     * {@link #add2(java.lang.StringBuilder, java.lang.String)}.
     */
    protected static void add1(StringBuilder sb, String pad, String s) {
        sb.append(pad);
        add2(sb, s);
    }

    /**
     * Appends Mol_Environment.EOL, Mol_Strings.SYMBOL_SEMI_COLON, s,
     * Mol_Environment.EOL, Mol_Strings.SYMBOL_SEMI_COLON and
     * Mol_Environment.EOL to sb.
     *
     * @param sb The StringBuilder.
     * @param s The String to add between Mol_Environment.EOL and
     * Mol_Strings.SYMBOL_SEMI_COLON symbols.
     */
    protected static void add2(StringBuilder sb, String s) {
        sb.append(Mol_Environment.EOL);
        sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
        sb.append(s);
        sb.append(Mol_Environment.EOL);
        sb.append(Mol_Strings.SYMBOL_SEMI_COLON);
        sb.append(Mol_Environment.EOL);
    }

    /**
     * Appends s and pad to sb.
     *
     * @param sb The StringBuilder.
     * @param pad The padding to add after s.
     * @param s The String to append before pad.
     */
    protected static void add_s_pad(StringBuilder sb, String pad, String s) {
        sb.append(s);
        sb.append(pad);
    }

    /**
     * Appends pad and s to sb.
     *
     * @param sb The StringBuilder.
     * @param pad The padding to add before s.
     * @param s The String to append after pad.
     */
    protected void add_pad_s(StringBuilder sb, String pad, String s) {
        sb.append(pad);
        sb.append(s);
    }

    /**
     * For writing name.
     *
     * @param bw The BufferedWriter.
     * @param categoryName The category name.
     * @param variableName The variable name.
     * @throws IOException
     */
    protected void writeName(BufferedWriter bw, String categoryName, String variableName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(Mol_Strings.symbol_underscore);
        sb.append(categoryName);
        sb.append(Mol_Strings.symbol_dot);
        sb.append(variableName);
        //bw.write(sb.toString());
        bw.write(sb.toString());
    }
}
