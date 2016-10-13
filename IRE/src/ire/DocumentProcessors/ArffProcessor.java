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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class ArffProcessor {
    
   
    public static ArrayList<Document> process(CorpusFile file){
        ArrayList<Document> documents = new ArrayList<>();
        
        File cf = new File(file.getPath());
        
        boolean dataFound = false;
        try(BufferedReader br = new BufferedReader(new FileReader(cf))) {
            int nLine = 0;
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                nLine++;
                if(!dataFound){
                    if(line.equals("@DATA")){
                        dataFound = true;
                    }
                    continue;
                }
                String[] parts = line.split("\"");
                Document doc = new Document(file.getPath(), nLine, Integer.parseInt(parts[0].substring(0, parts[0].length()-1)));
                
                PrintWriter writer = new PrintWriter(doc.getDocumentPath(), "UTF-8");
                parts[1] = parts[1].replace("<e>", ""); // esta a dar erro aqui
                parts[1] = parts[1].replace("</e>", "");
                writer.println(parts[1]);
                writer.close();
            }
            // line is not visible here.
        } catch (IOException ex) {
            Logger.getLogger(ArffProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }
}
