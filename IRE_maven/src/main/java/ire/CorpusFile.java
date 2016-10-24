/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class CorpusFile {
    private String path;
    private String extension;
    
    public CorpusFile(String path, String extension){
        this.path = path;
        this.extension = extension;
    }
    
    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }
}
