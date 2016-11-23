/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class CorpusReader {
    private String [] ignoreExtensions;
    
    private int currentFile;
    private boolean finishedReadDir;
    private ArrayList<CorpusFile> files;
    
    private CorpusFile[] corpusIterator;
    
    public CorpusReader(String [] ignoreExtensions){
        currentFile = 0;
        finishedReadDir = false;
        files = new ArrayList<>();
        this.ignoreExtensions = ignoreExtensions;
    }
    
    public CorpusReader(){
        currentFile = 0;
        finishedReadDir = false;
        files = new ArrayList<>();
        String [] t = {".pdf", ".docx", ".txt"};
        this.ignoreExtensions = t;
    }
    
    public synchronized void readDir(String path){
        // Ver o formato e guardar path e extension na array list recursivamente dentro de pastas
        File dir = new File(path);
        Collection<File> f = FileUtils.listFiles(dir,new IOFileFilter(){
            @Override
            public boolean accept(File dir, String name){
                return false;
            }

            @Override
            public boolean accept(File file) {
                String name = file.getName();
                boolean ignore = false;
                for (String ext : ignoreExtensions) {
                    if(name.toLowerCase().endsWith(ext) || name.toLowerCase().startsWith("."))
                        ignore = true;
                }
                if(!ignore && !file.isDirectory()){
                    CorpusFile fl = new CorpusFile(path + "/" + name, FilenameUtils.getExtension(name));
                    files.add(fl);
                }
                return !ignore;            
            }
        }
                ,TrueFileFilter.INSTANCE);
        corpusIterator = files.toArray(new CorpusFile[0]);
        finishedReadDir = true;
        notifyAll();
    }

    public ArrayList<CorpusFile> getFiles() {
        return files;
    }
    
    public synchronized CorpusFile getNextFile(){
        if(currentFile == corpusIterator.length) {
            return null;
        }
        if(!finishedReadDir){
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        return corpusIterator[currentFile++];
    }
}
