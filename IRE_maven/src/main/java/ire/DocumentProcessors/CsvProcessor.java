/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.DocumentProcessors;

import ire.Objects.Buffer;
import ire.Objects.CorpusFile;
import ire.Objects.DocumentContent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class CsvProcessor implements Processor{
    
    private final Buffer buffer;
    private final static Pattern PATTERN = Pattern.compile("(?s)<code>.*?</code>|(?s)<CODE>.*?</CODE>|<(.)*?>|(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    
    public CsvProcessor(Buffer b){
        this.buffer = b;
    }
    
    @Override
    public void process(CorpusFile file){
        
        File cf = new File(file.getPath());
        Charset utf8charset = Charset.forName("UTF-8");
        CSVParser parser;
        int nLine = 0;
        boolean title = true;
        boolean firstTime = true;
        StringBuilder currentDoc = new StringBuilder();
        String current;
        try {
            parser = CSVParser.parse(cf, utf8charset, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
                if(nLine == 0){ nLine++; continue; }
                
                if(firstTime){
                    firstTime = false;
                    currentDoc.append(csvRecord.get(5));
                    try{ 
                        currentDoc.append(" ");
                        currentDoc.append(csvRecord.get(6));
                    }catch(ArrayIndexOutOfBoundsException e) {
                        title = false;
                    }
                    
                }else{
                    currentDoc.append(csvRecord.get(5));
                    if(title){
                        currentDoc.append(" ");
                        currentDoc.append(csvRecord.get(6));
                    }
                }
                current = currentDoc.toString();              
                current = parseTags(current);
                
                buffer.addItem(new DocumentContent(current, file.getPath(), nLine));
                
                currentDoc.setLength(0);
                nLine++;
            }
        } catch (IOException ex) {
            Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public DocumentContent getDocument(){
        return buffer.getItem();
    }
        
    private static String parseTags(String string) {
        return PATTERN.matcher(string).replaceAll("");
    }
}
