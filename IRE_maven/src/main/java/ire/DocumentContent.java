/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentContent {
    private static int id = 0;

    private int docId;
    private String filePath;
    private int startLine;
    private String content;
    
    public DocumentContent(String content, String filePath, int startLine){
        this.content = content;
        this.docId = getID();
        this.filePath = filePath;
        this.startLine = startLine;
    }

    public String getContent() {
        return content;
    }

    public int getDocId() {
        return docId;
    }
    
    private static synchronized int getID(){
        return DocumentContent.id++;
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
