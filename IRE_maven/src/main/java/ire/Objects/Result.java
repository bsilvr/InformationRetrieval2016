/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

/**
 *
 * @author bernardo
 */
public class Result {

    private int docId;
    private String filePath;
    private int startLine;
    
    public Result(String filePath, int startLine, int docId){
        this.docId = docId;
        this.filePath = filePath;
        this.startLine = startLine;
    }

    public int getDocId() {
        return docId;
    }
    
    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * @return the startLine
     */
    public int getStartLine() {
        return startLine;
    }
}
