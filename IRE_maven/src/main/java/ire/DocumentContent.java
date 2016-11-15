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

    int docId;
    
    String content;
    
    public DocumentContent(String content, int docId){
        this.content = content;
        this.docId = docId;
    }

    public String getContent() {
        return content;
    }

    public int getDocId() {
        return docId;
    }
}
