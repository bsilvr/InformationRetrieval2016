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
public class Document {
    static String tmpPath = "./documents/";
    static int id = 0;
    
    String filePath;
    int docStartLine;
    int originalDocId;
    
    int docId;
    String documentPath;
    
    public Document(String filePath, int docStartLine, int originalDocId){
        this.docId = Document.id++;
        this.filePath = filePath;
        this.docStartLine = docStartLine;
        this.documentPath = tmpPath + "doc_" + id;
        this.originalDocId = originalDocId;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDocStartLine() {
        return docStartLine;
    }

    public int getDocId() {
        return docId;
    }

    public String getDocumentPath() {
        return documentPath;
    }
    
    
    
}
