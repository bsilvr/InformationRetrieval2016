/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

/**
 *
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Result {

    private final int docId;
    private final String filePath;
    private final int startLine;
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
        
        return (this.score < o.getScore()) ? 1 : (this.score > o.getScore()) ? -1 : (this.docId > o.getDocId()) ? 1 : (this.docId < o.getDocId()) ? -1 : 0;
    }
    
    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(this == o) return true;
        if (getClass() != o.getClass()) return false;
        Result tmp = (Result)o;
        return this.docId == tmp.getDocId();
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.docId;
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.score) ^ (Double.doubleToLongBits(this.score) >>> 32));
        return hash;
    }
    
    @Override
    public String toString(){
        
        return docId + " - " + filePath + " - " + startLine + " - " + score;
    }
}
