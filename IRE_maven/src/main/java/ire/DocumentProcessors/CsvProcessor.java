/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.DocumentProcessors;

import ire.CorpusFile;
import ire.Document;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class CsvProcessor {
    public static String process(Document doc){
        
        File cf = new File(doc.getFilePath());
        
        int nLine= doc.getDocStartLine();
        int idx = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(cf))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                idx++;
                if(idx == nLine){
                    String[] parts = StringUtils.split(line, "\"");
                    parts[1] = parts[1].replaceAll("<.e>", "");
                    return parts[1];
                }
                
            }
            // line is not visible here.
        } catch (IOException ex) {
            Logger.getLogger(ArffProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static ArrayList<Document> identify(CorpusFile file){
        ArrayList<Document> documents = new ArrayList<>();
        File cf = new File(file.getPath());
        Charset utf8charset = Charset.forName("UTF-8");
        try {
            CSVParser parser = CSVParser.parse(cf, utf8charset, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
               
            }
        } catch (IOException ex) {
            Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }
}
