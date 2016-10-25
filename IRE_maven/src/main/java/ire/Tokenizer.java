/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Tokenizer {
    private Pattern pattern = Pattern.compile("\\W");
    private ArrayList<String> tokens = new ArrayList<>();
    String [] stopWordsList;
    englishStemmer stemmer = new englishStemmer();
    
    public Tokenizer(String [] stopWordsList){
        this.stopWordsList = stopWordsList;
    }
    
    public String[] tokenize(String content, Document doc){
        
        content = content.replaceAll("[-()]", " ");
        //content = content.replace("-", " ");
        String [] words = content.split("\\s+");
        for(String w : words){
            String word = transform(w);
            if(word != null){
                //Token tk = new Token(word, doc);
                tokens.add(word);
            } 
        }
        return tokens.toArray(new String[0]);     
        
        //tokensIterator = tokens.iterator();
    }
        
    public String transform(String term){
        term = term.toLowerCase();
        if(useArraysBinarySearch(stopWordsList, term)){
            return null;
        }
        
        //term = term.replaceAll("\\W", "");
        term = pattern.matcher(term).replaceAll("");
        if(term.length()<2){
            return null;
        }
        
        try {
            int foo = Integer.parseInt(term);
            return Integer.toString(foo);
        } catch (Exception e) {
            
        }

        // Stemming
        stemmer.setCurrent(term);
        if (stemmer.stem()){
            return stemmer.getCurrent();
        }
        
        return term;
    }
    
    public ArrayList<String> getTokens() {
        return tokens;
    }
    
//    public Token getNextToken(){
//        if(tokensIterator.hasNext()) {
//            return tokensIterator.next();
//        }
//        return null;
//    }
    
    // http://www.programcreek.com/2014/04/check-if-array-contains-a-value-java/
    public static boolean useArraysBinarySearch(String[] arr, String targetValue) {	
	int a =  Arrays.binarySearch(arr, targetValue);
	if(a > 0)
            return true;
	else
            return false;
    }
}
