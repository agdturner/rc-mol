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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rcsb.cif.CifBuilder;
import org.rcsb.cif.CifIO;
import org.rcsb.cif.model.CifFile;
import org.rcsb.cif.model.Column;
import org.rcsb.cif.model.FloatColumn;
import org.rcsb.cif.model.StrColumn;
import org.rcsb.cif.model.ValueKind;
import org.rcsb.cif.schema.StandardSchemata;
import org.rcsb.cif.schema.mm.AtomSite;
import org.rcsb.cif.schema.mm.Entry;
import org.rcsb.cif.schema.mm.MmCifBlock;
import org.rcsb.cif.schema.mm.MmCifFile;
import uk.ac.leeds.ccg.generic.io.Generic_IO;
import uk.ac.leeds.ccg.math.arithmetic.Math_BigDecimal;
import uk.ac.leeds.ccg.math.arithmetic.Math_Double;

/**
 *
 * @author Andy Turner
 */
public class Example1 {

    public static int LINE_CHAR_LENGTH_MAX = 2048;

    public static int NAME_CODE_LENGTH_MAX = 75;

    public static int HEADER_LENGTH_MAX = 80;
    
    /**
     * The path for IO.
     */
    Path dir;

    /**
     * Create a new instance.
     */
    public Example1() {
    }

