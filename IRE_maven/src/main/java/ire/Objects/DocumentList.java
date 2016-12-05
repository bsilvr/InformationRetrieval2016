/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nustaq.serialization.FSTObjectInput;
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
    
    private BidiMap<String,Integer> filesMapping;
    private HashMap<Integer, Pair<Integer,Integer>> documents;
            
    public DocumentList(){
        filesMapping = new DualHashBidiMap();
        documents = new HashMap<>();
        basefolder = "indexes";
        debug = false;
        
        // Create folders they dont exist
        new File(basefolder+"/other/documents").mkdirs();
    }
    
    public DocumentList(String bf, boolean debug){
        filesMapping = new DualHashBidiMap();
        documents = new HashMap();
        basefolder = bf;
        this.debug = debug;
        
        // Create folders they dont exist
        new File(basefolder+"/other/documents").mkdirs();
    }
    
    public DocumentList(String bf){
        filesMapping = new DualHashBidiMap();
        documents = new HashMap();
        basefolder = bf;
        this.debug = false;
        
        // Create folders they dont exist
        new File(basefolder+"/other/documents").mkdirs();
    }
    
    public DocumentList(boolean debug){
        filesMapping = new DualHashBidiMap();
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
    
    public void writeFileMap(){
        try {
            String filename = basefolder + "/other/fileMapping";
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(filesMapping);
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void mergeDocuments(){
        ArrayList<String> files = readDir();
        //writeMergeDocuments();
        
        for(String f : files){
            documents.putAll(readObject("/other/documents/" + f));
            if (debug){
                System.err.println("Merged " + f);
            }
            new File(basefolder + "/other/documents/" + f).delete();
            System.gc();
        } 
        writeToDisk();
    }
    
    private ArrayList<String> readDir(){
        File folder = new File(basefolder + "/other/documents/");
        ArrayList<String> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if(fileEntry.getName().equals(".DS_Store") || fileEntry.isDirectory()){
                continue;
            }
            files.add(fileEntry.getName());
        }
        folder = null;
        return files;
    }
    
    private HashMap<Integer, Pair<Integer,Integer>> readObject(String filename){
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(basefolder + filename));
            Object res = in.readObject(HashMap.class);
            in.close();
            return (HashMap<Integer, Pair<Integer,Integer>>) res;
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    
    private void writeToDisk(){
        try {
            String filename = basefolder + "/other/documents_final";
            
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(documents);
            out.close();
                
        }catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeMergeDocuments(){
        HashMap<Integer, Pair<Integer,Integer>> docs = new HashMap<>();
        try {
            FSTObjectOutput out;
            
            String filename = basefolder + "/other/documents_final";

            out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(docs);
            out.close();
                
            out = null;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static synchronized int getFileID(){
        return DocumentList.fileID++;
    }
}
