/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nustaq.serialization.FSTObjectOutput;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentList {
    
    private static int fileID = 0;
    private final String basefolder;
    private boolean debug;
    private int docCount = 0;
    
    private HashMap<String,Integer> filesMapping;
    private HashMap<Integer, Pair<Integer,Integer>> documents;
            
    public DocumentList(){
        filesMapping = new HashMap<>();
        documents = new HashMap<>();
        basefolder = "indexes";
        debug = false;
        
        // Create folders they dont exist
        new File(basefolder+"/other/documents").mkdirs();
    }
    
    public DocumentList(String bf, boolean debug){
        filesMapping = new HashMap<>();
        documents = new HashMap();
        basefolder = bf;
        this.debug = debug;
        
        // Create folders they dont exist
        new File(basefolder+"/other/documents").mkdirs();
    }
    
    public DocumentList(String bf){
        filesMapping = new HashMap<>();
        documents = new HashMap();
        basefolder = bf;
        this.debug = false;
        
        // Create folders they dont exist
        new File(basefolder+"/other/documents").mkdirs();
    }
    
    public DocumentList(boolean debug){
        filesMapping = new HashMap<>();
        documents = new HashMap();
        basefolder = "indexes";
        this.debug = debug;
        
        // Create folders they dont exist
        new File(basefolder+"/other/documents").mkdirs();
    }
    
    
    public synchronized void addDocument(String filePath, int docID, int line){
        int file;
        if(!filesMapping.containsKey(filePath)){
            file = getFileID();
            filesMapping.put(filePath, fileID);
        }
        else{
            file = filesMapping.get(filePath);
        }
        documents.put(docID, new ImmutablePair<>(file,line));
    }
    
    public synchronized void writeDocuments(){
        try {
            String filename = basefolder + "/other/documents/doc_" + docCount;
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(documents);
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        documents = new HashMap<>();
        docCount++;
    }
    
    private static synchronized int getFileID(){
        return DocumentList.fileID++;
    }
}