    public static void main(String[] args) {
        Example1 ex = new Example1();
        try {
            //String pdbId = "4ug0";
            String pdbId = "6xu8";
            boolean parseBinary = true;
            System.out.println("Load " + pdbId + " parseBinary " + Boolean.toString(parseBinary));
            /**
             * CIF and BinaryCIF are stored in the same data structure to access
             * the data, it does not matter where and in which format the data
             * came from all relevant IO operations are exposed by the CifIO
             * class.
             */
            CifFile cifFile;
            if (parseBinary) {
                cifFile = CifIO.readFromURL(new URL("https://models.rcsb.org/" + pdbId + ".bcif"));
            } else {
                // parse CIF from RCSB PDB
                cifFile = CifIO.readFromURL(new URL("https://files.rcsb.org/download/" + pdbId + ".cif"));
            }
            // fine-grained options are available in the CifOptions class

            /**
             * Access can be generic or using a specified schema - currently
             * supports MMCIF and CIF_CORE.
             */
            MmCifFile mmCifFile = cifFile.as(StandardSchemata.MMCIF);

            ArrayList<String> lines = ex.parseFile(mmCifFile);
            
            

            //ex.translate(mmCifFile, 100d, 200d, 300d);

            // the created CifFile instance behaves like a parsed file and can be processed or written as needed
            ex.dir = Paths.get("C:", "Users", "geoagdt", "Downloads");
          Path p = Paths.get(ex.dir.toString(), "testbinary.cif");
//          ex.writeFile(mmCifFile, p, true);
            //p = Paths.get(ex.dir.toString(), "testtext.cif");
            p = Paths.get(ex.dir.toString(), "testtextsb.cif");
          //ex.writeFile(mmCifFile, p, false);
            
           ex.writeFile(lines, p);
//
            cifFile = CifIO.readFromURL(p.toUri().toURL());
            mmCifFile = cifFile.as(StandardSchemata.MMCIF);
            ex.parseFile(mmCifFile);
//
//            //ex.buildModel();
//            ex.convertAlphaFold();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    /**
     * For parsing a CIF file.
     *
     * @param mmCifFile The CIF file to parse.
     * @return The lines.
     */
    public ArrayList<String> parseFile(MmCifFile mmCifFile) {

        ArrayList<String> lines = new ArrayList<>();
  //      StringBuilder sb = new StringBuilder();
        // Get first block of CIF
        MmCifBlock data = mmCifFile.getFirstBlock();
        String str = "data_" + data.getBlockHeader();
        addLine(lines, str);
        //System.out.println("#");
        //Entry entry = data.getEntry();
        //System.out.print("_" + entry.getCategoryName());
        //StrColumn sc = entry.getId();
        //System.out.print("." + sc.getColumnName());
        //String entryId = sc.get(0);
        //System.out.println("\t" + entryId);
        data.categories().forEach(cat -> {
            //int rowCount = cat.getRowCount();
            //System.out.println("rowCount " + rowCount);
            addLine(lines, "#");
            String catName = cat.getCategoryName();
            //System.out.println(catName);
            List<String> colNames = cat.getColumnNames();
            int ncols = colNames.size();
            //System.out.println("ncols " + ncols);
            int catNrows = cat.getRowCount();
            //System.out.println("nrows " + nrows);
            if (catNrows > 1) {
                addLine(lines, "loop_");
            }
            StringBuilder[] rows = new StringBuilder[catNrows];
            for (int i = 0; i < catNrows; i++) {
                rows[i] = new StringBuilder();
            }
            cat.columns().forEach(col -> {
                int colNrows = col.getRowCount();
                if (colNrows == 1) {
                    addLine(lines, "_" + catName + "." + col.getColumnName() + "\t" + format(col, 0));
                } else {
                    addLine(lines, "_" + catName + "." + col.getColumnName());
                    for (int i = 0; i < colNrows; i++) {
                        try {
                            rows[i].append(format(col, i)).append(" ");
                        } catch(java.lang.ArrayIndexOutOfBoundsException e) {
                            System.err.println(e.getMessage());                            
                        }
                    }
                }
            });
            if (catNrows > 1) {
                for (int i = 0; i < catNrows; i++) {
                    // This is where coordinates could be changed...
                    addLine(lines, rows[i].substring(0, rows[i].length()));
                }
            }
        });

        // get category with name '_atom_site' from first block - access is type-safe, all categories
        // are inferred from the CIF schema
        AtomSite atomSite = data.getAtomSite();
        FloatColumn cartnX = atomSite.getCartnX();
        FloatColumn cartnY = atomSite.getCartnY();
        FloatColumn cartnZ = atomSite.getCartnZ();

        // calculate the average x-coordinate
        OptionalDouble averageCartnX = cartnX.values().average();
        System.out.print("averageCartnX ");
        averageCartnX.ifPresent(System.out::println);

        // calculate the average y-coordinate
        try {
        OptionalDouble averageCartnY = cartnY.values().average();
        System.out.print("averageCartnY ");
        averageCartnY.ifPresent(System.out::println);
        } catch (java.lang.NumberFormatException e) {
            System.out.println("cartnY.values().count " + cartnY.values().count());
            System.err.println(e.getMessage());
            cartnY.values().forEach(x -> {
                if (!Double.isFinite(x)) {
                    if (Double.isNaN(x)) {
                        System.out.println("NaN");
                    } else {
                        System.out.println("Infinity");
                    }                    
                }
                try {
                    Double.toString(x);
                } catch (java.lang.NumberFormatException e2) {
                    int debug = 1;
                }
            });
            
        }
        // calculate the average z-coordinate
        OptionalDouble averageCartnZ = cartnZ.values().average();
        System.out.print("averageCartnZ ");
        averageCartnZ.ifPresent(System.out::println);

        // print the last residue sequence id - this time #values() returns an IntStream
        OptionalInt lastLabelSeqId = atomSite.getLabelSeqId().values().max();
        System.out.print("lastLabelSeqId ");
        lastLabelSeqId.ifPresent(System.out::println);

        // print record type - or #values() may be text
        Optional<String> groupPdb = data.getAtomSite().getGroupPDB().values().findFirst();
        System.out.print("groupPdb ");
        groupPdb.ifPresent(System.out::println);
        return lines;
    }

    public void addLine(ArrayList<String> lines, String s) {
        System.out.println(s);
        if (s.equalsIgnoreCase("_atom_site.Cartn_x")) {
            int debug = 1;
        }
        lines.add(s.trim());
    }

    /**
     * @param col The column to parse.
     * @param i The row index to parse.
     * @return A String.
     */
    public String format(Column<?> col, int i) {
        
//        // Debug code
//        if (col.getRowCount() < i + 1) {
//            int debug = 1;
//        }
        
        String s = col.getStringData(i);
        ValueKind vk = col.getValueKind(i);
        if (vk == ValueKind.NOT_PRESENT) {
            s = ".";
        } else if (vk == ValueKind.UNKNOWN) {
            s = "?";
        }
        if (s.contains("'")) {
            s = "\"" + s + "\"";
        } else {
            if (s.contains(" ") || s.startsWith("_")) {
                s = "'" + s + "'";
            }
        }
        if (Math_Double.isDouble(s)) {
            //int debug = 1;
            s = Math_BigDecimal.round(new BigDecimal(s), -3).toPlainString();
        }
//        if (s.length() > 60) {
//            if (!s.contains(" ") && !s.contains(",")) {
//                int debug = 1;
//                s = "\n" + ";" + s.trim() + "\n" + ";" + "\n";
//            }
//        }
        return s;
    }

    /**
     * For writing CIF format data to file.
     *
     * @param mmCifFile The data to write.
     * @param p The path of the file to be written.
     * @param binary If true then mmCifFile is written in an CIF binary format
     * otherwise it is written in the CIF text format.
     * @throws IOException
     */
    public void writeFile(MmCifFile mmCifFile, Path p, boolean binary) throws IOException {
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
        try (var bos = Generic_IO.getBufferedOutputStream(p)) {
            if (binary) {
                bos.write(CifIO.writeBinary(mmCifFile));
            } else {
                byte[] bs = CifIO.writeText(mmCifFile);
                //System.out.println(new String(bs)));
                bos.write(bs);
            }
            bos.flush();
        }
    }
    
    /**
     * For writing CIF format data to file.
     *
     * @param lines The lines to write.
     * @param p The path of the file to be written.
     * @throws IOException
     */
    public void writeFile(ArrayList<String> lines, Path p) throws IOException {
        if (!Files.exists(p)) {
            Files.createFile(p);
        }
        try (var bos = Generic_IO.getBufferedOutputStream(p)) {
            lines.forEach(l -> {
                try {
                    write(l, bos);
                } catch (IOException ex) {
                    Logger.getLogger(Example1.class.getName()).log(Level.SEVERE, null, ex);
                }
            });            
            bos.flush();
        }
    }
    
    /**
     * Write out a string splitting it across multiple lines and adding a 
     * semicolon if necessary according to the CIF file specification which is 
     * based on a STAR format restricting lines to 80 characters.
     * @param s The string to write.
     * @param bos The BufferedOutputStream to write to.
     * @throws IOException if encountered.
     */
    public void write(String s, BufferedOutputStream bos) throws IOException {
        if (s.length() > LINE_CHAR_LENGTH_MAX) {
            bos.write((s.substring(0, LINE_CHAR_LENGTH_MAX) + "\n;").getBytes());
            write(s.substring(LINE_CHAR_LENGTH_MAX), bos);
        } else {
            bos.write((s + "\n").getBytes());
        }
    }

    /**
     * For translating.
     *
     * @param mmCifFile The CIF file to parse.
     */
    public void translate2(MmCifFile mmCifFile, double dx, double dy, double dz) throws IOException {
    
        ArrayList<String> lines = new ArrayList<String>();
        // Get first block of CIF
        MmCifBlock data = mmCifFile.getFirstBlock();
        String str = "data_" + data.getBlockHeader();
        addLine(lines, str);
        //System.out.println("#");
        //Entry entry = data.getEntry();
        //System.out.print("_" + entry.getCategoryName());
        //StrColumn sc = entry.getId();
        //System.out.print("." + sc.getColumnName());
        //String entryId = sc.get(0);
        //System.out.println("\t" + entryId);
        data.categories().forEach(cat -> {
            //int rowCount = cat.getRowCount();
            //System.out.println("rowCount " + rowCount);
            addLine(lines, "#");
            String catName = cat.getCategoryName();
            //System.out.println(catName);
            List<String> colNames = cat.getColumnNames();
            int ncols = colNames.size();
            //System.out.println("ncols " + ncols);
            int nrows = cat.getRowCount();
            //System.out.println("nrows " + nrows);
            if (nrows > 1) {
                addLine(lines, "loop_");
            }
            StringBuilder[] rows = new StringBuilder[nrows];
            for (int i = 0; i < nrows; i++) {
                rows[i] = new StringBuilder();
            }
            cat.columns().forEach(col -> {
                if (nrows == 1) {
                    addLine(lines, "_" + catName + "." + col.getColumnName() + "\t" + format(col, 0));
                } else {
                    addLine(lines, "_" + catName + "." + col.getColumnName());
                    for (int i = 0; i < nrows; i++) {
                        rows[i].append(format(col, i) + " ");
                    }
                }
            });
            if (nrows > 1) {
                for (int i = 0; i < nrows; i++) {
                    addLine(lines, rows[i].substring(0, rows[i].length()));
                }
            }
        });
        
        
        String blockHeader = data.getBlockHeader();
        AtomSite atomSite = data.getAtomSite();
        FloatColumn cartnX = atomSite.getCartnX();
        double[] xs = new double[(int) cartnX.values().count()];
        int i = 0;
        Iterator<Double> ite = cartnX.values().iterator();
        while (ite.hasNext()) {
            double x = ite.next();
            xs[i] = x + dx;
            i++;
        }
        i = 0;
        FloatColumn cartnY = atomSite.getCartnY();
        double[] ys = new double[(int) cartnY.values().count()];
        ite = cartnY.values().iterator();
        while (ite.hasNext()) {
            double y = ite.next();
            ys[i] = y + dy;
            i++;
        }
        i = 0;
        FloatColumn cartnZ = atomSite.getCartnZ();
        double[] zs = new double[(int) cartnZ.values().count()];
        ite = cartnZ.values().iterator();
        while (ite.hasNext()) {
            double z = ite.next();
            zs[i] = z + dz;
            i++;
        }
        MmCifFile outCifFile = CifBuilder.enterFile(StandardSchemata.MMCIF)
                // create a block
                .enterBlock(blockHeader)
                // create a category with name 'entry'
                .enterEntry()
                // set value of column 'id'
                .enterId()
                // to '1EXP'
                .add("1EXP")
                // leave current column
                .leaveColumn()
                // and category
                .leaveCategory()
                // create atom site category
                .enterAtomSite()
                // and specify some x-coordinates
                .enterCartnX()
                .add(xs)
                .leaveColumn()
                .enterCartnY()
                .add(ys)
                .leaveColumn()
                .enterCartnZ()
                .add(zs)
                .leaveColumn()
                // leaving the builder will release the CifFile instance
                .leaveCategory()
                .leaveBlock()
                .leaveFile();

        Path p = Paths.get("C:", "Users", "geoagdt", "Downloads", "translated.cif");
        writeFile(outCifFile, p, false);
        MmCifFile mmCifFile2 = outCifFile.as(StandardSchemata.MMCIF);
        parseFile(mmCifFile2);
    }
    
    
    /**
     * For translating.
     *
     * @param mmCifFile The CIF file to parse.
     * @param dx
     * @param dy
     * @param dz
     * @throws java.io.IOException
     */
    public void translate(MmCifFile mmCifFile, double dx, double dy, double dz) throws IOException {
        // Get first block of CIF
        MmCifBlock data = mmCifFile.getFirstBlock();
        String blockHeader = data.getBlockHeader();
        AtomSite atomSite = data.getAtomSite();
        FloatColumn cartnX = atomSite.getCartnX();
        double[] xs = new double[(int) cartnX.values().count()];
        int i = 0;
        Iterator<Double> ite = cartnX.values().iterator();
        while (ite.hasNext()) {
            double x = ite.next();
            xs[i] = x + dx;
            i++;
        }
        i = 0;
        FloatColumn cartnY = atomSite.getCartnY();
        double[] ys = new double[(int) cartnY.values().count()];
        ite = cartnY.values().iterator();
        while (ite.hasNext()) {
            double y = ite.next();
            ys[i] = y + dy;
            i++;
        }
        i = 0;
        FloatColumn cartnZ = atomSite.getCartnZ();
        double[] zs = new double[(int) cartnZ.values().count()];
        ite = cartnZ.values().iterator();
        while (ite.hasNext()) {
            double z = ite.next();
            zs[i] = z + dz;
            i++;
        }
        MmCifFile outCifFile = CifBuilder.enterFile(StandardSchemata.MMCIF)
                // create a block
                .enterBlock(blockHeader)
                // create a category with name 'entry'
                .enterEntry()
                // set value of column 'id'
                .enterId()
                // to '1EXP'
                .add("1EXP")
                // leave current column
                .leaveColumn()
                // and category
                .leaveCategory()
                // create atom site category
                .enterAtomSite()
                // and specify some x-coordinates
                .enterCartnX()
                .add(xs)
                .leaveColumn()
                .enterCartnY()
                .add(ys)
                .leaveColumn()
                .enterCartnZ()
                .add(zs)
                .leaveColumn()
                // leaving the builder will release the CifFile instance
                .leaveCategory()
                .leaveBlock()
                .leaveFile();

        Path p = Paths.get("C:", "Users", "geoagdt", "Downloads", "translated.cif");
        writeFile(outCifFile, p, false);
        MmCifFile mmCifFile2 = outCifFile.as(StandardSchemata.MMCIF);
        parseFile(mmCifFile2);
    }

    public void buildModel() throws IOException {
        // all builder functionality is exposed by the CifBuilder class
        // again access can be generic or following a given schema
        MmCifFile cifFile = CifBuilder.enterFile(StandardSchemata.MMCIF)
                // create a block
                .enterBlock("1EXP")
                // create a category with name 'entry'
                .enterEntry()
                // set value of column 'id'
                .enterId()
                // to '1EXP'
                .add("1EXP")
                // leave current column
                .leaveColumn()
                // and category
                .leaveCategory()
                // create atom site category
                .enterAtomSite()
                // and specify some x-coordinates
                .enterCartnX()
                .add(1.0, -2.4, 4.5)
                // values can be unknown or not specified
                .markNextUnknown()
                .add(-3.14, 5.0)
                .leaveColumn()
                // after leaving, the builder is in AtomSite again and provides column names
                .enterCartnY()
                .add(0.0, -1.0, 2.72)
                .markNextNotPresent()
                .add(42, 100)
                .leaveColumn()
                // leaving the builder will release the CifFile instance
                .leaveCategory()
                .leaveBlock()
                .leaveFile();

        // the created CifFile instance behaves like a parsed file and can be processed or written as needed
        Path p = Paths.get(dir.toString(), "test.cif");
        writeFile(cifFile, p, false);

        System.out.println(cifFile.getFirstBlock().getEntry().getId().get(0));
        cifFile.getFirstBlock()
                .getAtomSite()
                .getCartnX()
                .values()
                .forEach(System.out::println);
    }

    public void convertAlphaFold() throws IOException {
        String id = "AF-Q76EI6-F1-model_v4";

        CifFile cifFile = CifIO.readFromURL(new URL("https://alphafold.ebi.ac.uk/files/" + id + ".cif"));
        MmCifFile mmCifFile = cifFile.as(StandardSchemata.MMCIF);

        // print average quality score
        System.out.println(mmCifFile.getFirstBlock()
                .getMaQaMetricLocal()
                .getMetricValue()
                .values()
                .average()
                .orElseThrow());

        //System.out.println(mmCifFile.toString());
        //System.out.println(mmCifFile.toString());
        // convert to BinaryCIF representation
        byte[] output = CifIO.writeBinary(mmCifFile);
    }

}
