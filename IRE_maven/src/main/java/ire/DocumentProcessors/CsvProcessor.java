/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.DocumentProcessors;

import ire.Buffer;
import ire.CorpusFile;
import ire.Document;
import ire.DocumentContent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class CsvProcessor implements Processor{
    
    private Buffer buffer;
    
    private static Pattern pattern = Pattern.compile("(?s)<code>.*?</code>|(?s)<CODE>.*?</CODE>|<(.|\n)*?>");
    
    public CsvProcessor(Buffer b){
        this.buffer = b;
    }
    
    @Override
    public ArrayList<Document> process(CorpusFile file){
        
        ArrayList<Document> documents = new ArrayList<>();
        File cf = new File(file.getPath());
        Charset utf8charset = Charset.forName("UTF-8");
        CSVParser parser;
        int nLine = 0;
        boolean title = true;
        boolean firstTime = true;
        StringBuilder currentDoc = new StringBuilder();
        String current;
        Document doc;
        try {
            parser = CSVParser.parse(cf, utf8charset, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
                if(nLine == 0){ nLine++; continue; }
                nLine++;
                
                if(firstTime){
                    firstTime = false;
                    currentDoc.append(csvRecord.get(5));
                    try{ 
                        currentDoc.append(csvRecord.get(6));
                    }catch(ArrayIndexOutOfBoundsException e) {
                        title = false;
                    }
                    current = parseTags(currentDoc.toString());
                }else{
                    currentDoc.append(csvRecord.get(5));
                    if(title){
                        currentDoc.append(csvRecord.get(6));
                    }
                    current = parseTags(currentDoc.toString());
                }
                
                doc = new Document(file.getPath(), Integer.parseInt(csvRecord.get(0)));
                documents.add(doc);
                buffer.addItem(new DocumentContent(current, doc.getDocId()));
                currentDoc.setLength(0);
            }
        } catch (IOException ex) {
            Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }
    
    @Override
    public synchronized DocumentContent getDocument(){
        return buffer.getItem();
    }
        
    private static String parseTags(String string) {
        /*string = string.replaceAll("(?s)<code>.*?</code>", "");
        string = string.replaceAll("(?s)<CODE>.*?</CODE>", "");
        string = string.replaceAll("<(.|\n)*?>", "");*/
        
        return pattern.matcher(string).replaceAll("");
    }
}
