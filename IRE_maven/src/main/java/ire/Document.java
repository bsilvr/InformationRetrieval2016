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
        this.docId = getID();
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
    
    public int getOriginalDocId() {
        return originalDocId;
    }

    int compareTo(Document b) {
        if(docId == b.getDocId()){
            return 0;
        }
        else if(docId < b.getDocId()){
            return -1;
        }
        return 1;
    }
    
    private static synchronized int getID(){
        return Document.id++;
    }
}
