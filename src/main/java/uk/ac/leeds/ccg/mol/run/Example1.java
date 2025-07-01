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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.WRITE;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import org.rcsb.cif.CifBuilder;
import org.rcsb.cif.CifIO;
import org.rcsb.cif.model.CifFile;
import org.rcsb.cif.model.FloatColumn;
import org.rcsb.cif.schema.StandardSchemata;
import org.rcsb.cif.schema.mm.AtomSite;
import org.rcsb.cif.schema.mm.MmCifBlock;
import org.rcsb.cif.schema.mm.MmCifFile;
import uk.ac.leeds.ccg.generic.io.Generic_IO;

/**
 *
 * @author Andy Turner
 */
public class Example1 {

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
             * CIF and BinaryCIF are stored in the same data structure to access the
             * data, it does not matter where and in which format the data came from
             * all relevant IO operations are exposed by the CifIO class.
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
            
            ex.parseFile(mmCifFile);
    
            ex.translate(mmCifFile, 100d, 200d, 300d);
            
            
            // the created CifFile instance behaves like a parsed file and can be processed or written as needed
            Path p = Paths.get("C:", "Users", "geoagdt", "Downloads", "test.cif");
            Files.createFile(p);

            ex.writeFile(mmCifFile, p);
            
            cifFile = CifIO.readFromURL(p.toUri().toURL());
            mmCifFile = cifFile.as(StandardSchemata.MMCIF);
            ex.parseFile(mmCifFile);
            
            
            
            
            //ex.buildModel();
            ex.convertAlphaFold();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    /**
     * For downloading and parsing a CIF file.
     *
     * @param pdbId The PDB ID of the file to download.
     * @param parseBinary This indicates if the file is binary.
     * @throws IOException
     */
    public void parseFile(MmCifFile mmCifFile) throws IOException {
        
        // get first block of CIF
        MmCifBlock data = mmCifFile.getFirstBlock();

        // get category with name '_atom_site' from first block - access is type-safe, all categories
        // are inferred from the CIF schema
        AtomSite atomSite = data.getAtomSite();
        FloatColumn cartnX = atomSite.getCartnX();
        FloatColumn cartnY = atomSite.getCartnY();
        FloatColumn cartnZ = atomSite.getCartnZ();

        // obtain entry id
        String entryId = data.getEntry().getId().get(0);
        System.out.print("entryId ");
        System.out.println(entryId);

        // calculate the average x-coordinate
        OptionalDouble averageCartnX = cartnX.values().average();
        System.out.print("averageCartnX ");
        averageCartnX.ifPresent(System.out::println);

        // calculate the average y-coordinate
        OptionalDouble averageCartnY = cartnY.values().average();
        System.out.print("averageCartnY ");
        averageCartnY.ifPresent(System.out::println);

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
        
    }

    /**
     * 
     * @param mmCifFile
     * @param p
     * @throws IOException 
     */
    public void writeFile(MmCifFile mmCifFile, Path p) throws IOException{    
        BufferedOutputStream bos = Generic_IO.getBufferedOutputStream(p);
        bos.write(CifIO.writeBinary(mmCifFile));
        bos.close();
    }
    
    public void translate(MmCifFile mmCifFile, double dx, double dy, double dz) throws IOException{
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
            i ++;
        }
        i = 0;
        FloatColumn cartnY = atomSite.getCartnY();
        double[] ys = new double[(int) cartnY.values().count()];
        ite = cartnY.values().iterator();
        while (ite.hasNext()) {
            double y = ite.next();
            ys[i] = y + dy;
            i ++;
        }
        i = 0;
        FloatColumn cartnZ = atomSite.getCartnZ();
        double[] zs = new double[(int) cartnZ.values().count()];
        ite = cartnZ.values().iterator();
        while (ite.hasNext()) {
            double z = ite.next();
            zs[i] = z + dz;
            i ++;
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
                
        // the created CifFile instance behaves like a parsed file and can be processed or written as needed
        Path p = Paths.get("C:", "Users", "geoagdt", "Downloads", "translated.cif");
        Files.createFile(p);
        
        BufferedOutputStream bos = Generic_IO.getBufferedOutputStream(p);
        bos.write(CifIO.writeText(outCifFile));
        bos.close();
        
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
        Path p = Paths.get("C:", "Users", "geoagdt", "Downloads", "test.cif");
        Files.createFile(p);
        
        BufferedOutputStream bos = Generic_IO.getBufferedOutputStream(p);
        bos.write(CifIO.writeText(cifFile));
        bos.close();
        //System.out.println(new String(CifIO.writeText(cifFile)));
        

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
