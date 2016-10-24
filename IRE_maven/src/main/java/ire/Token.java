/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

/**
 *
 * @author bernardo
 */
public class Token {
    
    private String term;
    private Document doc;
    
    public Token(String term, Document doc){
        this.term = term;
        this.doc = doc;
    }
    
    public String getTerm(){
        return term;
    }
    public Document getDocument(){
        return doc;
    }
            
}
