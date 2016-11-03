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
        
        int nLine= doc.getDocStartLine();
        File cf = new File(doc.getFilePath());
        Charset utf8charset = Charset.forName("UTF-8");
        CSVParser parser;
        
        int idx = 0;
        try {
            parser = CSVParser.parse(cf, utf8charset, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
                idx++;
                
                if(idx==nLine){
                    String body = csvRecord.get(5);
                    try{
                        return body + csvRecord.get(6);
                        
                    }catch(ArrayIndexOutOfBoundsException e) {
                        return body;
                    }
                    
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static ArrayList<Document> identify(CorpusFile file){
        
        int nLine = 0;
        ArrayList<Document> documents = new ArrayList<>();
        File cf = new File(file.getPath());
        Charset utf8charset = Charset.forName("UTF-8");
        CSVParser parser;
        try {
           
            parser = CSVParser.parse(cf, utf8charset, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
                if(nLine == 0){ nLine++; continue; }
                nLine++;
                
                Document doc = new Document(file.getPath(), nLine, Integer.parseInt(csvRecord.get(0)));
                documents.add(doc);
            }
        } catch (IOException ex) {
            Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return documents;
    }
}
