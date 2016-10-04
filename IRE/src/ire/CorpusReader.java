/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class CorpusReader {
    static String [] ignoreExtensions = {"pdf", "docx", "txt"};
    
    private ArrayList<CorpusFile> files;
    
    private Iterator<CorpusFile> corpusIterator = files.iterator();
    
    public CorpusReader(){
        files = new ArrayList<>();
    }
    
    public void readDir(String path){
        // Ver o formato e guardar path e extension na array list recursivamente dentro de pastas
    }

    public ArrayList<CorpusFile> getFiles() {
        return files;
    }
    
    public CorpusFile getNextFile(){
        if(corpusIterator.hasNext()) {
            return corpusIterator.next();
        }
        return null;
    }
}
