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
    private double score;
    
    public Result(String filePath, int startLine, int docId, double score){
        this.docId = docId;
        this.filePath = filePath;
        this.startLine = startLine;
        this.score = score;
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
    
    public double getScore(){
        return score;
    }
    
    public void addScore(double score){
        this.score += score;
    }
    
    public int compareTo(Result o){
        
        return (this.score > o.getScore()) ? 1 : (this.score < o.getScore()) ? -1 : (this.docId > o.getDocId()) ? 1 : (this.docId < o.getDocId()) ? -1 : 0;
    }
    
    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(this == o) return true;
        Result tmp = (Result)o;
        return this.docId == tmp.getDocId();
        
    }
}
