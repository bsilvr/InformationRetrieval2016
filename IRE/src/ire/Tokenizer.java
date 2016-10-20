/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.ArffProcessor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Tokenizer {
    
    static char [] ignoreExtensions = {'!','.','\'', '#','$','%','&','(',')','*','+',',','-','.','/',':',';','<','>','=','?','@','[',']','\\','^','_','´','`','}','{','~','|'};
    private ArrayList<Token> tokens = new ArrayList<>();
    private Iterator<Token> tokensIterator;
    String [] stopWordsList;
    englishStemmer stemmer = new englishStemmer();
    
    public Tokenizer(String [] stopWordsList){
        this.stopWordsList = stopWordsList;
    }
    
    public void tokenize(Document doc){
        
        File cf = new File(doc.getDocumentPath());
        try(BufferedReader br = new BufferedReader(new FileReader(cf))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                line = line.replace("-", " ");
                String [] words = line.split("\\s+");
                for(String w : words){
                    String word = transform(w);
                    if(word != null){
                        Token tk = new Token(word, doc);
                        tokens.add(tk);
                    } 
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ArffProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        tokensIterator = tokens.iterator();
    }
        
    public String transform(String term){
        term = term.toLowerCase();
        if(Arrays.asList(stopWordsList).contains(term)){
            return null;
        }
        
        term = term.replaceAll("\\p{P}", "");
        if(term.length()<2){
            return null;
        }

        // Stemming
        stemmer.setCurrent(term);
        if (stemmer.stem()){
            return stemmer.getCurrent();
        }
        
        return term;
    }
    
    public ArrayList<Token> getTokens() {
        return tokens;
    }
    
    public Token getNextToken(){
        if(tokensIterator.hasNext()) {
            return tokensIterator.next();
        }
        return null;
    }
}
