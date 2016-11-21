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
public class DocumentContent {
    static int id = 0;

    int docId;
    
    String content;
    
    public DocumentContent(String content){
        this.content = content;
        this.docId = getID();
    }

    public String getContent() {
        return content;
    }

    public int getDocId() {
        return docId;
    }
    
    private static synchronized int getID(){
        return Document.id++;
    }
}
